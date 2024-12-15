package com.CloudBike.controller.user;


import com.CloudBike.result.Result;
import com.CloudBike.service.IOrderService;
import com.CloudBike.vo.OrderOverviewVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final IOrderService orderService;

    /**
     * 查看我的订单
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<OrderOverviewVO>> list(Integer category)
    {
        log.info("查看我的订单：{}",category);
        List<OrderOverviewVO> orderOverviewVOS = orderService.list(category);
        return Result.success(orderOverviewVOS);
    }

    /**
     * 根据订单id查看订单
     * @param id
     * @return
     */
    @GetMapping
    public Result<OrderOverviewVO> one(Integer id)
    {
        log.info("根据订单id查看订单：{}",id);
        OrderOverviewVO orderOverviewVO=orderService.one(id);
        return Result.success(orderOverviewVO);
    }

    /**
     * 订单支付
     * @param id
     * @return
     */
    @PutMapping("/pay")
    public Result pay(Integer id)
    {
        log.info("订单支付：{}",id);
        orderService.pay(id);
        return Result.success();
    }

    /**
     * 根据订单ids批量删除订单
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result remove(@RequestParam List<Integer> ids)
    {
        log.info("根据订单ids批量删除订单：{}",ids);
        orderService.removeBatch(ids);
        return Result.success();
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @PutMapping("/cancel")
    public Result cancel(Integer id)
    {
        log.info("取消订单：{}",id);
        orderService.cancel(id);
        return Result.success();
    }
}
