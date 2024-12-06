package com.CloudBike.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 骑行团概览VO
 */
@Data
public class RideOverviewVO implements Serializable {
    /**
     * 骑行团id
     */
    private Integer id;
    /**
     * 骑行团名称
     */
    private String name;
    /**
     * 骑行团发起用户
     */
    private String username;
    /**
     * 骑行团图片
     */
    private String image;
    /**
     * 骑行团出发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureTime;
    /**
     * 参与人数
     */
    private Integer participants;
}
