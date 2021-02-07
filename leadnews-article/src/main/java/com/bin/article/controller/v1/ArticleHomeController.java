package com.bin.article.controller.v1;

import com.bin.apis.article.controllerApi.ArticleHomeControllerApi;
import com.bin.article.service.ArticleHomeService;
import com.bin.common.article.contants.ArticleContants;
import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/5 10:58
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController implements ArticleHomeControllerApi {

    @Autowired
    private ArticleHomeService articleHomeService;

    @Override
    @GetMapping("load")
    public ResponseResult load(ArticleHomeDto dto) {
        return articleHomeService.load(dto, ArticleContants.LOAD_MORE);
    }

    @Override
    @GetMapping("loadmore")
    public ResponseResult loadMore(ArticleHomeDto dto, Short type) {
        return articleHomeService.load(dto, ArticleContants.LOAD_MORE);
    }

    @Override
    @GetMapping("loadnews")
    public ResponseResult loadNews(ArticleHomeDto dto, Short type) {
        return articleHomeService.load(dto, ArticleContants.LOAD_NEW);
    }
}
