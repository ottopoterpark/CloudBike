package com.CloudBike.service.impl;

import com.CloudBike.constant.BusinessConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.entity.Bike;
import com.CloudBike.entity.Cart;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.CartMapper;
import com.CloudBike.service.ICartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-12-13
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    /**
     * 添加购物车
     *
     * @param cart
     */
    @Override
    @Transactional
    public void insert(Cart cart)
    {
        // 1、获取当前用户信息
        Integer userId = BaseContext.getCurrentId();
        cart.setUserId(userId);

        // 2、查看购物车中是否存在该单车的业务
        // 2.1、如果存在，返回提示消息
        List<Cart> carts = lambdaQuery()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getBikeId, cart.getBikeId())
                .list();
        if (carts != null && !carts.isEmpty())
            throw new BaseException(MessageConstant.BUSINESS_EXISTS);

        // 3、获取单车信息
        Integer bikeId = cart.getBikeId();
        Bike bike = Db.lambdaQuery(Bike.class)
                .eq(Bike::getId, bikeId)
                .one();

        // 3、判断业务类型
        Integer payment = 0;

        // 3.1、租赁业务
        if (cart.getType() == BusinessConstant.DAILY)
            payment = bike.getDaily() * cart.getCount();
        if (cart.getType() == BusinessConstant.MONTHLY)
            payment = bike.getMonthly() * cart.getCount();

        // 3.2、购买业务
        if (cart.getType() == BusinessConstant.PURCHASE)
            payment = bike.getPrice();

        // 4、属性补充
        cart.setPayment(payment);

        // 5、添加购物车
        save(cart);
    }
}
