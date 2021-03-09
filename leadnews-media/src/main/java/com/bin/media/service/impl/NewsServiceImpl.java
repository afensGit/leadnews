package com.bin.media.service.impl;

import com.bin.media.constans.WmMediaConstans;
import com.bin.media.service.NewsService;
import com.bin.model.common.dtos.PageResponseResult;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.wemedia.WmMaterialMapper;
import com.bin.model.mappers.wemedia.WmNewsMapper;
import com.bin.model.mappers.wemedia.WmNewsMaterialMapper;
import com.bin.model.media.dtos.WmNewsDto;
import com.bin.model.media.dtos.WmNewsPageReqDto;
import com.bin.model.media.pojos.WmMaterial;
import com.bin.model.media.pojos.WmNews;
import com.bin.model.media.pojos.WmUser;
import com.bin.utils.threadlocal.WmThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 9:24
 */

@Service
@Slf4j
public class NewsServiceImpl implements NewsService {

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${FILE_SERVER_URL}")
    private String fileServerUrl;

    @Override
    public ResponseResult saveNews(WmNewsDto dto, Short type) {
        //判断参数的有效性
        if (dto == null || StringUtils.isEmpty(dto.getContent())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmUser user = WmThreadLocalUtils.getUser();
        //如果是修改文章，则先删除所以文章和图片的关联信息
        if (dto.getId() != null){
            wmNewsMaterialMapper.deleteByNewId(dto.getId());
        }
        //解析文章内容，获取图文关联信息
        List<Map> list = new ArrayList<>();
        try {
            list = objectMapper.readValue(dto.getContent(), List.class);
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("文章内容解析失败！");
            log.error("文章内容信息：[{}]", dto.getContent());
            log.error("错误信息：[{}]", e);
        }
        //获取文章中的图片信息
        Map<String, Object> imageMap = extractUrlInfo(list);
        Map<String,Object> materials = (Map<String, Object>)imageMap.get("materials");
        int imageNum = (int)imageMap.get("imageNum");
        //保存文章发布信息
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        if (WmMediaConstans.WM_NEWS_TYPE_AUTO.equals(dto.getType())){
            saveWmNews(wmNews, imageNum, type);
        }else {
            saveWmNews(wmNews, dto.getType(), type);
        }
        //保存文章内容中图片和当前文章信息
        if (materials.keySet().size() != 0){
            ResponseResult result = saveRelationInfoForContent(materials, wmNews.getId());
            if (result != null){
                return result;
            }
        }
        //关联封面图片
        ResponseResult result = coverImagesRelation(dto, materials, wmNews, imageNum);
        if (result != null){
            return result;
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult selectNewsList(WmNewsPageReqDto dto) {
        if (dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        WmUser user = WmThreadLocalUtils.getUser();
        List<WmNews> newsList = wmNewsMapper.selectBySelective(dto, user.getId());
        int total = wmNewsMapper.selectNewsCount(dto, user.getId());
        PageResponseResult result = new PageResponseResult(dto.getPage(), dto.getSize(), total);
        result.setData(newsList);
        result.setHost(fileServerUrl);
        return result;
    }

    @Override
    public ResponseResult selectNewsById(WmNewsDto dto) {
        if (dto == null || dto.getId() ==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "文章id不可为空");
        }
        WmNews news = wmNewsMapper.selectNewsById(dto.getId());
        if (news == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章信息不存在");
        }
        ResponseResult result = ResponseResult.okResult(news);
        result.setHost(fileServerUrl);
        return result;
    }

    @Override
    public ResponseResult deleteNewsById(WmNewsDto dto) {
        if (dto == null || dto.getId() ==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews news = wmNewsMapper.selectNewsById(dto.getId());
        if(news == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章不存在");
        }
        //判断文章是否已经通过审核或已发布
        if (news.getStatus().equals(WmMediaConstans.WM_NEWS_PUBLISH_STATUS) ||
                news.getStatus().equals(WmMediaConstans.WM_NEWS_AUTHED_STATUS)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "当前文章已通过审核，不可删除");
        }
        //伤处文章关联信息
        wmNewsMaterialMapper.deleteByNewId(news.getId());
        //删除文章
        wmNewsMapper.deleteNewsById(news.getId());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 封面图片信息保存
     * @param dto
     * @param Materials
     * @param wmNews
     * @param imageNum
     * @return
     */
    private ResponseResult coverImagesRelation(WmNewsDto dto, Map<String, Object> Materials, WmNews wmNews, int imageNum){
        List<String> images = dto.getImages();
        if (!WmMediaConstans.WM_NEWS_TYPE_AUTO.equals(dto.getType()) && dto.getType() != images.size()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "图文模式不匹配");
        }
        //如果是自动匹配封面
        if (WmMediaConstans.WM_NEWS_TYPE_AUTO.equals(dto.getType())){
            images = new ArrayList<>();
            if (imageNum >= WmMediaConstans.WM_NEWS_SINGLE_IMAGE && imageNum <= 2){
                //如果文章图片只有一张或两张
                for (Object value : Materials.values()) {
                    images.add(String.valueOf(value));
                    break;
                }
            }
            //如果文章图片大于等于三张
            if (imageNum >= WmMediaConstans.WM_NEWS_MANY_IMAGE){
                for (int i = 0; i < WmMediaConstans.WM_NEWS_MANY_IMAGE; i++){
                    images.add(String.valueOf(Materials.get(String.valueOf(i))));
                }
            }
            if (images.size() != 0){
                ResponseResult result = saveRelationInfoForCover(images, wmNews.getId());
                if (result != null){
                    return result;
                }
            }
        }else if (!CollectionUtils.isEmpty(images)){
            //不是自动匹配封面
            ResponseResult result = saveRelationInfoForCover(images, wmNews.getId());
            if (result != null){
                return result;
            }
        }
        if (images != null){
            //更新文章信息
            wmNews.setImages(StringUtils.join(images.stream().map(s -> s.replace(fileServerUrl,"")).collect(Collectors.toList()), ","));
            wmNewsMapper.updateNewsByPrimaryKey(wmNews);
        }
        return null;
    }

    /**
     * 保存文章封面图片关联关系信息
     * @param images
     * @param newsId
     * @return
     */
    private ResponseResult saveRelationInfoForCover(List<String> images, Integer newsId){
        Map<String, Object> materials = new HashMap<>();
        for (int i = 0; i < images.size(); i++){
            String fileId = images.get(i);
            materials.put(String.valueOf(i), fileId.replace(fileServerUrl, ""));
        }
        return saveRelationInfo(materials, newsId, WmMediaConstans.WM_IMAGE_REFERENCE);
    }

    /**
     * 解析文章内容关联图片
     * @param list
     * @return
     */
    public Map<String, Object> extractUrlInfo(List<Map> list){
        //保存结果
        Map<String, Object> result = new HashMap<>();
        //保存图片地址
        Map<String, Object> materials = new HashMap<>();
        //序号
        int ord = 0;
        //图片总数
        int imageNum = 0;
        for (Map map : list) {
            ord++;
            if (WmMediaConstans.WM_NEWS_TYPE_IMAGE.equals(map.get("type"))){
                imageNum++;
                String imageUrl = String.valueOf(map.get("value"));
                if (imageUrl.startsWith(fileServerUrl)){
                    imageUrl = imageUrl.replace(fileServerUrl, "");
                }
                materials.put(String.valueOf(ord), imageUrl);
            }
        }
        result.put("materials", materials);
        result.put("imageNum", imageNum);
        return result;
    }

    /**
     * 保存文章
     * @param wmNews
     * @param imageNum
     * @param type
     */
    public void saveWmNews(WmNews wmNews, int imageNum, Short type){
        if (imageNum == WmMediaConstans.WM_NEWS_SINGLE_IMAGE){
            wmNews.setType(WmMediaConstans.WM_NEWS_SINGLE_IMAGE);
        }else if (imageNum >= WmMediaConstans.WM_NEWS_MANY_IMAGE){
            wmNews.setType(WmMediaConstans.WM_NEWS_MANY_IMAGE);
        }else {
            wmNews.setType(WmMediaConstans.WM_NEWS_NONE_IMAGE);
        }
        WmUser user = WmThreadLocalUtils.getUser();
        wmNews.setUserId(user.getId());
        wmNews.setStatus(type);
        wmNews.setCreatedTime(new Date());
        wmNews.setPublishTime(new Date());
        //wmNews.setEnable(WmMediaConstans.WM_NEWS_SUMMIT_STATUS);

        if (wmNews.getId() == null){
            wmNewsMapper.insertNewsForEdit(wmNews);
        }else {
            wmNewsMapper.updateNewsByPrimaryKey(wmNews);
        }
    }

    /**
     * 保存内容图片关联信息
     * @param material
     * @param newsId
     * @return
     */
    private ResponseResult saveRelationInfoForContent(Map<String, Object> material, Integer newsId){
        return saveRelationInfo(material, newsId, WmMediaConstans.WM_CONTENT_REFERENCE);
    }

    /**
     * 保存关联关系到数据库
     * @param materials
     * @param newsId
     * @param type
     * @return
     */
    private ResponseResult saveRelationInfo(Map<String, Object> materials, Integer newsId, Short type){
        WmUser user = WmThreadLocalUtils.getUser();
        //根据userId和图片Url查询素材图片id
        List<WmMaterial> imageUrlList = wmMaterialMapper.findMaterialByUserIdAndImageUrl(user.getId(), materials.values());
        if (!CollectionUtils.isEmpty(imageUrlList)){
            Map<String, Object> UrlIdMap = imageUrlList.stream().collect(Collectors.toMap(WmMaterial::getUrl, WmMaterial::getId));
            for (String key : materials.keySet()) {
                //获取关联的图片id
                String fileId = String.valueOf(UrlIdMap.get(materials.get(key)));
                if (StringUtils.isEmpty(fileId)){
                    return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "应用图片异常");
                }
                materials.put(key, fileId);
            }
            //储存关联
            wmNewsMaterialMapper.saveRelationsByContent(materials, newsId, type);
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return null;
    }


}
