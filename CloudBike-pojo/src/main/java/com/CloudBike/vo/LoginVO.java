package com.CloudBike.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LoginVO implements Serializable {
    private Integer id;             // 主键
    private String username;        // 用户名
    private String name;            // 姓名
    private String token;           // token信息
}
