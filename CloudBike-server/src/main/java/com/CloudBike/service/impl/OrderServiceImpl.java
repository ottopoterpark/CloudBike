package com.CloudBike.service.impl;

import com.CloudBike.entity.Order;
import com.CloudBike.mapper.OrderMapper;
import com.CloudBike.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-18
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
