package crawler;


import com.bin.crawler.CrawlerJarApplication;
import com.bin.crawler.service.CrawlerIpPoolService;
import com.bin.crawler.utils.SeleniumClient;
import com.bin.model.crawler.core.cookie.CrawlerHtml;
import com.bin.model.crawler.pojos.ClIpPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest(classes = CrawlerJarApplication.class)
@RunWith(SpringRunner.class)
public class SeleniumClientTest {

    @Autowired
    private SeleniumClient seleniumClient;

    @Autowired
    private CrawlerIpPoolService crawlerIpPoolService;

    @Test
    public void test(){
        CrawlerHtml crawlerHtml =  seleniumClient.getCrawlerHtml("http://www.baidu.com",null,null);
        System.out.println(crawlerHtml.getHtml());
    }
    @Test
    public void testSaveCrawlerIpPool(){
        ClIpPool clIpPool = new ClIpPool();
        clIpPool.setIp("2222.3333.444.5555");
        clIpPool.setPort(1111);
        clIpPool.setIsEnable(true);
        clIpPool.setCreatedTime(new Date());
        crawlerIpPoolService.saveCrawlerIpPool(clIpPool);
    }


}
