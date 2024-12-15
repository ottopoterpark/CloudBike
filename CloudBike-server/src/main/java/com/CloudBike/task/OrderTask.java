package com.CloudBike.task;

import com.CloudBike.constant.StatusConstant;
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
import java.util.Collection;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单定时任务类
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderTask {

    private final IOrderService orderService;

    /**
     * 定时处理超过30分钟未付款的订单（每分钟一次）
     */
    @Scheduled(cron = "0 */1 * * * ?")
    @Transactional
    public void checkPay()
    {
        log.info("定时处理超过30分钟未付款的订单");

        // 1、筛选状态为未付款且订单创建时间超过30min的订单
        List<Integer> orderIds = orderService.lambdaQuery()
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
        orderService.lambdaUpdate()
                .in(Order::getId, orderIds)
                .set(Order::getStatus, StatusConstant.CANCEL)
                .update();
    }

    /**
     * 定时处理超过两天未提车的订单（每分钟一次）
     */
    @Transactional
    @Scheduled(cron = "0 */1 * * * ?")
    public void checkPick()
    {
        log.info("定时处理超过两天未提车的订单");

        // 1、筛选状态为未提车且订单创建时间超过2天的订单
        List<Order> orders = orderService.lambdaQuery()
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
        List<Integer> orderIds = orders.stream()
                .map(Order::getId)
                .toList();
        orderService.lambdaUpdate()
                .in(Order::getId, orderIds)
                .set(Order::getStatus, StatusConstant.CANCEL)
                .update();
    }

}
