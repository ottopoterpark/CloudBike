package com.CloudBike.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@Builder
public class User implements Serializable {

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
    private BigDecimal balance;

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


}
