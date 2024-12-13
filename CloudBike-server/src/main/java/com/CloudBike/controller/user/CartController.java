package com.CloudBike.controller.user;

import com.CloudBike.entity.Cart;
import com.CloudBike.result.Result;
import com.CloudBike.service.ICartService;
import com.CloudBike.vo.CartInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 购物车表 前端控制器
 * </p>
 *
 * @author unique
 * @since 2024-12-13
 */
@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final ICartService cartService;

    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Cart cart)
    {
        log.info("添加购物车：{}",cart);
        cartService.insert(cart);
        return Result.success();
    }

    /**
     * 查询我的购物车
     * @return
     */
    @GetMapping("/list")
    public Result<List<CartInfoVO>> list()
    {
        log.info("查询我的购物车");
        List<CartInfoVO> list=cartService.listAll();
        return Result.success(list);
    }
}
