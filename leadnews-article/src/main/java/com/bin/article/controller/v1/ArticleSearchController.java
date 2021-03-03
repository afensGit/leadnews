package com.bin.article.controller.v1;

import com.bin.apis.article.controllerApi.ArticleSearchControllerApi;
import com.bin.article.service.ApArticleSearchService;
import com.bin.model.article.dtos.UserSearchDto;
import com.bin.model.common.dtos.ResponseResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 17:08
 */

@RestController
@RequestMapping("/api/v1/article/search")
public class ArticleSearchController implements ArticleSearchControllerApi {

    @Autowired
    private ApArticleSearchService apArticleSearchService;

    @Override
    @PostMapping("/load_search_history")
    public ResponseResult findUserSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.findUserSearchHistory(dto);
    }

    @Override
    @PostMapping("/del_search")
    public ResponseResult deleteUserSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.deleteUserSearch(dto);
    }

    @Override
    @PostMapping("/clear_search")
    public ResponseResult clearUserSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.clearUserSearch(dto);
    }

    @Override
    @PostMapping("/load_hot_keywords")
    public ResponseResult hotKeyWords(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.hotKeyWords(dto);
    }

    @Override
    @PostMapping("/associate_search")
    public ResponseResult searchAssociate(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.searchAssociate(dto);
    }

    @Override
    @PostMapping("/article_search")
    public ResponseResult esArticleSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.esArticleSearch(dto);
    }
}
