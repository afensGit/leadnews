package com.bin.article.controller.v1;

import com.bin.apis.article.controllerApi.AppArticleInfoControllerApi;
import com.bin.article.service.AppArticleInfoService;
import com.bin.model.article.dtos.ArticleInfoDto;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/22 23:16
 */
@RestController
@RequestMapping("/api/v1/article")
public class AppArticleInfoController implements AppArticleInfoControllerApi {

    @Autowired
    private AppArticleInfoService appArticleInfoService;

    @Override
    @PostMapping("/load_article_info")
    public ResponseResult loadArticleInfo(@RequestBody ArticleInfoDto dto) {
        return appArticleInfoService.loadArticleInfo(dto.getArticleId());
    }

    @Override
    @PostMapping("/load_article_behavior")
    public ResponseResult loadArticleBehavior(@RequestBody ArticleInfoDto dto) {
        return appArticleInfoService.loadArticleBehavior(dto);
    }
}
