package com.bin.crawler.service;

import com.bin.model.crawler.pojos.ClNews;

import java.util.List;

/**
 * @author huangbin
 */
public interface CrawlerNewsService {
    /**
     * 保存文章
     * @param clNews
     */
    public void saveNews(ClNews clNews);

    /**
     * 更新文章
     * @param clNews
     */
    public void updateNews(ClNews clNews);

    /**
     * 根据url删除文章
     * @param url
     */
    public void deleteByUrl(String url);

    /**
     * 查询文章
     * @param clNews
     * @return
     */
    public List<ClNews> queryList(ClNews clNews);
}
