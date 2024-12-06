package com.CloudBike.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 骑行团详情VO
 */
@Data
public class RideDetailVO implements Serializable {
    /**
     * 骑行团id
     */
    private Integer id;
    /**
     * 发起者用户名
     */
    private String username;
    /**
     * 发起次数
     */
    private Integer rideTimes;
    /**
     * 骑行团名称
     */
    private String name;
    /**
     * 出发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureTime;
    /**
     * 集合地点
     */
    private String meetingPoint;
    /**
     * 活动描述
     */
    private String description;
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
     * 参与人数
     */
    private Integer participants;
}
