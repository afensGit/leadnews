package com.bin.admin.dao;

import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author huangbin
 */

@Mapper
public interface CommonDao {
    /**
     * 分页查询
     * @param tableName
     * @param start
     * @param size
     * @return
     */
    @Select("select * from ${tableName} limit ${start}, ${size}")
    @ResultType(HashMap.class)
    List<HashMap> list(@Param("tableName") String tableName,@Param("start") int start,@Param("size") int size);

    /**
     * 查询总记录数
     * @param tableName
     * @return
     */
    @Select("select count(*) from ${tableName}")
    @ResultType(Integer.class)
    int listCount(@Param("tableName") String tableName);

    /**
     * 根据条件分页查询
     * @param tableName
     * @param where
     * @param start
     * @param size
     * @return
     */
    @Select("select * from ${tableName} where 1=1 ${where} limit ${start}, ${size}")
    @ResultType(HashMap.class)
    List<HashMap> listForWhere(@Param("tableName") String tableName, @Param("where") String where, @Param("start") int start,@Param("size") int size);

    /**
     * 根据条件查询总记录数
     * @param tableName
     * @param where
     * @return
     */
    @Select("select count(*) from ${tableName} where 1=1 ${where}")
    @ResultType(Integer.class)
    int listCountForWhere(@Param("tableName") String tableName, @Param("where") String where);

    /**
     * 更新
     * @param tableName
     * @param sets
     * @param where
     * @return
     */
    @Update("update from ${tableName} set ${sets} where 1=1 ${where}")
    @ResultType(Integer.class)
    int update(@Param("tableName") String tableName,@Param("sets") String sets,@Param("where") String where);

    /**
     * 插入
     * @param tableName
     * @param fileds
     * @param values
     * @return
     */
    @Insert("insert into ${tableName} (${fileds}) values (${values})")
    @ResultType(Integer.class)
    int insert(@Param("tableName") String tableName,@Param("fileds") String fileds,@Param("values") String values);

    /**
     * 删除
     * @param tableName
     * @param where
     * @return
     */
    @Delete("delete from ${tableName} where 1=1 ${where} limit 1")
    @ResultType(Integer.class)
    int delete(@Param("tableName") String tableName,@Param("where") String where);
}
