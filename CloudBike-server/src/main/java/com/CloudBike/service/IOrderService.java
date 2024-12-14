package com.CloudBike.service;

import com.CloudBike.entity.Order;
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
}
