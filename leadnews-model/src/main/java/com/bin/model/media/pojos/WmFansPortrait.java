package com.bin.model.media.pojos;

import com.bin.model.annotation.IdEncrypt;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class WmFansPortrait {
    private Integer id;
    @IdEncrypt
    private Integer userId;
    private String name;
    private String value;
    @JsonIgnore
    private String burst;
    private Date createdTime;
}