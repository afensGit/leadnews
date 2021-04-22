package com.bin.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.bin.article.service.ApArticleSearchService;
import com.bin.article.utils.Trie;
import com.bin.model.article.dtos.UserSearchDto;
import com.bin.model.article.pojos.ApArticle;
import com.bin.model.article.pojos.ApAssociateWords;
import com.bin.model.article.pojos.ApHotWords;
import com.bin.model.behavior.pojos.ApBehaviorEntry;
import com.bin.model.common.constants.ESIndexConstants;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.common.enums.AppHttpCodeEnum;
import com.bin.model.mappers.app.*;
import com.bin.model.user.pojos.ApUser;
import com.bin.model.user.pojos.ApUserSearch;
import com.bin.utils.common.DateUtils;
import com.bin.utils.threadlocal.AppThreadLocalUtils;
import com.google.common.collect.Lists;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/25 16:50
 */

@Service
@Slf4j
public class ApArticleSearchServiceImpl implements ApArticleSearchService {

    @Autowired
    private ApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private ApUserSearchMapper apUserSearchMapper;

    @Autowired
    private ApHotWordsMapper apHotWordsMapper;

    @Autowired
    private ApAssociateWordsMapper apAssociateWordsMapper;

    @Autowired
    @Qualifier("jestClient")
    private JestClient jestClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public ResponseResult findUserSearchHistory(UserSearchDto dto) {
        ResponseResult result = this.getEntryId(dto);
        if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()){
            return result;
        }
        if (dto.getPageSize() > 50 && dto.getPageSize() < 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<ApUserSearch> userSearches = apUserSearchMapper.selectByEntryId((int) result.getData(), dto.getPageSize());
        return ResponseResult.okResult(userSearches);
    }

    @Override
    public ResponseResult deleteUserSearch(UserSearchDto dto) {
        if (dto.getHisList() == null || dto.getHisList().size() <= 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        ResponseResult result = this.getEntryId(dto);
        if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()){
            return result;
        }
        List<Integer> historyLists = dto.getHisList().stream().map(ApUserSearch::getId).collect(Collectors.toList());
        int count = apUserSearchMapper.deleteUserSearch((Integer) result.getData(), historyLists);
        return ResponseResult.okResult(count);
    }

    @Override
    public ResponseResult clearUserSearch(UserSearchDto dto) {
        ResponseResult result = getEntryId(dto);
        if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()){
            return result;
        }
        int count = apUserSearchMapper.clearUserSearch((Integer) result.getData());
        return ResponseResult.okResult(count);
    }

    @Override
    public ResponseResult hotKeyWords(UserSearchDto dto) {
        String date = dto.getHotDate();
        if (StringUtils.isEmpty(date)){
            date = DateUtils.getDateFormat();
        }
        List<ApHotWords> hotWords = apHotWordsMapper.queryHotWords(date);
        return ResponseResult.okResult(hotWords);
    }

    @Override
    public ResponseResult searchAssociate(UserSearchDto dto) {
        if (dto.getPageSize() > 50 || dto.getPageSize() < 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<ApAssociateWords> associateWords = apAssociateWordsMapper.selectByAssociateWords("%"+dto.getSearchWords()+"%", dto.getPageSize());
        return ResponseResult.okResult(associateWords);
    }

    @Override
    public ResponseResult esArticleSearch(UserSearchDto dto) {
        //只在第一页的时候保存用户搜索行为
        if (dto.getFromIndex() == 0){
            ResponseResult result = getEntryId(dto);
            if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()){
                return result;
            }
            saveUserSearch((int)result.getData(), dto.getSearchWords());
        }
        //构建查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title", dto.getSearchWords()));
        //设置分页
        searchSourceBuilder.from(dto.getFromIndex());
        searchSourceBuilder.size(dto.getPageSize());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(ESIndexConstants.ARTICLE_INDEX)
                .addType(ESIndexConstants.DEFAULT_DOC)
                .build();

        try {
            SearchResult searchResult = jestClient.execute(search);
            List<SearchResult.Hit<ApArticle, Void>> resultHits = searchResult.getHits(ApArticle.class);
            List<ApArticle> articleList = new ArrayList<>();
            for (SearchResult.Hit<ApArticle, Void> resultHit : resultHits) {
                ApArticle article = apArticleMapper.selectById(Long.valueOf(resultHit.id));
                if (article == null){
                    continue;
                }
                articleList.add(article);
            }
            return ResponseResult.okResult(articleList);
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("搜索出现异常");
            log.error("异常信息：[{}]", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    @Override
    public ResponseResult saveUserSearch(Integer entryId, String keyword) {
        //判断参数是否正确
        //查询记录是否已经存在
        int count = apUserSearchMapper.checkSearchRecord(entryId, keyword);
        if (count > 0){
            return ResponseResult.okResult(1);
        }
        ApUserSearch userSearch = new ApUserSearch();
        userSearch.setEntryId(entryId);
        userSearch.setKeyword(keyword);
        userSearch.setCreatedTime(new Date());
        userSearch.setStatus(1);
        return ResponseResult.okResult(apUserSearchMapper.insertUserSearch(userSearch));
    }

    private ResponseResult getEntryId(UserSearchDto dto){
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null && dto.getEquipmentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if (user != null){
            userId = user.getId();
        }
        ApBehaviorEntry entry = apBehaviorEntryMapper.selectByUserIdOrEquipment(userId, dto.getEquipmentId());
        if (entry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return ResponseResult.okResult(entry.getId());
    }

    @Override
    public ResponseResult searchAssociateV2(UserSearchDto dto) {
        if(dto.getPageSize()>50){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String associateStr = redisTemplate.opsForValue().get("associate_list");
        List<ApAssociateWords> aw = null;
        if(StringUtils.isNotEmpty(associateStr) && !"[]".equals(associateStr)){
            aw = JSON.parseArray(associateStr, ApAssociateWords.class);
        }else{
            aw = apAssociateWordsMapper.selectAllAssociateWords();
            redisTemplate.opsForValue().set("associate_list", JSON.toJSONString(aw));
        }
        //needed cache trie
        Trie t = new Trie();
        for (ApAssociateWords a : aw){
            t.insert(a.getAssociateWords());
        }
        List<String> ret = t.startWith(dto.getSearchWords());
        List<ApAssociateWords> wrapperList = Lists.newArrayList();
        for(String s : ret){
            ApAssociateWords apAssociateWords = new ApAssociateWords();
            apAssociateWords.setAssociateWords(s);
            wrapperList.add(apAssociateWords);
        }
        return ResponseResult.okResult(wrapperList);
    }
}
