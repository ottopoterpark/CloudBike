package com.CloudBike.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户端订单VO
 * @author unique
 */
@Data
public class OrderOverviewVO implements Serializable {

    /**
     * 订单id
     */
    private Integer id;
    /**
     * 单车编号
     */
    private String number;
    /**
     * 单车名称
     */
    private String name;
    /**
     * 图片路径集合
     */
    private List<String> images;
    /**
     * 业务类型（0：日租，1：月租，2：购买）
     */
    private Integer type;
    /**
     * 业务叠加数量
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
}
