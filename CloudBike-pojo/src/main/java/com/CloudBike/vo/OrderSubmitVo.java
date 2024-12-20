package com.CloudBike.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建订单VO
 * @author unique
 */
@Data
public class OrderSubmitVo implements Serializable {
    /**
     * 订单id
     */
    private Integer id;
}
