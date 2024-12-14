package com.CloudBike.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建订单VO
 */
@Data
public class OrderSubmitVO implements Serializable {
    /**
     * 订单id
     */
    private Integer id;
}
