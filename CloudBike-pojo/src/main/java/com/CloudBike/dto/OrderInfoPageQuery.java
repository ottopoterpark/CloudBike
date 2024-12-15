package com.CloudBike.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 订单分页查询DTO
 * @author unique
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoPageQuery extends PageQuery implements Serializable {

    /**
     * 订单状态（0：待付款，1：待提车，2：租赁中，3：已完成，4：待归还，5：已取消，6、全部）
     */
    private Integer category;
    /**
     * 订单编号
     */
    private String number;
    /**
     * 用户名
     */
    private String username;
    /**
     * 下单时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginDate;
    /**
     * 下单时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
