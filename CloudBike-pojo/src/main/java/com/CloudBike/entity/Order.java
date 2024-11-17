package com.CloudBike.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author unique
 * @since 2024-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order")
@ApiModel(value="Order对象", description="订单表")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @ApiModelProperty(value = "业务类型（0：租赁，1：收购）")
    private Integer orderType;

    @ApiModelProperty(value = "租赁类型（0：天，1：月，2：学期）")
    private Integer duration;

    @ApiModelProperty(value = "租金")
    private BigDecimal rent;

    @ApiModelProperty(value = "订单创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    @ApiModelProperty(value = "创建订单的用户")
    private Integer userId;

    @ApiModelProperty(value = "关联的自行车")
    private Integer bikeId;


}
