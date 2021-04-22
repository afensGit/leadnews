package com.bin.crawler.service.impl;

import com.bin.crawler.service.CrawlerNewsCommentService;
import com.bin.model.crawler.pojos.ClNewsComment;
import com.bin.model.mappers.crawlers.ClNewsCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huangbin
 */
@Service
public class CrawlerNewsCommentServiceImpl implements CrawlerNewsCommentService {

    @Autowired
    private ClNewsCommentMapper clNewsCommentMapper;

    @Override
    public void saveClNewsComment(ClNewsComment clNewsComment) {
        clNewsCommentMapper.insertSelective(clNewsComment);
    }
}
