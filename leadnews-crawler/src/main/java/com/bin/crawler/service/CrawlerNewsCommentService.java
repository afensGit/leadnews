package com.bin.crawler.service;

import com.bin.model.crawler.pojos.ClNewsComment;

/**
 * @author huangbin
 */
public interface CrawlerNewsCommentService {
    /**
     * 保存爬取的文章评论信息
     * @param clNewsComment
     */
    void saveClNewsComment(ClNewsComment clNewsComment);
}
