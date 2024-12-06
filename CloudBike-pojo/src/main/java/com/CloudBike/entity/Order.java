package com.CloudBike.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author unique
 * @since 2024-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`order`")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业务类型（0：日租，1：月租，2：购买）
     */
    private Integer type;

    /**
     * 业务叠加数量
     */
    private Integer count;

    /**
     * 订单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 提车时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pickTime;

    /**
     * 实付款
     */
    private Integer payment;

    /**
     * 创建订单的用户
     */
    private Integer userId;

    /**
     * 关联的自行车
     */
    private Integer bikeId;

    /**
     * 订单状态（0：待付款，1：待提车，2：租赁中，3：已完成，4：待归还，5：已取消）
     */
    private Integer status;


}
