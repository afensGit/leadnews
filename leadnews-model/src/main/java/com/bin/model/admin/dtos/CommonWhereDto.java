package com.bin.model.admin.dtos;

import lombok.Data;

/**
 * @author huangbin
 */
@Data
public class CommonWhereDto {

    private String filed;
    private String type="eq";
    private String value;

}
