package com.bin.crawler.service;

/**
 * @author huangbin
 */
public interface AdLabelService {

    /**
     * 从爬取的文章中解析获取具体的标签，去数据库查询
     * @param labels
     * @return
     */
    String getLabelIds(String labels);

    /**
     * 根据标签id查询频道
     * @param labels
     * @return
     */
    Integer getAdChannelByLabelIds(String labels);
}
