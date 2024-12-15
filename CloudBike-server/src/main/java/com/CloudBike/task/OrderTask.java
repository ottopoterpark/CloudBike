package com.CloudBike.task;

import com.CloudBike.constant.StatusConstant;
import com.CloudBike.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.CloudBike.entity.Order;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.IllegalFormatCodePointException;
import java.util.List;

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

        // 筛选状态为未付款且订单创建时间超过30min的订单
        List<Integer> orderIds = orderService.lambdaQuery()
                .eq(Order::getStatus, StatusConstant.UNPAID)
                .le(Order::getCreateTime, LocalDateTime.now().minusMinutes(30))
                .list()
                .stream()
                .map(Order::getId)
                .toList();

        // 无超时未支付订单
        if (orderIds.isEmpty())
        {
            return;
        }

        // 将这些订单状态改为已取消
        orderService.lambdaUpdate()
                .in(Order::getId,orderIds)
                .set(Order::getStatus,StatusConstant.CANCEL)
                .update();
    }
}
