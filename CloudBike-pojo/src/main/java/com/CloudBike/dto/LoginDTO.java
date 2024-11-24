package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录DTO
 */
@Data
public class LoginDTO implements Serializable {
    private String username;        // 用户名
    private String password;        // 密码
}
