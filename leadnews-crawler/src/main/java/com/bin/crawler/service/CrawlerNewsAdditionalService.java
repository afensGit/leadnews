package com.bin.crawler.service;

import com.bin.model.crawler.core.parse.ParseItem;
import com.bin.model.crawler.pojos.ClNewsAdditional;

import java.util.Date;
import java.util.List;

/**
 * @author huangbin
 */
public interface CrawlerNewsAdditionalService {
    /**
     * 保存
     * @param clNewsAdditional
     */
    void saveAdditional(ClNewsAdditional clNewsAdditional);

    /**
     * 更新
     * @param currentDate
     * @return
     */
    public List<ClNewsAdditional> queryListByNeedUpdate(Date currentDate);

    /**
     * 保存
     * @param clNewsAdditional
     * @return
     */
    List<ClNewsAdditional> queryList(ClNewsAdditional clNewsAdditional);

    /**
     * 检查是否存在
     * @param url
     * @return
     */
    public boolean checkExist(String url);

    /**
     * 根据url查询
     * @param url
     * @return
     */
    public ClNewsAdditional getAdditionalByUrl(String url);

    /**
     * 是否是已经存在的url
     * @param url
     * @return
     */
    public boolean isExistsUrl(String url);

    /**
     * 更新
     * @param clNewsAdditional
     */
    public void updateAdditional(ClNewsAdditional clNewsAdditional);

    /**
     * 转换数据为ParseItem
     * @param additionalList
     * @return
     */
    public List<ParseItem> toParseItem(List<ClNewsAdditional> additionalList);

    /**
     * 查询增量的统计数据
     * @param currentDate
     * @return
     */
    public List<ParseItem> queryIncrementParseItem(Date currentDate);
}
