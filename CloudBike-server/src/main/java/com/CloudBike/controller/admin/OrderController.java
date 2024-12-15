package com.CloudBike.controller.admin;


import com.CloudBike.dto.OrderInfoPageQuery;
import com.CloudBike.result.PageResult;
import com.CloudBike.result.Result;
import com.CloudBike.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
}
