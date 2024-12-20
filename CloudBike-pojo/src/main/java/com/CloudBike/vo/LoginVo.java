package com.CloudBike.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 后台登陆VO
 * @author unique
 */
@Data
@Builder
public class LoginVo implements Serializable {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * token信息
     */
    private String token;
}
