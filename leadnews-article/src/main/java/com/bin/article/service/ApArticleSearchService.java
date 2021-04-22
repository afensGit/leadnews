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
    /**
     * 搜索历史查找
     * @param dto
     * @return
     */
    ResponseResult findUserSearchHistory(UserSearchDto dto);

    /**
     * 删除搜索历史
     * @param dto
     * @return
     */
    ResponseResult deleteUserSearch(UserSearchDto dto);

    /**
     * 清除搜索
     * @param dto
     * @return
     */
    ResponseResult clearUserSearch(UserSearchDto dto);

    /**
     * 热词搜索
     * @param dto
     * @return
     */
    ResponseResult hotKeyWords(UserSearchDto dto);

    /**
     * 联想词查找
     * @param dto
     * @return
     */
    ResponseResult searchAssociate(UserSearchDto dto);

    /**
     * 新闻查找
     * @param dto
     * @return
     */
    ResponseResult esArticleSearch(UserSearchDto dto);

    /**
     * 保存搜索
     * @param entryId
     * @param keyword
     * @return
     */
    ResponseResult saveUserSearch(Integer entryId, String keyword);

    /**
     * 联想词查找V2
     * @param userSearchDto
     * @return
     */
    ResponseResult searchAssociateV2(UserSearchDto userSearchDto);
}
