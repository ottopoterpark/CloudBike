package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 重置密码DTO
 * @author unique
 */
@Data
public class PasswordDto implements Serializable {
    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
}
