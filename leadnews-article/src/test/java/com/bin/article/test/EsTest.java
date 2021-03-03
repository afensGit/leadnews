package com.bin.article.test;

import com.bin.common.common.pojo.EsIndexEntity;
import com.bin.model.article.dtos.ArticleHomeDto;
import com.bin.model.article.pojos.ApArticle;
import com.bin.model.article.pojos.ApArticleContent;
import com.bin.model.common.constants.ESIndexConstants;
import com.bin.model.crawler.core.parse.ZipUtils;
import com.bin.model.mappers.app.ApArticleContentMapper;
import com.bin.model.mappers.app.ApArticleMapper;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTest {

    @Autowired
    @Qualifier("jestClient")
    private JestClient jestClient;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Test
    public void testSave() throws IOException {

        ArticleHomeDto dto = new ArticleHomeDto();
        dto.setSize(50);
        dto.setTag("__all__");
        dto.setMinBehotTime(new Date());
        dto.setMaxBehotTime(new Date());
        Short type = 1;
        List<ApArticle> apArticles = apArticleMapper.loadArticleListByLocation(dto, type);
        System.out.println(apArticles.size());
        for (ApArticle apArticle : apArticles) {
            ApArticleContent apArticleContent = apArticleContentMapper.selectArticleById(apArticle.getId());

            EsIndexEntity esIndexEntity = new EsIndexEntity();
            esIndexEntity.setChannelId(new Long(apArticle.getChannelId()));
            esIndexEntity.setContent(ZipUtils.gunzip(apArticleContent.getContent()));
            esIndexEntity.setPublishTime(apArticle.getPublishTime());
            esIndexEntity.setStatus(new Long(1));
            esIndexEntity.setTag("article");
            esIndexEntity.setTitle(apArticle.getTitle());
            Index.Builder builder = new Index.Builder(esIndexEntity);
            builder.id(apArticle.getId().toString());
            builder.refresh(true);
            Index index = builder.index(ESIndexConstants.ARTICLE_INDEX).type(ESIndexConstants.DEFAULT_DOC).build();
            JestResult result = jestClient.execute(index);
            if (result != null && !result.isSucceeded()) {
                throw new RuntimeException(result.getErrorMessage() + "插入更新索引失败!");
            }
        }
    }
}