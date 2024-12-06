package com.CloudBike.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 骑行团后台审核概览VO
 */
@Data
public class RideCheckOverviewVO implements Serializable {
    /**
     * 骑行团id
     */
    private Integer id;
    /**
     * 发起者用户名
     */
    private String username;
    /**
     * 发起者电话
     */
    private String phone;
    /**
     * 骑行团名称
     */
    private String name;
    /**
     * 审核状态
     */
    private Integer status;
    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 出发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureTime;
}
