package com.bin.model.media.dtos;

import com.bin.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class CommentReplytDto {

    @IdEncrypt
    private Integer commentId;
    private String content;

}
