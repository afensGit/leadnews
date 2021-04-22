package com.bin.model.mess.app;

import lombok.Data;

/**
 * @author huangbin
 */
@Data
public class ArticleVisitStreamDto {
    private Integer articleId;
    private long view;
    private long collect;
    private long commont;
    private long like;
}