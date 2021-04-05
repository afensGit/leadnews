package kafka;

import com.bin.admin.AdminJarApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = AdminJarApplication.class)
@RunWith(SpringRunner.class)
public class KafkaTest {

    @Autowired
    private KafkaTemplate<String, String> template;

    @Test
    public void test(){
        try{
            template.send("topic.test", "123key", "huangbin");
            System.out.println("===========消息发送了=========");
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
