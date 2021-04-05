package com.bin.media.service.impl;

import com.bin.common.fastdfs.FastDfsClient;
import com.bin.media.service.MaterialService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.wemedia.WmMaterialMapper;
import com.bin.model.mappers.wemedia.WmNewsMaterialMapper;
import com.bin.model.media.dtos.WmMaterialDto;
import com.bin.model.media.dtos.WmMaterialListDto;
import com.bin.model.media.dtos.WmNewsDto;
import com.bin.model.media.pojos.WmMaterial;
import com.bin.model.media.pojos.WmUser;
import com.bin.utils.threadlocal.WmThreadLocalUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/26 10:56
 */

@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    @Value("${FILE_SERVER_URL}")
    private String fileServerUrl;

    @Autowired
    private FastDfsClient fastDfsClient;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Override
    public ResponseResult uploadPicture(MultipartFile file) {
        WmUser user = WmThreadLocalUtils.getUser();
        if (file == null){
            //参数无效
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀名（图片格式）
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (!extName.matches("(gif|png|jpg|jpeg|JPG)")){
            //图片格式有误
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_IMAGE_FORMAT_ERROR,"图片格式有误");
        }
        String filename = null;
        try {
            //group_name=group1, remote_filename=M00/00/00/rB6qWWBA4yaAD9_CAAAAGI4iG5E667.txt
            //返回组名和文件名的组合
            filename = fastDfsClient.uploadFile(file.getBytes(), extName);
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("自媒体用户上传图片失败：用户id[{}]", user.getId());
            log.error("图片名称：[{}]", originalFilename);
            log.error("错误信息：[{}]", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
        WmMaterial material = new WmMaterial();
        material.setUserId(user.getId());
        material.setCreatedTime(new Date());
        material.setIsCollection((short) 0);
        material.setType((short) 0);
        material.setUrl(filename);
        wmMaterialMapper.insertPicture(material);
        material.setUrl(fileServerUrl + filename);
        return ResponseResult.okResult(material);
    }

    @Override
    public ResponseResult deletePicture(WmMaterialDto dto) {
        WmUser user = WmThreadLocalUtils.getUser();
        if (dto == null || dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmMaterial material = wmMaterialMapper.selectByPrimaryKey(dto.getId());
        if (material == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        int count = wmNewsMaterialMapper.countByMid(dto.getId());
        if (count > 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "当前图片已被引用，无法删除!");
        }
        String fileId = material.getUrl().replace(fileServerUrl, "");
        try {
            fastDfsClient.delFile(fileId);
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("图片删除异常：[{}]", fileId);
            log.error("自媒体用户：[{}]", user.getId());
            log.error("错误信息", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
        wmMaterialMapper.deleteByPrimaryKey(dto.getId());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult findList(WmMaterialListDto dto) {
        dto.checkParam();
        Long userId = WmThreadLocalUtils.getUser().getId();
        List<WmMaterial> wmMaterialList = wmMaterialMapper.findListByUidAndStatus(dto, userId);
        List<WmMaterial> pictureList = wmMaterialList.stream().map(item -> {
            item.setUrl(fileServerUrl + item.getUrl());
            return item;
        }).collect(Collectors.toList());
        int total = wmMaterialMapper.countListByUidAndStatus(dto, userId);
        Map<String, Object> data = new HashMap<>();
        data.put("curPage", dto.getPage());
        data.put("size", dto.getSize());
        data.put("list", pictureList);
        data.put("total", total);

        return ResponseResult.okResult(data);
    }

    @Override
    public ResponseResult changeUserMaterialStatus(WmMaterialDto dto, Short type) {
        if (dto == null || dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmUser user = WmThreadLocalUtils.getUser();
        wmMaterialMapper.updateStatusByUidAndId(dto.getId(), user.getId(), type);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


}
