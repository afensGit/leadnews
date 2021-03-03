package com.bin.article.service.impl;

import com.bin.article.service.AppArticleInfoService;
import com.bin.model.article.dtos.ArticleInfoDto;
import com.bin.model.article.pojos.ApArticleConfig;
import com.bin.model.article.pojos.ApArticleContent;
import com.bin.model.article.pojos.ApAuthor;
import com.bin.model.article.pojos.ApCollection;
import com.bin.model.behavior.pojos.ApBehaviorEntry;
import com.bin.model.behavior.pojos.ApLikesBehavior;
import com.bin.model.behavior.pojos.ApUnlikesBehavior;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.crawler.core.parse.ZipUtils;
import com.bin.model.mappers.app.*;
import com.bin.model.user.pojos.ApUser;
import com.bin.model.user.pojos.ApUserFollow;
import com.bin.utils.common.BurstUtils;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/22 23:03
 */
@Service
public class AppArticleInfoServiceImpl implements AppArticleInfoService {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private ApUnlikesBehaviorMapper apUnlikesBehaviorMapper;

    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;

    @Autowired
    private ApCollectionMapper apCollectionMapper;

    @Autowired
    private ApAuthorMapper apAuthorMapper;

    @Autowired
    private ApUserFollowMapper apUserFollowMapper;
    /**
     * 获取文章信息
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult loadArticleInfo(Integer articleId) {
        //判断参数有效性
        if (articleId == null || articleId < 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //根据文章id查询文章配置信息
        ApArticleConfig apArticleConfig = apArticleConfigMapper.selectConfigByArticleId(articleId);
        Map<String, Object> data = new HashMap<>();
        if (apArticleConfig == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }else if (!apArticleConfig.getIsDelete()){
            ApArticleContent apArticleContent = apArticleContentMapper.selectArticleById(articleId);
            String content = ZipUtils.gunzip(apArticleContent.getContent());
            apArticleContent.setContent(content);
            data.put("content", apArticleContent);
        }
        data.put("config", apArticleConfig);
        return ResponseResult.okResult(data);
    }

    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {
        //判断设备id及用户id的有效性
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null && dto.getEquipmentId() == null){
            //用户id和设备id不能同时为null
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if (user != null){
            userId = user.getId();
        }
        ApBehaviorEntry entry = apBehaviorEntryMapper.selectByUserIdOrEquipment(userId, dto.getEquipmentId());
        if (entry == null){
            //参数错误
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        boolean isUnLike = false;
        boolean isLike = false;
        boolean isCollection = false;
        boolean isFollow = false;

        //获取分库节点
        String burst = BurstUtils.groudOne(entry);
        //判断是否是不喜欢
        ApUnlikesBehavior unLike = apUnlikesBehaviorMapper.selectLastUnLike(entry.getEntryId(), dto.getArticleId());
        if (unLike != null && unLike.getType() == ApUnlikesBehavior.Type.UNLIKE.getCode()){
            isUnLike = true;
        }

        //判断是否是喜欢
        ApLikesBehavior like = apLikesBehaviorMapper.selectLastLike(burst, entry.getEntryId(), dto.getArticleId(),
                ApCollection.Type.ARTICLE.getCode());
        if (like != null && like.getType() == ApLikesBehavior.Operation.LIKE.getCode()){
            isLike = true;
        }

        //判断是否已经收藏
        ApCollection collection = apCollectionMapper.selectForEntryId(burst, entry.getEntryId(), dto.getArticleId(),
                ApCollection.Type.ARTICLE.getCode());
        if (collection != null){
            isCollection = true;
        }

        //判断是否已关注
        ApAuthor author = apAuthorMapper.selectById(dto.getAuthorId());
        if (user != null && author != null && author.getUserId() != null){
            ApUserFollow userFollow = apUserFollowMapper.selectByFollowId(BurstUtils.groudOne(user.getId()),
                    user.getId(), author.getUserId().intValue());
            if (userFollow != null){
                isFollow = true;
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("isfollow",isFollow);
        data.put("islike",isLike);
        data.put("isunlike",isUnLike);
        data.put("iscollection",isCollection);

        return ResponseResult.okResult(data);
    }
}
