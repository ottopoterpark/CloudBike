package com.CloudBike.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 骑行团信息DTO
 */
@Data
public class RideInfoDTO implements Serializable {
    /**
     * 骑行团id
     */
    private Integer id;
    /**
     * 骑行团名称
     */
    private String name;
    /**
     * 发起者id
     */
    private Integer userId;
    /**
     * 发起者用户名
     */
    private String username;
    /**
     * 发起者电话
     */
    private String phone;
    /**
     * 骑行次数
     */
    private Integer rideTimes;
    /**
     * 出发时间（格式为 yyyy-MM-dd HH:mm:ss）
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
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 图片路径集合
     */
    List<String> images;
    /**
     * 状态（0：审核中，1：已通过，2：已驳回，3：已结束）
     */
    private Integer status;
    /**
     * 参加人数
     */
    private Integer participants;
    /**
     * 限制人数
     */
    private Integer maxPeople;
    /**
     * 审核意见
     */
    private String reason;
}
