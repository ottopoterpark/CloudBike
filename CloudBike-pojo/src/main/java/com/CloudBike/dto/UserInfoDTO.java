package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户个人信息DTO
 * @author unique
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
    /**
     * 头像图片路径
     */
    private String image;

}
