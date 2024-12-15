package com.CloudBike.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车信息VO
 * @author unique
 */
@Data
public class CartInfoVO implements Serializable {
    /**
     * 购物车id
     */
    private Integer id;
    /**
     * 业务类型（0：日租，1：月租，2：购买）
     */
    private Integer type;
    /**
     * 业务叠加数量
     */
    private Integer count;
    /**
     * 合计
     */
    private Integer payment;
    /**
     * 单车编号
     */
    private String number;
    /**
     * 单车名称
     */
    private String name;
    /**
     * 图片路径集合
     */
    private List<String> images;
}
