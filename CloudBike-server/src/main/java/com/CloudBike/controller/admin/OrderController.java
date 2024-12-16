package com.CloudBike.controller.admin;


import com.CloudBike.dto.OrderInfoPageQuery;
import com.CloudBike.result.PageResult;
import com.CloudBike.result.Result;
import com.CloudBike.service.IOrderService;
import com.CloudBike.vo.OrderCheckDetailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    /**
     * 订单分页查询
     * @param pageQuery
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(OrderInfoPageQuery pageQuery)
    {
        log.info("订单分页查询：{}",pageQuery);
        PageResult pageResult=orderService.page(pageQuery);
        return Result.success(pageResult);
    }

    /**
     * 根据订单id查看订单详情
     * @param id
     * @return
     */
    @GetMapping
    public Result<OrderCheckDetailVO> one(Integer id)
    {
        log.info("根据订单id查看订单详情：{}",id);
        OrderCheckDetailVO orderCheckDetailVO=orderService.checkOne(id);
        return Result.success(orderCheckDetailVO);
    }

    /**
     * 提车
     * @param id
     * @return
     */
    @PutMapping("/pick")
    public Result update(Integer id)
    {
        log.info("提车：{}",id);
        orderService.update(id);
        return Result.success();
    }
}
