package com.bin.article.controller.v1;

import com.bin.apis.article.controllerApi.ArticleHomeControllerApi;
import com.bin.article.service.ArticleHomeService;
import com.bin.common.article.contants.ArticleContants;
import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
        return articleHomeService.load(dto, ArticleContants.LOAD_MORE);
    }

    @Override
    @PostMapping("loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return articleHomeService.load(dto, ArticleContants.LOAD_MORE);
    }

    @Override
    @PostMapping("loadnew")
    public ResponseResult loadNews(@RequestBody ArticleHomeDto dto) {
        return articleHomeService.load(dto, ArticleContants.LOAD_NEW);
    }
}
