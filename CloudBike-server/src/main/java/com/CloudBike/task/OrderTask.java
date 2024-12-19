package com.CloudBike.task;

import com.CloudBike.constant.BusinessConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.entity.Bike;
import com.CloudBike.entity.User;
import com.CloudBike.service.IOrderService;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.CloudBike.entity.Order;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单定时任务类
 * @author unique
 */
//@Component
@Slf4j
public class OrderTask {

    /**
     * 定时处理超过30分钟未付款的订单（每分钟一次）
     */
    @Scheduled(cron = "0 */1 * * * ?")
    @Transactional
    public void checkPay()
    {
        log.info("定时处理超过30分钟未付款的订单");

        // 1、筛选状态为未付款且订单创建时间超过30min的订单
        List<Integer> orderIds =Db.lambdaQuery(Order.class)
                .eq(Order::getStatus, StatusConstant.UNPAID)
                .le(Order::getCreateTime, LocalDateTime.now().minusMinutes(30))
                .list()
                .stream()
                .map(Order::getId)
                .toList();

        // 2、无超时未支付订单
        if (orderIds.isEmpty())
        {
            return;
        }

        // 3、将这些订单状态改为已取消
        Db.lambdaUpdate(Order.class)
                .in(Order::getId, orderIds)
                .set(Order::getStatus, StatusConstant.CANCEL)
                .update();
    }

    /**
     * 定时处理超过两天未提车的订单（每十分钟一次）
     */
    @Transactional
    @Scheduled(cron = "0 */10 * * * ?")
    public void checkPick()
    {
        log.info("定时处理超过两天未提车的订单");

        // 1、筛选状态为未提车且订单创建时间超过2天的订单
        List<Order> orders = Db.lambdaQuery(Order.class)
                .eq(Order::getStatus, StatusConstant.UNPICKED)
                .le(Order::getCreateTime, LocalDateTime.now().minusDays(2))
                .list();

        // 2、无超时未提车订单
        if (orders == null || orders.isEmpty())
        {
            return;
        }

        // 3、订单退款
        // 3.1、获取订单关联的用户id
        List<Integer> userIds = orders.stream()
                .map(Order::getUserId)
                .toList();

        // 3.2、将用户id与对应用户组成Map
        Map<Integer, User> userMap = Db.lambdaQuery(User.class)
                .in(User::getId, userIds)
                .list()
                .stream()
                .collect(Collectors.toMap(User::getId, l -> l));

        // 3.3、更新用户的余额（对象）
        orders.stream()
                .forEach(l ->
                {
                    // 3.3.1、获取订单共计
                    Integer payment = l.getPayment();

                    // 3.3.2、获取用户余额
                    User user = userMap.get(l.getUserId());
                    Integer balance = user.getBalance() + payment;

                    // 3.3.3、更新用户余额
                    user.setBalance(balance);
                });

        // 3.4、更新用户余额（数据库）
        Db.updateBatchById(userMap.values());

        // 4、将这些订单状态改为已取消
        // 4.1、获取订单id
        List<Integer> orderIds = orders.stream()
                .map(Order::getId)
                .toList();

        // 4.2、更新订单状态
        Db.lambdaUpdate(Order.class)
                .in(Order::getId, orderIds)
                .set(Order::getStatus, StatusConstant.CANCEL)
                .update();

        // 5、将单车状态设为空闲
        // 5.1、获取订单关联的单车id
        List<Integer> bikeIds = orders.stream()
                .map(Order::getBikeId)
                .toList();

        // 5.2、更新单车状态
        Db.lambdaUpdate(Bike.class)
                .in(Bike::getId,bikeIds)
                .set(Bike::getStatus,StatusConstant.FREE)
                .update();

    }

    /**
     * 定时处理租赁到期的订单（每十分钟一次）
     */
    @Transactional
    @Scheduled(cron = "0 */10 * * * ?")
    public void checkRent()
    {
        log.info("定时处理租赁到期的订单");

        // 1、获取订单状态为租赁中的订单
        List<Order> orders = Db.lambdaQuery(Order.class)
                .eq(Order::getStatus, StatusConstant.RENTING)
                .list();

        // 2、筛选其中已过期的订单id
        List<Integer> orderIds = orders.stream()
                .filter(l ->
                {
                    // 2.1、获取提车时间
                    LocalDateTime pickTime = l.getPickTime();

                    // 2.2、获取租赁时长（单位：天）
                    int duration = Integer.MAX_VALUE;
                    if (Objects.equals(l.getType(), BusinessConstant.DAILY))
                    {
                        duration = l.getCount();
                    }
                    if (Objects.equals(l.getType(), BusinessConstant.MONTHLY))
                    {
                        duration = 30 * l.getCount();
                    }

                    // 2.3、计算还车时间
                    LocalDateTime backTime = pickTime.plusDays(duration);

                    // 2.4、筛选是否已超过还车时间
                    return backTime.isBefore(LocalDateTime.now());
                })
                .map(Order::getId)
                .toList();

        // 2.5、如果无相关订单
        if (orderIds.isEmpty())
        {
            return;
        }

        // 3、更新订单状态（待归还）
        Db.lambdaUpdate(Order.class)
                .in(Order::getId,orderIds)
                .set(Order::getStatus,StatusConstant.TO_RETURN)
                .update();

        // 4、更新单车信息
        // 4.1、获取订单关联单车id
        List<Integer> bikeIds = orders.stream()
                .map(Order::getBikeId)
                .toList();

        // 4.2、更新单车状态（待归还）
        Db.lambdaUpdate(Bike.class)
                .in(Bike::getId,bikeIds)
                .set(Bike::getStatus,StatusConstant.TO_RETURN)
                .update();
    }
}
