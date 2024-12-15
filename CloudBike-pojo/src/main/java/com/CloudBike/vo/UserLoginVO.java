package com.CloudBike.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信用户登录VO
 * @author unique
 */
@Builder
@Data
public class UserLoginVO implements Serializable {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 微信用户唯一标识
     */
    private String openid;
    /**
     * 用户jwt令牌
     */
    private String token;
}
