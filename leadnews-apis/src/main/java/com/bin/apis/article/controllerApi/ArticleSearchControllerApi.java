package com.bin.apis.article.controllerApi;

import com.bin.model.article.dtos.UserSearchDto;
import com.bin.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 17:07
 */
public interface ArticleSearchControllerApi {
    /**
     * 查找搜索历史记录
     * @param dto
     * @return
     */
    ResponseResult findUserSearch(UserSearchDto dto);

    /**
     * 删除搜索历史记录
     * @param dto
     * @return
     */
    ResponseResult deleteUserSearch(UserSearchDto dto);

    /**
     * 清空搜索历史记录
     * @param dto
     * @return
     */
    ResponseResult clearUserSearch(UserSearchDto dto);

    /**
     * 查找今日热词
     * @param dto
     * @return
     */
    ResponseResult hotKeyWords(UserSearchDto dto);

    /**
     * 查找搜索联想词
     * @param dto
     * @return
     */
    ResponseResult searchAssociate(UserSearchDto dto);

    /**
     * 文章分页查询
     * @param dto
     * @return
     */
    ResponseResult esArticleSearch(UserSearchDto dto);
}
