package com.CloudBike.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 单车分页查询DTO
 * @author unique
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BikeInfoPageQuery extends PageQuery implements Serializable {
    /**
     * 单车编号
     */
    private String number;
    /**
     * 单车类型（0：公路车，1：旅行车，2：折叠车，3：死飞，4：山地车，5：其他）
     */
    private Integer type;
    /**
     * 单车状态（0：正常，1：租赁中，2：已售，3：待归还）
     */
    private Integer status;
}
