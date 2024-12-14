package com.CloudBike.constant;

import java.util.zip.Inflater;

/**
 * 状态常量类
 */
public class StatusConstant {

    /**
     * 用户账号状态为正常状态
     */
    public static final Integer ENABLED = 1;
    /**
     * 用户账号状态为冻结状态
     */
    public static final Integer DISABLED = 0;

    /**
     * 骑行团活动审核中
     */
    public static final Integer CHECKING = 0;
    /**
     * 骑行团活动已通过
     */
    public static final Integer PASSED = 1;
    /**
     * 骑行团活动已驳回
     */
    public static final Integer REJECTED = 2;

    /**
     * 骑行记录：全部
     */
    public static final Integer ALL = 0;
    /**
     * 骑行记录：未开始
     */
    public static final Integer PREPARED = 1;
    /**
     * 骑行记录：已结束
     */
    public static final Integer FINISHED = 2;

    /**
     * 订单状态：未付款
     */
    public static final Integer UNPAID = 0;
    /**
     * 单车或订单状态：租赁中
     */
    public static final Integer RENTING = 2;
    /**
     * 单车或订单状态：待归还
     */
    public static final Integer TO_RETURN = 4;
    /**
     * 订单状态：已取消
     */
    public static final Integer CANCEL = 5;

    /**
     * 单车状态：空闲
     */
    public static final Integer AVAILABLE = 0;
}
