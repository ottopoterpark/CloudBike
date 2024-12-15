package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信用户登录DTO
 * @author unique
 */
@Data
public class UserLoginDTO implements Serializable {
    /**
     * 微信授权码
     */
    private String code;
}
