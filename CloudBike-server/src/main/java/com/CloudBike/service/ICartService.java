package com.CloudBike.service;

import com.CloudBike.entity.Cart;
import com.CloudBike.vo.CartInfoVo;
import com.CloudBike.vo.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author unique
 * @since 2024-12-13
 */
public interface ICartService extends IService<Cart> {

    /**
     * 添加购物车
     * @param cart
     */
    void insert(Cart cart);

    /**
     * 查询我的购物车
     * @return
     */
    List<CartInfoVo> listAll();

    /**
     * 修改购物车数量
     * @param type
     * @param id
     */
    void updateCount(Integer type, Integer id);

    /**
     * 根据购物车id批量删除购物车
     * @param ids
     */
    void removeBatch(List<Integer> ids);

    /**
     * 提交订单
     * @param id
     * @return
     */
    OrderSubmitVo submit(Integer id);
}
