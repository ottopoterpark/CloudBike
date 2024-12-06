package com.CloudBike.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 骑行团后台审核详情VO
 */
@Data
public class RideCheckDetailVO implements Serializable {
    /**
     * 骑行团id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 联系方式
     */
    private String phone;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动简介
     */
    private String description;
    /**
     * 集合地点
     */
    private String meetingPoint;
    /**
     * 图片1
     */
    private String image1;
    /**
     * 图片2
     */
    private String image2;
    /**
     * 图片3
     */
    private String image3;
    /**
     * 审核状态
     */
    private Integer status;
    /**
     * 限制人数
     */
    private Integer maxPeople;
    /**
     * 出发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureTime;
}
