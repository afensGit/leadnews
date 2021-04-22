package com.bin.article.controller.v2;

import com.bin.apis.article.controllerApi.ArticleHomeControllerApi;
import com.bin.article.service.ArticleHomeService;
import com.bin.common.article.contants.ArticleContants;
import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 */
@RestController
@RequestMapping("/api/v2/article")
public class ArticleHomeV2Controller implements ArticleHomeControllerApi {
    @Autowired
    private ArticleHomeService articleHomeService;

    @Override
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
        return articleHomeService.loadV2( ArticleContants.LOAD_MORE, dto, true);
    }

    @Override
    @PostMapping("/loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return articleHomeService.loadV2( ArticleContants.LOAD_MORE, dto, false);
    }

    @Override
    @PostMapping("/loadnew")
    public ResponseResult loadNews(@RequestBody ArticleHomeDto dto) {
        return articleHomeService.loadV2( ArticleContants.LOAD_NEW, dto, false);
    }
}
