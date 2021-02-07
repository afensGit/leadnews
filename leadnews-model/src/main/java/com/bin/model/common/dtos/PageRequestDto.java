package com.bin.model.common.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 想输出日志时，如果不想每个类都写private static Logger logger = new LoggerFactory.getLogger(xxxx.class)
 * 可以使用@Slf4j注解
 */
@Data
@Slf4j
public class PageRequestDto {

    protected Integer size;
    protected Integer page;

    public void checkParam() {
        if (this.page == null || this.page < 0) {
            setPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            setSize(10);
        }
    }
}
