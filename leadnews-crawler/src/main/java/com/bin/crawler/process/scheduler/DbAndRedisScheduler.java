package com.bin.crawler.process.scheduler;

import com.bin.crawler.helper.CrawlerHelper;
import com.bin.crawler.process.ProcessFlow;
import com.bin.crawler.process.entity.ProcessFlowData;
import com.bin.crawler.service.CrawlerNewsAdditionalService;
import com.bin.model.crawler.enums.CrawlerEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.RedisScheduler;

/**
 * @author huangbin
 */

@Slf4j
public class DbAndRedisScheduler extends RedisScheduler implements ProcessFlow {

    @Autowired
    private CrawlerHelper crawlerHelper;

    @Autowired
    private CrawlerNewsAdditionalService crawlerNewsAdditionalService;

    public DbAndRedisScheduler(String host) {
        super(host);
    }

    public DbAndRedisScheduler(JedisPool pool) {
        super(pool);
    }

    /**
     * url排重
     * @param request
     * @param task
     * @return
     */
    @Override
    public boolean isDuplicate(Request request, Task task) {
        String handelType = crawlerHelper.getHandelType(request);
        boolean isExist = false;
        //正向统计才尽心排重
        if (CrawlerEnum.HandelType.FORWARD.name().equals(handelType)) {
            log.info("URL排重开始，URL:{},documentType:{}", request.getUrl(), handelType);
            isExist = super.isDuplicate(request, task);
            if (!isExist) {
                isExist = crawlerNewsAdditionalService.isExistsUrl(request.getUrl());
            }
            log.info("URL排重结束，URL:{}，handelType:{},isExist：{}", request.getUrl(), handelType, isExist);
        } else {
            log.info("反向抓取，不进行URL排重");
        }
        return isExist;
    }

    @Override
    public void handel(ProcessFlowData processFlowData) {

    }

    @Override
    public CrawlerEnum.ComponentType getComponentType() {
        return CrawlerEnum.ComponentType.SCHEDULER;
    }

    @Override
    public int getPriority() {
        return 123;
    }
}
