package com.bin.model.behavior.dtos;

import com.bin.model.annotation.IdEncrypt;
import com.bin.model.article.pojos.ApArticle;
import lombok.Data;

import java.util.List;

@Data
public class ShowBehaviorDto {

    // 设备ID
    @IdEncrypt
    Integer equipmentId;
    List<ApArticle> articleIds;

}
