package com.CloudBike.controller.user;

import com.CloudBike.entity.Cart;
import com.CloudBike.result.Result;
import com.CloudBike.service.ICartService;
import com.CloudBike.vo.CartInfoVo;
import com.CloudBike.vo.OrderSubmitVo;
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
     *
     * @param cart
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Cart cart)
    {
        log.info("添加购物车：{}", cart);
        cartService.insert(cart);
        return Result.success();
    }

    /**
     * 查询我的购物车
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<CartInfoVo>> list()
    {
        log.info("查询我的购物车");
        List<CartInfoVo> list = cartService.listAll();
        return Result.success(list);
    }

    /**
     * 修改购物车数量
     *
     * @param type
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result update(Integer type, @PathVariable Integer id)
    {
        log.info("修改购物车数量：{}  {}", type, id);
        cartService.updateCount(type, id);
        return Result.success();
    }

    /**
     * 根据购物车id批量删除购物车
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result remove(@RequestParam List<Integer> ids)
    {
        log.info("根据购物车id批量删除购物车：{}",ids);
        cartService.removeBatch(ids);
        return Result.success();
    }

    /**
     * 提交订单
     * @param id
     * @return
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVo> submit(Integer id)
    {
        log.info("提交订单：{}",id);
        OrderSubmitVo orderSubmitVO= cartService.submit(id);
        return Result.success(orderSubmitVO);
    }
}
