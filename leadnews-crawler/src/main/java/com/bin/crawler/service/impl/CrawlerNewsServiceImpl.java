package com.bin.crawler.service.impl;

import com.bin.crawler.service.CrawlerNewsService;
import com.bin.model.crawler.pojos.ClNews;
import com.bin.model.mappers.crawlers.ClNewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huangbin
 */
@Service
public class CrawlerNewsServiceImpl implements CrawlerNewsService {

    @Autowired
    private ClNewsMapper clNewsMapper;

    @Override
    public void saveNews(ClNews clNews) {
        clNewsMapper.insertSelective(clNews);
    }
    @Override
    public void deleteByUrl(String url) {
        clNewsMapper.deleteByUrl(url);
    }
    @Override
    public List<ClNews> queryList(ClNews clNews) {
        return clNewsMapper.selectList(clNews);
    }
    @Override
    public void updateNews(ClNews clNews) {
        clNewsMapper.updateByPrimaryKey(clNews);
    }
}
