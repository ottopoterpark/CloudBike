package com.CloudBike.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 骑行记录概览VO
 */
@Data
public class RideRecordOverviewVO {

    /**
     * 骑行团id
     */
    private Integer id;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 集合地点
     */
    private String meetingPoint;
    /**
     * 出发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureTime;
    /**
     * 参与人数
     */
    private Integer participants;
    /**
     * 图片
     */
    private String image;
    /**
     * 申请状态（0：审核中，1：已通过，2：已驳回，3：已结束）
     */
    private Integer status;
}
