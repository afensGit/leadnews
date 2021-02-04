package com.bin.model.websocket.dtos;

import com.bin.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class WebSocketDto {
    // 设备ID
    @IdEncrypt
    Integer equipmentId;
    // 文章ID
    @IdEncrypt
    String token;
}
