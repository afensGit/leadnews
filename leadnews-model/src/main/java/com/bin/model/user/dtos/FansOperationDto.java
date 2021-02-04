package com.bin.model.user.dtos;

import com.bin.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class FansOperationDto {
    @IdEncrypt
    private Integer fansId;

    /**
     * true 开   false 关
     */
    private Boolean switchState;
}
