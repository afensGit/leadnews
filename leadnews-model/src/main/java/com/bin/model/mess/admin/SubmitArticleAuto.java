package com.bin.model.mess.admin;

import lombok.Data;

@Data
public class SubmitArticleAuto {

    // 文章类型
    private ArticleType type;
    // 文章ID
    private Integer articleId;

    public enum ArticleType{
        /**
         * 自媒体文章类型
         */
        WEMEDIA,
        /**
         * 爬虫文章类型
         */
        CRAWLER;
    }

}
