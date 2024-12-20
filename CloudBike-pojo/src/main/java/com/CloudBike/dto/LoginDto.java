package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录DTO
 * @author unique
 */
@Data
public class LoginDto implements Serializable {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
