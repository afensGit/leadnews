package com.bin.admin.service;

import com.bin.model.crawler.pojos.ClNews;

/**
 * @author huangbin
 */
public interface ReviewCrawlerArticleService {
    /**
     * 查询数据库获取需要审核的文章
     * @param clNews
     * @throws Exception
     */
    public void autoReviewArticleByCrawler(ClNews clNews) throws Exception;

    /**
     * 设置审核文章信息
     * @throws Exception
     */
    public void autoReviewArticleByCrawler() throws Exception;

    /**
     * 审核文章
     * @param clNewsId
     * @throws Exception
     */
    public void autoReviewArticleByCrawler(Integer clNewsId) throws Exception;
}