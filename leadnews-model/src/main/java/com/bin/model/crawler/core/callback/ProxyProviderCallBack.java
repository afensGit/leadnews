package com.bin.model.crawler.core.callback;

import com.bin.model.crawler.core.proxy.CrawlerProxy;
import java.util.List;

/**
 * IP池更新回调
 * @author huangbin
 */
public interface ProxyProviderCallBack {
    /**
     * 获取代理列表
     * @return
     */
    public List<CrawlerProxy> getProxyList();

    /**
     * 设置代理失效
     * @param crawlerProxy
     */
    public void invalid(CrawlerProxy crawlerProxy);
}