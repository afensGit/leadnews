package com.bin.crawler.process.original.impl;

import com.bin.crawler.config.CrawlerConfig;
import com.bin.crawler.process.entity.ProcessFlowData;
import com.bin.crawler.process.original.AbstractOriginalDataProcess;
import com.bin.model.crawler.core.parse.ParseItem;
import com.bin.model.crawler.core.parse.impl.CrawlerParseItem;
import com.bin.model.crawler.enums.CrawlerEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangbin
 */
@Component
@Slf4j
public class CsdnOriginalDataProcess extends AbstractOriginalDataProcess {

    @Autowired
    private CrawlerConfig crawlerConfig;

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public List<ParseItem> parseOriginalRequestData(ProcessFlowData processFlowData) {
        List<ParseItem> parseItemList = null;
        //从crawlerConfig获取初始化url列表
        List<String> initCrawlerUrlList = crawlerConfig.getInitCrawlerUrlList();
        if (!CollectionUtils.isEmpty(initCrawlerUrlList)){
            parseItemList = initCrawlerUrlList.stream().map(url -> {
                CrawlerParseItem parseItem = new CrawlerParseItem();
                url = url + "?rnd=" + System.currentTimeMillis();
                parseItem.setUrl(url);
                parseItem.setDocumentType(CrawlerEnum.DocumentType.INIT.name());
                parseItem.setHandelType(processFlowData.getHandelType().name());
                log.info("初始化url：[{}]", url);
                return parseItem;
            }).collect(Collectors.toList());
        }
        return parseItemList;
    }
}
