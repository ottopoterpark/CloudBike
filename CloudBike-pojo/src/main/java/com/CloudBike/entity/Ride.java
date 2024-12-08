package com.CloudBike.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 骑行团表
 * </p>
 *
 * @author unique
 * @since 2024-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ride")
public class Ride implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发起者
     */
    private Integer userId;

    /**
     * 活动名称
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
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 申请状态（0：审核中，1：已通过，2：已驳回，3：已结束）
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
