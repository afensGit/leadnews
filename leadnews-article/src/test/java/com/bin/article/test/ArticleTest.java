package com.bin.article.test;

import com.bin.apis.article.controllerApi.ArticleHomeControllerApi;
import com.bin.article.ArticleJarApplication;
import com.bin.article.service.ArticleHomeService;
import com.bin.common.article.contants.ArticleContants;
import com.bin.model.common.dtos.ResponseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/6 10:05
 */
@SpringBootTest(classes = ArticleJarApplication.class)
@RunWith(SpringRunner.class)
public class ArticleTest {

    @Autowired
    private ArticleHomeService articleHomeService;

    @Test
    public void testArticle(){
        ResponseResult responseResult = articleHomeService.load(null, ArticleContants.LOAD_NEW);
        System.out.println(responseResult.getData());

    }


}
