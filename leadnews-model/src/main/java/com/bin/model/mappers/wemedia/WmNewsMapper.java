package com.bin.model.mappers.wemedia;

import com.bin.model.media.dtos.WmNewsDto;
import com.bin.model.media.dtos.WmNewsPageReqDto;
import com.bin.model.media.pojos.WmNews;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 10:20
 */

@Mapper
public interface WmNewsMapper {

    /**
     * 保存文章信息
     * @param wmNews
     * @return
     */
    int insertNewsForEdit(WmNews wmNews);

    /**
     * 更新文章信息
     * @param wmNews
     * @return
     */
    int updateNewsByPrimaryKey(WmNews wmNews);

    /**
     * 查询文章列表
     * @param dto
     * @param userId
     * @return
     */
    List<WmNews> selectBySelective(@Param("dto") WmNewsPageReqDto dto, @Param("userId") Long userId);

    /**
     * 查询文章总数
     * @param dto
     * @param userId
     * @return
     */
    int selectNewsCount(@Param("dto") WmNewsPageReqDto dto, @Param("userId") Long userId);

    /**
     * 根据文章id查询文章
     * @param id
     * @return
     */
    WmNews selectNewsById(Integer id);

    /**
     * 根据文章id删除文章
     * @param id
     * @return
     */
    int deleteNewsById(Integer id);
}
