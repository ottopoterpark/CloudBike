package com.CloudBike.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author unique
 * @since 2024-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 虚拟货币
     */
    private Integer balance;

    /**
     * 微信用户唯一标识
     */
    private String openid;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 发起骑行次数
     */
    private Integer rideTimes;

    /**
     * 信用（0：冻结，1：正常）
     */
    private Integer credit;

    /**
     * 头像图片路径
     */
    private String image;


}
