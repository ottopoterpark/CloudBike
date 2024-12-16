package com.CloudBike.context;

/**
 * 共享上下文用户类
 * @author unique
 */
public class BaseContext {

    /**
     * 用于存放客户端用户的主键
     */
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    /**
     * 存放用户主键
     * @param id
     */
    public static void setCurrentId(Integer id)
    {
        threadLocal.set(id);
    }

    /**
     * 获取用户主键
     * @return
     */
    public static Integer getCurrentId()
    {
        return threadLocal.get();
    }

    /**
     * 移除用户主键
     */
    public static void removeId()
    {
        threadLocal.remove();
    }
}
