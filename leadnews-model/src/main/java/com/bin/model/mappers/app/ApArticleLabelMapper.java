package com.bin.model.mappers.app;

import com.bin.model.article.pojos.ApArticleLabel;

import java.util.List;

/**
 * @author huangbin
 */
public interface ApArticleLabelMapper {
    /**
     * 插入
     * @param record
     * @return
     */
    int insert(ApArticleLabel record);

    /**
     * 判断条件插入
     * @param record
     * @return
     */
    int insertSelective(ApArticleLabel record);

    /**
     * 判断条件插入
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ApArticleLabel record);

    /**
     * 查询多个
     * @param apArticleLabel
     * @return
     */
    List<ApArticleLabel> selectList(ApArticleLabel apArticleLabel);
}
