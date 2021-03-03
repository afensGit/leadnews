package com.bin.article.service;

import com.bin.model.article.dtos.UserSearchDto;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.user.pojos.ApUserSearch;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 16:48
 */
public interface ApArticleSearchService {

    ResponseResult findUserSearchHistory(UserSearchDto dto);

    ResponseResult deleteUserSearch(UserSearchDto dto);

    ResponseResult clearUserSearch(UserSearchDto dto);

    ResponseResult hotKeyWords(UserSearchDto dto);

    ResponseResult searchAssociate(UserSearchDto dto);

    ResponseResult esArticleSearch(UserSearchDto dto);

    ResponseResult saveUserSearch(Integer entryId, String keyword);
}
