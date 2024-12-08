package com.CloudBike.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 运营团队表
 * </p>
 *
 * @author unique
 * @since 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("employee")
public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名（手机号码）
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 权限（0：普通，1：管理员）
     */
    private Integer authority;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账号状态（0：禁用，1：启用）
     */
    private Integer status;


}
