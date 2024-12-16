package com.CloudBike.service;

import com.CloudBike.dto.OrderInfoPageQuery;
import com.CloudBike.entity.Order;
import com.CloudBike.result.PageResult;
import com.CloudBike.vo.OrderCheckDetailVO;
import com.CloudBike.vo.OrderOverviewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
public interface IOrderService extends IService<Order> {

    /**
     * 查看我的订单
     * @param category
     * @return
     */
    List<OrderOverviewVO> list(Integer category);

    /**
     * 根据订单id查看你订单
     * @param id
     * @return
     */
    OrderOverviewVO one(Integer id);

    /**
     * 订单支付
     * @param id
     */
    void pay(Integer id);

    /**
     * 根据订单ids批量删除订单
     * @param ids
     */
    void removeBatch(List<Integer> ids);

    /**
     * 取消订单
     * @param id
     */
    void cancel(Integer id);

    /**
     * 订单分页查询
     * @param pageQuery
     * @return
     */
    PageResult page(OrderInfoPageQuery pageQuery);

    /**
     * 根据订单id查看订单详情
     * @param id
     * @return
     */
    OrderCheckDetailVO checkOne(Integer id);

    /**
     * 提车
     * @param id
     */
    void update(Integer id);
}
