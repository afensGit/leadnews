package com.bin.crawler.config;

import com.bin.crawler.helper.CookieHelper;
import com.bin.crawler.helper.CrawlerHelper;
import com.bin.crawler.process.entity.CrawlerConfigProperty;
import com.bin.crawler.process.scheduler.DbAndRedisScheduler;
import com.bin.crawler.service.CrawlerIpPoolService;
import com.bin.crawler.utils.SeleniumClient;
import com.bin.model.crawler.core.callback.DataValidateCallBack;
import com.bin.model.crawler.core.callback.ProxyProviderCallBack;
import com.bin.model.crawler.core.parse.ParseRule;
import com.bin.model.crawler.core.proxy.CrawlerProxy;
import com.bin.model.crawler.core.proxy.CrawlerProxyProvider;
import com.bin.model.crawler.enums.CrawlerEnum;
import com.bin.model.crawler.pojos.ClIpPool;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangbin
 */
@Configuration
@Setter
@Getter
@PropertySource("classpath:crawler.properties")
@ConfigurationProperties("crawler.init.url")
public class CrawlerConfig {

    private String prefix;
    private String suffix;

    @Value("${crux.cookie.name}")
    private String CRUX_COOKIE_NAME;

    @Value("${proxy.isUsedProxyIp}")
    private Boolean isUsedProxyIp;

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.timeout}")
    private int redisTimeout;

    @Value("${redis.password}")
    private String redisPassword;

    @Value("${crawler.help.nextPagingSize}")
    private Integer nextPagingSize;

    private String initCrawlerXpath="//ul[@class='feedlist_mod']/li[@class='clearfix']/div[@class='list_con']/dl[@class='list_userbar']/dd[@class='name']/a";

    private String helpCrawlerXpath="//div[@class='article-list']/div[@class='article-item-box']/h4/a";

    private Spider spider;

    @Autowired
    private CrawlerIpPoolService crawlerIpPoolService;

    public List<String> getInitCrawlerUrlList(){
        List<String> initCrawlerUrlList = null;
        if (StringUtils.isNotEmpty(suffix)){
            String[] initUrlArray = suffix.split(",");
            if (initUrlArray != null && initUrlArray.length > 0){
                for (int i = 0; i < initUrlArray.length; i++) {
                    String initUrl = initUrlArray[i];
                    if (StringUtils.isNotEmpty(initUrl)){
                        if (!initUrl.toLowerCase().startsWith("http")){
                            initUrl = prefix + initUrl;
                            initUrlArray[i] = initUrl;
                        }
                    }
                }
            }
            initCrawlerUrlList = Arrays.asList(initUrlArray).stream()
                    .filter(url -> StringUtils.isNotEmpty(url)).collect(Collectors.toList());
        }
        return initCrawlerUrlList;
    }


    @Bean
    public SeleniumClient getSeleniumClient() {
        return new SeleniumClient();
    }

    /**
     * 设置cookie的辅助类
     * @return
     */
    @Bean
    public CookieHelper getCookieHelper(){
        return new CookieHelper(CRUX_COOKIE_NAME);
    }

    /**
     * CrawlerHelper辅助类
     * @return
     */
    @Bean
    public CrawlerHelper getCrawlerHelper(){
        CookieHelper cookieHelper = getCookieHelper();
        DataValidateCallBack dataValidateCallBack = getDataValidateCallBack(cookieHelper);
        CrawlerHelper crawlerHelper = new CrawlerHelper();
        crawlerHelper.setDataValidateCallBack(dataValidateCallBack);
        return crawlerHelper;
    }

    @Bean
    public CrawlerProxyProvider getCrawlerProxyProvider(){
        CrawlerProxyProvider proxyProvider = new CrawlerProxyProvider();
        proxyProvider.setUsedProxyIp(isUsedProxyIp);
        proxyProvider.setProxyProviderCallBack(new ProxyProviderCallBack() {
            @Override
            public List<CrawlerProxy> getProxyList() {
                return getCrawlerPoxyList();
            }

            @Override
            public void invalid(CrawlerProxy crawlerProxy) {
                invalidation(crawlerProxy);
            }
        });
        return proxyProvider;
    }

    /**
     * 获取初始化的ip代理
     * @return
     */
    public List<CrawlerProxy> getCrawlerPoxyList(){
        List<CrawlerProxy> crawlerProxyList = new ArrayList<>();
        ClIpPool clIpPool = new ClIpPool();
        clIpPool.setDuration(5);
        List<ClIpPool> clIpPools = crawlerIpPoolService.queryList(clIpPool);
        if (!CollectionUtils.isEmpty(clIpPools)){
            for (ClIpPool ipPool : clIpPools) {
                crawlerProxyList.add(new CrawlerProxy(ipPool.getIp(),ipPool.getPort()));
            }
        }
        return crawlerProxyList;
    }

    public void invalidation(CrawlerProxy crawlerProxy){
        crawlerIpPoolService.unvailableProxy(crawlerProxy, "自动禁用");
    }

    @Bean
    public CrawlerConfigProperty getCrawlerConfigProperty(){
        CrawlerConfigProperty configProperty = new CrawlerConfigProperty();
        //初始化url列表
        configProperty.setInitCrawlerUrlList(getInitCrawlerUrlList());
        //初始化url解析规则定义
        configProperty.setHelpCrawlerXpath(helpCrawlerXpath);
        //用户空间下的解析规则
        configProperty.setInitCrawlerXpath(initCrawlerXpath);
        //抓去用户空间的页大小
        configProperty.setCrawlerHelpNextPagingSize(nextPagingSize);
        //目标页的解析规则
        configProperty.setTargetParseRuleList(getTargetParseRuleList());
        return configProperty;
    }

    /**
     * 目标页的解析规则
     * @return
     */
    private List<ParseRule> getTargetParseRuleList() {
        List<ParseRule> parseRules = new ArrayList<ParseRule>(){{
            //标题
            add(new ParseRule("title", CrawlerEnum.ParseRuleType.XPATH,"//h1[@class='title-article']/text()"));
            //作者
            add(new ParseRule("author",CrawlerEnum.ParseRuleType.XPATH,"//a[@class='follow-nickName']/text()"));
            //发布日期
            add(new ParseRule("releaseDate",CrawlerEnum.ParseRuleType.XPATH,"//span[@class='time']/text()"));
            //标签
            add(new ParseRule("labels",CrawlerEnum.ParseRuleType.XPATH,"//span[@class='tags-box']/a/text()"));
            //个人空间
            add(new ParseRule("personalSpace",CrawlerEnum.ParseRuleType.XPATH,"//a[@class='follow-nickName']/@href"));
            //阅读量
            add(new ParseRule("readCount",CrawlerEnum.ParseRuleType.XPATH,"//span[@class='read-count']/text()"));
            //点赞量
            add(new ParseRule("likes",CrawlerEnum.ParseRuleType.XPATH,"//div[@class='tool-box']/ul[@class='meau-list']/li[@class='btn-like-box']/button/p/text()"));
            //回复次数
            add(new ParseRule("commentCount",CrawlerEnum.ParseRuleType.XPATH,"//div[@class='tool-box']/ul[@class='meau-list']/li[@class='to-commentBox']/a/p/text()"));
            //文章内容
            add(new ParseRule("content",CrawlerEnum.ParseRuleType.XPATH,"//div[@id='content_views']/html()"));
        }};
        return parseRules;
    }

    /**
     * 数据校验匿名内部类
     * @param cookieHelper
     * @return
     */
    private DataValidateCallBack getDataValidateCallBack(CookieHelper cookieHelper){
        return content -> {
            boolean flag;
            if (StringUtils.isEmpty(content)){
                flag = false;
            }else {
                boolean isContains_acw_sc__v2 = content.contains(CRUX_COOKIE_NAME);
                boolean isContains_location_reload = content.contains("document.location.reload()");
                flag = isContains_acw_sc__v2 && isContains_location_reload;
            }
            return flag;
        };
    }


    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
    }

    @Bean
    public DbAndRedisScheduler getDbAndRedisScheduler() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        JedisPool jedisPool = new JedisPool(genericObjectPoolConfig, redisHost, redisPort, redisTimeout, null, 0);
        return new DbAndRedisScheduler(jedisPool);
    }

}
