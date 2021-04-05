package kafka;


import com.bin.admin.AdminJarApplication;
import com.bin.admin.service.ReviewMediaArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = AdminJarApplication.class)
@RunWith(SpringRunner.class)
public class ReviewArticleTest {

    @Autowired
    private ReviewMediaArticleService reviewMediaArticleService;

    @Test
    public void testReview(){
        reviewMediaArticleService.autoReviewMediaArticle(5100);
    }


}
