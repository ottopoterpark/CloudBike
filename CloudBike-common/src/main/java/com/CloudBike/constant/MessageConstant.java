package com.CloudBike.constant;

/**
 * 信息常量类
 */
public class MessageConstant {

    /**
     * 登录信息
     */
    public static final String NOT_LOGIN = "未登录";
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String ACCOUNT_LOCKED = "账号被冻结";
    public static final String LOGIN_FAILED = "登录失败";

    /**
     * 操作非法提示信息
     */
    public static final String AUTHORITY_TOO_LOW = "权限不足，请联系管理员";
    public static final String AUTHORITY_TOO_HIGN = "无法对管理员进行操作";
    public static final String CREDIT_LIMIT = "当前信用状态冻结，无法执行操作";
    public static final String DISCOUNT_ILLEGAL = "非法套餐";

    /**
     * 操作有误提示信息
     */
    public static final String DUPLICATE_USERNAME = "用户名重复";
    public static final String DUPLICATE_NUMBER = "单车编号重复";
    public static final String OLDPASSWORD_ERROR = "原始密码有误";
    public static final String UPLOAD_FAILED = "文件上传失败";
    public static final String BUSY_DAY = "当天已有其他活动";
    public static final String TOO_HOT = "当前活动太火爆，人数已满！";
    public static final String BUSINESS_EXISTS = "当前购物车已有该单车业务";
    public static final String BIKE_TOO_HOT = "来晚了，单车已被抢";

    /**
     * 空结果提示信息
     */
    public static final String EMPTY_RESULT = "无符合条件的结果";
}
