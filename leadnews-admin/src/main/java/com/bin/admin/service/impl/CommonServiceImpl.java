package com.bin.admin.service.impl;

import com.bin.admin.dao.CommonDao;
import com.bin.admin.service.CommonService;
import com.bin.admin.service.impl.commfilter.BaseCommonFilter;
import com.bin.model.admin.dtos.CommonDto;
import com.bin.model.admin.pojos.AdUser;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.utils.threadlocal.AdminThreadLocalUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huangbin
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private ApplicationContext context;

    @Override
    public ResponseResult list(CommonDto dto) {
        if (!dto.getName().isList()){
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        String where = getWhere(dto);
        String tableName = dto.getName().name().toLowerCase();
        int count = 0;
        int start = (dto.getPage() - 1) * dto.getSize();
        if (start < 0){
            start = 0;
        }
        List<HashMap> list = null;
        if (StringUtils.isEmpty(where)){
            list = commonDao.list(tableName, start, dto.getSize());
            count = commonDao.listCount(tableName);
        }else {
            list = commonDao.listForWhere(tableName, where, start, dto.getSize());
            count = commonDao.listCountForWhere(tableName, where);
        }
        Map map = Maps.newHashMap();
        map.put("list", list);
        map.put("total", count);
        doFilter(dto,"list");
        return ResponseResult.okResult(map);
    }

    /**
     * 执行增强方法
     * @param dto
     * @param name
     */
    private void doFilter(CommonDto dto, String name) {
        try {
            BaseCommonFilter filter = getFilter(dto);
            if (filter != null){
                AdUser user = AdminThreadLocalUtils.getUser();
                if ("insert".equals(name)){
                    filter.doInsertAfter(user, dto);
                }else if ("update".equals(name)){
                    filter.doUpdateAfter(user,dto);
                }else if ("delete".equals(name)){
                    filter.doDeleteAfter(user, dto);
                }else if ("list".equals(name)){
                    filter.doListAfter(user, dto);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("方法增强出现异常！");
        }
    }

    /**
     * 获取增强bean对象
     * @param dto
     * @return
     */
    private BaseCommonFilter getFilter(CommonDto dto){
        String name = dto.getName().name();
        if (context.containsBean(name)){
            return context.getBean(name, BaseCommonFilter.class);
        }
        return null;
    }

    /**
     * 获取where的条件拼接字符串
     * @param dto
     * @return
     */
    private String getWhere(CommonDto dto) {
        StringBuffer where = new StringBuffer();
        if (!CollectionUtils.isEmpty(dto.getWhere())){
            dto.getWhere().stream().forEach(w -> {
                //字段和值不能为空，同时字段和值不能相同，防止凭借真条件进行sql注入
                if (!StringUtils.isEmpty(w.getFiled()) && !StringUtils.isEmpty(w.getValue()) && !w.getFiled().equalsIgnoreCase(w.getValue())){
                    String tempF = parseValue(w.getFiled());
                    String tempV = parseValue(w.getValue());
                    //字段不能有数字
                    if (!tempF.matches("\\d*") && !tempF.equalsIgnoreCase(tempV)){
                        if ("eq".equals(w.getType())){
                            where.append(" and ").append(tempF).append(" = ").append("\'").append(tempV).append("\'");
                        }else if ("like".equals(w.getType())){
                            where.append(" and ").append(tempF).append(" like ").append("\'").append(tempV).append("\'");
                        }else if ("between".equals(w.getType())){
                            String[] split = tempV.split(",");
                            where.append(" and ").append(tempF).append(" between ")
                                    .append(split[0]).append(" and ").append(split[1]);
                        }
                    }
                }
            });
        }
        return where.toString();
    }

    /**
     * 避免传递的参数带有单引号分号注释等被当做参数
     * @param filed
     * @return
     */
    private String parseValue(String filed) {
        if (StringUtils.isNotEmpty(filed)){
            return filed.replace(".*([';#%]+|(--)+).*", "");
        }
        return filed;
    }

    @Override
    public ResponseResult update(CommonDto dto) {
        String model = dto.getModel();
        String where = getWhere(dto);
        String tableName = dto.getName().name().toLowerCase();
        if ("add".equals(model)){
            if (StringUtils.isNotEmpty(where)){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"参数设置有误！");
            }else {
                return addData(dto, tableName);
            }
        }else {
            if (StringUtils.isEmpty(where)){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "缺少参数");
            }else {
                return updateData(tableName, dto, where);
            }
        }
    }

    private ResponseResult updateData(String tableName, CommonDto dto, String where) {
        String sets = getSets(dto);
        if (!dto.getName().isUpdate()){
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        if (StringUtils.isEmpty(sets)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"缺少参数");
        }
        int temp = commonDao.update(tableName, sets, where);
        if (temp > 0){
            doFilter(dto, "update");
        }
        return ResponseResult.okResult(temp);
    }

    private String getSets(CommonDto dto) {
        StringBuffer sets = new StringBuffer();
        AtomicInteger count = new AtomicInteger();
        if (!CollectionUtils.isEmpty(dto.getSets())){
            dto.getSets().stream().forEach(s -> {
                if (StringUtils.isEmpty(s.getValue())){
                    count.decrementAndGet();
                }else {
                    String filed = parseValue(s.getFiled());
                    String value = parseValue(s.getValue());
                    if (!filed.matches("\\d*") && !filed.equalsIgnoreCase(value)){
                        if (sets.length() > 0){
                            sets.append(",");
                        }
                        sets.append(filed).append("\'").append(value).append("\'");
                    }
                }
            });
        }
        if (count.get() > 0){
            return null;
        }
        return sets.toString();
    }

    private ResponseResult addData(CommonDto dto, String tableName) {
        String[] sql = getInsertSql(dto);
        if (!dto.getName().isAdd()){
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        if (sql == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"参数传递有误");
        }
        int temp = commonDao.insert(tableName, sql[0], sql[1]);
        if (temp > 0){
            doFilter(dto, "insert");
        }
        return ResponseResult.okResult(temp);
    }

    private String[] getInsertSql(CommonDto dto) {
        StringBuffer fileds = new StringBuffer();
        StringBuffer values = new StringBuffer();
        AtomicInteger count = new AtomicInteger();
        if (!CollectionUtils.isEmpty(dto.getSets())){
            dto.getSets().stream().forEach(w -> {
                if (StringUtils.isEmpty(w.getValue())){
                    count.decrementAndGet();
                }else {
                    String filed = parseValue(w.getFiled());
                    String value = parseValue(w.getValue());
                    if (!filed.matches("\\d*") && !filed.equalsIgnoreCase(value)){
                        if (fileds.length() > 0){
                            fileds.append(",");
                            values.append(",");
                        }
                        fileds.append(filed);
                        values.append("\'").append(value).append("\'");
                    }
                }
            });
        }
        if (count.get() > 0){
            return null;
        }
        return new String[]{fileds.toString(), values.toString()};
    }

    @Override
    public ResponseResult delete(CommonDto dto) {
        String where = getWhere(dto);
        String tableName = dto.getName().name().toLowerCase();
        if (!dto.getName().isDelete()){
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        if (StringUtils.isEmpty(where)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"缺少参数");
        }
        int temp = commonDao.delete(tableName, where);
        if (temp > 0){
            doFilter(dto, "delete");
        }
        return ResponseResult.okResult(temp);
    }
}
