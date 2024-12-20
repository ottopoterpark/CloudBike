package com.CloudBike.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台订单管理（简单）VO
 * @author unique
 */
@Data
public class OrderCheckOverviewVo {

    /**
     * 订单id
     */
    private Integer id;
    /**
     * 订单编号
     */
    private String number;
    /**
     * 订单状态（0：待付款，1：待提车，2：租赁中，3：已完成，4：待归还，5：已取消）
     */
    private Integer status;
    /**
     * 用户名
     */
    private String username;
    /**
     * 单车编号
     */
    private String bikeNumber;
    /**
     * 业务类型（0：日租，1：月租，2：购买）
     */
    private Integer type;
    /**
     * 共计
     */
    private Integer payment;
    /**
     * 订单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
