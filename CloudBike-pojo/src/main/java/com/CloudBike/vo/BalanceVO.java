package com.CloudBike.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 余额VO
 */
@Data
public class BalanceVO implements Serializable {

    /**
     * 余额
     */
    private Integer balance;
}
