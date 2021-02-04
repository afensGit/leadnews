package com.bin.model.crawler.core.callback;

import com.bin.model.crawler.core.proxy.CrawlerProxy;
import java.util.List;

/**
 * IP池更新回调
 */
public interface ProxyProviderCallBack {
    public List<CrawlerProxy> getProxyList();
}