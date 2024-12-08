package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户个人信息DTO
 */
@Data
public class UserInfoDTO implements Serializable {
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
}
