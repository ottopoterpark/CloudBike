package com.CloudBike.service;

import com.CloudBike.entity.Cart;
import com.CloudBike.vo.CartInfoVO;
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
    List<CartInfoVO> listAll();
}
