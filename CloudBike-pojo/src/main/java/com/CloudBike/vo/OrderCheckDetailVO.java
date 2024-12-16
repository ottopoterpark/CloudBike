package com.CloudBike.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台订单管理（详细）VO
 * @author unique
 */
@Data
public class OrderCheckDetailVO implements Serializable {
    /**
     * 订单id
     */
    private Integer id;
    /**
     * 订单编号
     */
    private String number;
    /**
     * 业务类型（0：日租，1：月租，2：购买）
     */
    private Integer type;
    /**
     * 业务叠加类型
     */
    private Integer count;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 提车时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pickTime;
    /**
     * 共计
     */
    private Integer payment;
    /**
     * 订单状态（0：待付款，1：待提车，2：租赁中，3：已完成，4：待归还，5：已取消）
     */
    private Integer status;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户电话
     */
    private String phone;
    /**
     * 信用（0：冻结，1：正常）
     */
    private Integer credit;
    /**
     * 单车编号
     */
    private String bikeNumber;
    /**
     * 单车名称
     */
    private String name;
    /**
     * 图片路径集合
     */
    private List<String> images;
}
