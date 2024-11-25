package com.CloudBike.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author unique
 * @since 2024-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业务类型（0：租赁，1：收购）
     */
    private Integer type;

    /**
     * 租赁类型（0：天，1：月，2：学期）
     */
    private Integer duration;

    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;

    /**
     * 实付款
     */
    private BigDecimal payment;

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

    /**
     * 订单状态更新时间
     */
    private LocalDateTime updateTime;


}
