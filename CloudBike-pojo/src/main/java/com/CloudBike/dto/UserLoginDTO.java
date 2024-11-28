package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信用户登录DTO
 */
@Data
public class UserLoginDTO implements Serializable {
    /**
     * code
     */
    private Integer code;
}
