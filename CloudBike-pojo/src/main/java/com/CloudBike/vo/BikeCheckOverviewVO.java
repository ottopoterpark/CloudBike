package com.CloudBike.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 后台单车管理（简单）VO
 * @author unique
 */
@Data
public class BikeCheckOverviewVO implements Serializable {
    /**
     * 单车id
     */
    private Integer id;
    /**
     * 单车编号
     */
    private String number;
    /**
     * 单车类型（0：公路车，1：旅行车，2：折叠车，3：死飞，4：山地车，5：其他）
     */
    private Integer type;
    /**
     * 状态（0：正常，1：待提车，2：租赁中，3：已售，4：待归还）
     */
    private Integer status;
    /**
     * 单车售价
     */
    private Integer price;
    /**
     * 车龄
     */
    private Integer age;
    /**
     * 车身大小
     */
    private Integer size;
    /**
     * 应归还时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnTime;
}
