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
import com.CloudBike.vo.CartInfoVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        // 5.1、如果此时单车已被租赁或购买，返回提示信息
        if (bike.getStatus() != StatusConstant.AVAILABLE)
            throw new BaseException(MessageConstant.BIKE_TOO_HOT);

        // 5.2、添加购物车
        save(cart);
    }

    /**
     * 查询我的购物车
     *
     * @return
     */
    @Override
    public List<CartInfoVO> listAll()
    {
        // 1、获取用户信息
        Integer userId = BaseContext.getCurrentId();

        // 2、查询用户的购物车信息
        List<Cart> carts = lambdaQuery()
                .eq(Cart::getUserId, userId)
                .list();

        // 2.1、如果购物车为空，返回提示信息
        if (carts == null || carts.isEmpty())
            throw new BaseException(MessageConstant.EMPTY_RESULT);

        // 3、去除其中已经不可用的购物车
        // 3.1、获取购物车中的单车id
        List<Integer> bikeIds = carts.stream()
                .map(Cart::getBikeId)
                .toList();

        // 3.2、获取购物车中的单车
        List<Bike> bikes = Db.lambdaQuery(Bike.class)
                .in(Bike::getId, bikeIds)
                .list();

        // 3.3、筛选其中单车状态仍为正常的单车
        bikes = bikes.stream()
                .filter(l -> l.getStatus() == StatusConstant.AVAILABLE)
                .toList();

        // 3.4、如果筛选结果为空，返回提示信息
        if (bikes == null || bikes.isEmpty())
            throw new BaseException(MessageConstant.EMPTY_CART);

        // 3.5、筛选可用购物车
        // 3.5.1、更新可用单车id
        bikeIds = bikes.stream()
                .map(Bike::getId)
                .toList();

        // 3.5.2、根据单车id筛选可用购物车
        List<Integer> finalBikeIds = bikeIds;
        carts = carts.stream()
                .filter(l -> finalBikeIds.contains(l.getBikeId()))
                .toList();

        // 4、补充属性
        List<CartInfoVO> cartInfoVOS = new ArrayList<>();

        // 4.1、将单车id与单车组成Map
        Map<Integer, Bike> bikeMap = bikes.stream()
                .collect(Collectors.toMap(Bike::getId, l -> l));

        // 4.2、封装VOS结果
        carts.stream()
                .forEach(l->{

                    // 4.2.1、属性拷贝
                    CartInfoVO cartInfoVO = new CartInfoVO();
                    BeanUtils.copyProperties(l, cartInfoVO);

                    // 4.2.2、属性补充（单车编号，单车名称）
                    Bike bike = bikeMap.get(l.getBikeId());
                    cartInfoVO.setNumber(bike.getNumber());
                    cartInfoVO.setName(bike.getName());

                    // 4.2.3、将图片路径字符串转换为集合
                    String image = bike.getImage();
                    List<String> images=new ArrayList<>();
                    if (image!=null&&!image.isEmpty())
                        images= Arrays.asList(image.split(","));

                    // 4.2.4、属性补充（图片路径集合）
                    cartInfoVO.setImages(images);

                    // 4.2.5、将VO存入VOS
                    cartInfoVOS.add(cartInfoVO);
                });

        // 5、返回结果
        return cartInfoVOS;
    }
}
