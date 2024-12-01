package com.CloudBike.constant;

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

}
