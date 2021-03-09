package com.bin.media.controller.v1;

import com.bin.apis.media.controllerApi.NewsControllerApi;
import com.bin.media.constans.WmMediaConstans;
import com.bin.media.service.NewsService;
import com.bin.model.common.dtos.ResponseResult;
import com.bin.model.media.dtos.WmNewsDto;
import com.bin.model.media.dtos.WmNewsPageReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/3/8 17:43
 */

@RestController
@RequestMapping("/api/v1/media/news")
public class NewsController implements NewsControllerApi {

    @Autowired
    private NewsService newsService;

    @Override
    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody WmNewsDto dto) {
        return newsService.saveNews(dto, WmMediaConstans.WM_NEWS_SUMMIT_STATUS);
    }

    @Override
    @PostMapping("/save_draft")
    public ResponseResult saveDraftNews(@RequestBody WmNewsDto dto) {
        return newsService.saveNews(dto, WmMediaConstans.WM_NEWS_DRAFT_STATUS);
    }

    @Override
    @PostMapping("/list")
    public ResponseResult getNewsList(@RequestBody WmNewsPageReqDto dto) {
        return newsService.selectNewsList(dto);
    }

    @Override
    @PostMapping("/news")
    public ResponseResult getNewsById(@RequestBody WmNewsDto dto) {
        return newsService.selectNewsById(dto);
    }

    @Override
    @PostMapping("del_news")
    public ResponseResult deleteNewsById(@RequestBody WmNewsDto dto) {
        return newsService.deleteNewsById(dto);
    }
}
