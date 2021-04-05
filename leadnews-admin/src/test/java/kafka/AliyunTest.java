package kafka;

import com.bin.admin.AdminJarApplication;
import com.bin.common.aliyun.AliyunTextScanRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = AdminJarApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {

    @Autowired
    private AliyunTextScanRequest aliyunTextScanRequest;

    @Test
    public void testTextScanRequest() throws Exception {
        String message = "阿里云，阿里巴巴集团旗下云计算品牌冰毒买卖，全球卓越的云计算技术和服务提供商。创立于2009年，在杭州、北京、硅谷等地设有研发中心和运营机构。";
        String response = aliyunTextScanRequest.textScanRequest(message);
        System.out.println(response);
    }
}
