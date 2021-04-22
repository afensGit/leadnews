package com.bin.admin.job;

import com.bin.admin.service.ReviewCrawlerArticleService;
import com.bin.common.quartz.AbstractJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huangbin
 */
@Component
@DisallowConcurrentExecution
@Slf4j
public class CrawlerReviewArticleJob extends AbstractJob {

    @Autowired
    private ReviewCrawlerArticleService reviewCrawlerArticleService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long currentTime = System.currentTimeMillis();
        log.info("开始执行自动审核定时任务");
        try {
            reviewCrawlerArticleService.autoReviewArticleByCrawler();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("自动审核定时任务执行完成,耗时：{}", System.currentTimeMillis() - currentTime);
    }

    @Override
    public String[] triggerCron() {
        return new String[]{"0 0/5 * * * ?"};
    }

    @Override
    public String descTrigger() {
        return "每天晚上23:30执行";
    }
}