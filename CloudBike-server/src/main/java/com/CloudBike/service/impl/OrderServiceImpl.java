package com.CloudBike.service.impl;

import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.TypeConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.entity.Bike;
import com.CloudBike.entity.Order;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.OrderMapper;
import com.CloudBike.service.IOrderService;
import com.CloudBike.vo.OrderOverviewVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    /**
     * 查看我的订单
     * @param category
     * @return
     */
    @Override
    public List<OrderOverviewVO> list(Integer category)
    {
        // 1、获取用户id
        Integer userId = BaseContext.getCurrentId();

        // 2、根据用户id获取用户所有订单（以创建时间倒序）
        List<Order> orders = lambdaQuery()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreateTime)
                .list();

        // 2.1、如果订单为空，返回提示信息
        if (orders==null||orders.isEmpty())
            throw new BaseException(MessageConstant.EMPTY_RESULT);

        // 3、根据category筛选
        if (category!=TypeConstant.ALL)
            orders = orders.stream()
                    .filter(l -> l.getStatus() == category)
                    .toList();

        // 3.1、如果查询结果为空，返回提示信息
        if (orders.isEmpty())
            throw new BaseException(MessageConstant.EMPTY_RESULT);

        // 4、封装属性
        // 4.1、获取这些订单关联的单车
        List<Integer> bikeIds = orders.stream()
                .map(Order::getBikeId)
                .toList();
        Map<Integer, Bike> bikeMap = Db.lambdaQuery(Bike.class)
                .in(Bike::getId, bikeIds)
                .list()
                .stream()
                .collect(Collectors.toMap(Bike::getId, l -> l));

        // 4.2、补充属性
        List<OrderOverviewVO> orderOverviewVOS=new ArrayList<>();
        orders.stream()
                .forEach(l->{
                    OrderOverviewVO orderOverviewVO=new OrderOverviewVO();

                    // 4.2.1、属性拷贝
                    BeanUtils.copyProperties(l,orderOverviewVO);

                    // 4.2.2、单车属性补充
                    Bike bike = bikeMap.get(l.getBikeId());
                    orderOverviewVO.setNumber(bike.getNumber());
                    orderOverviewVO.setName(bike.getName());

                    // 4.2.3、图片路径字符串转集合
                    String image = bike.getImage();
                    List<String> images = new ArrayList<>();
                    if (image!=null&&!image.isEmpty())
                        images= Arrays.asList(image.split(","));
                    orderOverviewVO.setImages(images);

                    // 4.2.4、将VO存入VOS
                    orderOverviewVOS.add(orderOverviewVO);
                });

        // 5、返回数据
        return orderOverviewVOS;
    }

    /**
     * 根据订单id查询订单
     * @param id
     * @return
     */
    @Override
    public OrderOverviewVO one(Integer id)
    {
        // 1、根据id获取订单
        Order order = getById(id);

        // 2、获取订单关联的单车
        Integer bikeId = order.getBikeId();
        Bike bike = Db.getById(bikeId, Bike.class);

        // 3、封装属性
        OrderOverviewVO orderOverviewVO=new OrderOverviewVO();

        // 3.1、属性拷贝
        BeanUtils.copyProperties(order,orderOverviewVO);

        // 3.2、单车属性补充
        orderOverviewVO.setNumber(bike.getNumber());
        orderOverviewVO.setName(bike.getName());

        // 3.3、将图片字符串转集合
        String image = bike.getImage();
        List<String> images = new ArrayList<>();
        if (image!=null&&!image.isEmpty())
            images= Arrays.asList(image.split(","));
        orderOverviewVO.setImages(images);

        // 4、返回结果
        return orderOverviewVO;
    }
}
