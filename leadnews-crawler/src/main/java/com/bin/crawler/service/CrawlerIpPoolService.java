package com.bin.crawler.service;

import com.bin.model.crawler.core.proxy.CrawlerProxy;
import com.bin.model.crawler.pojos.ClIpPool;

import java.util.List;

/**
 * @author huangbin
 */
public interface CrawlerIpPoolService {

    /**
     * 保存方法
     *
     * @param clIpPool
     */
    void saveCrawlerIpPool(ClIpPool clIpPool);

    /**
     * 检查代理Ip 是否存在
     *
     * @param host
     * @param port
     * @return
     */
    boolean checkExist(String host, int port);

    /**
     * 更新方法
     *
     * @param clIpPool
     */
    void updateCrawlerIpPool(ClIpPool clIpPool);

    /**
     * 查询所有数据
     * @param clIpPool
     * @return
     */
    List<ClIpPool> queryList(ClIpPool clIpPool);

    /**
     * 获取可用的列表
     * @param clIpPool
     * @return
     */
    List<ClIpPool> queryAvailableList(ClIpPool clIpPool);

    /**
     * 删除
     * @param clIpPool
     */
    void delete(ClIpPool clIpPool);

    /**
     * 设置某个ip不可用
     * @param proxy
     * @param errorMsg
     */
    void unvailableProxy(CrawlerProxy proxy, String errorMsg);
}
