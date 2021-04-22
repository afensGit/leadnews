package com.bin.crawler.process.entity;


import com.bin.model.crawler.core.parse.ParseItem;
import com.bin.model.crawler.enums.CrawlerEnum;

import java.util.List;

/**
 * 流程数据
 * @author huangbin
 */
public class ProcessFlowData {
    /**
     * 抓取对象列表
     */
    private List<ParseItem> parseItemList;

    /**
     * 处理类型
     */
    private CrawlerEnum.HandelType handelType = CrawlerEnum.HandelType.FORWARD;


    public List<ParseItem> getParseItemList() {
        return parseItemList;
    }

    public void setParseItemList(List<ParseItem> parseItemList) {
        this.parseItemList = parseItemList;
    }


    public CrawlerEnum.HandelType getHandelType() {
        return handelType;
    }

    public void setHandelType(CrawlerEnum.HandelType handelType) {
        this.handelType = handelType;
    }

}
