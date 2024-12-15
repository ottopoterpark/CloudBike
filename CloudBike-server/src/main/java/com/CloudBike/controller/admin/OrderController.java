package com.CloudBike.controller.admin;


import com.CloudBike.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


}
