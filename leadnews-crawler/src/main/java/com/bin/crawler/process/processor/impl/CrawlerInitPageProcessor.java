package com.bin.crawler.process.processor.impl;

import com.bin.crawler.process.entity.CrawlerConfigProperty;
import com.bin.crawler.process.processor.AbstractCrawlerPageProcessor;
import com.bin.model.crawler.enums.CrawlerEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.List;

/**
 * 抓去初始的页面
 * @author huangbin
 */
@Component
@Slf4j
public class CrawlerInitPageProcessor extends AbstractCrawlerPageProcessor {

    @Autowired
    private CrawlerConfigProperty crawlerConfigProperty;

    /**
     * 处理数据
     *
     * @param page
     */
    @Override
    public void handelPage(Page page) {
        String initXpath = crawlerConfigProperty.getInitCrawlerXpath();
        //抓取帮助页面List
        List<String> helpUrlList = page.getHtml().xpath(initXpath).links().all();
        addSpiderRequest(helpUrlList, page.getRequest(), CrawlerEnum.DocumentType.HELP);
    }

    /**
     * 需要处理的爬取类型
     * 初始化只处理 正向爬取
     *
     * @param handelType
     * @return
     */
    @Override
    public boolean isNeedHandelType(String handelType) {
        return CrawlerEnum.HandelType.FORWARD.name().equals(handelType);
    }

    /**
     * 需要处理的文档类型
     * 只处理初始化的URK
     *
     * @param documentType
     * @return
     */
    @Override
    public boolean isNeedDocumentType(String documentType) {
        return CrawlerEnum.DocumentType.INIT.name().equals(documentType);
    }

    /**
     * 优先级
     *
     * @return
     */
    @Override
    public int getPriority() {
        return 100;
    }

}
