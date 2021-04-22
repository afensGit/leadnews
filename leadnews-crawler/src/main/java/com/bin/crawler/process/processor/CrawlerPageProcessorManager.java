package com.bin.crawler.process.processor;

import com.bin.crawler.helper.CrawlerHelper;
import com.bin.crawler.process.ProcessFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

/**
 * 爬虫文档管理
 * @author huangbin
 */

@Component
public class CrawlerPageProcessorManager {

    @Autowired
    private CrawlerHelper crawlerHelper;
    @Resource
    private List<AbstractCrawlerPageProcessor> abstractCrawlerPageProcessorList;


    /**
     * 初始化注入的接口排序
     */
    @PostConstruct
    private void initProcessingFlow() {
        if (null != abstractCrawlerPageProcessorList && !abstractCrawlerPageProcessorList.isEmpty()) {
            abstractCrawlerPageProcessorList.sort((Comparator<ProcessFlow>) (p1, p2) -> {
                if (p1.getPriority() > p2.getPriority()) {
                    return 1;
                } else if (p1.getPriority() < p2.getPriority()) {
                    return -1;
                }
                return 0;
            });
        }
    }

    /**
     * 处理数据
     *
     * @param page
     */
    public void handel(Page page) {
        String handelType = crawlerHelper.getHandelType(page.getRequest());
        String documentType = crawlerHelper.getDocumentType(page.getRequest());
        for (AbstractCrawlerPageProcessor abstractCrawlerPageProcessor : abstractCrawlerPageProcessorList) {
            boolean isNeedHandelType = abstractCrawlerPageProcessor.isNeedHandelType(handelType);
            boolean isNeedDocumentType = abstractCrawlerPageProcessor.isNeedDocumentType(documentType);
            if (isNeedHandelType && isNeedDocumentType) {
                abstractCrawlerPageProcessor.handelPage(page);
            }
        }
    }

}
