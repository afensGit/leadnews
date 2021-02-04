package com.bin.model.mess.admin;

import com.bin.model.article.pojos.ApArticleConfig;
import com.bin.model.article.pojos.ApArticleContent;
import com.bin.model.article.pojos.ApAuthor;
import lombok.Data;

@Data
public class AutoReviewClNewsSuccess {
    private ApArticleConfig apArticleConfig;
    private ApArticleContent apArticleContent;
    private ApAuthor apAuthor;

}
