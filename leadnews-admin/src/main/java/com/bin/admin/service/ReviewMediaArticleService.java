package com.bin.admin.service;

/**
 * @author huangbin
 */
public interface ReviewMediaArticleService {
    /**
     * 自动审核文章
     * @param newsId
     */
    void autoReviewMediaArticle(Integer newsId);

}
