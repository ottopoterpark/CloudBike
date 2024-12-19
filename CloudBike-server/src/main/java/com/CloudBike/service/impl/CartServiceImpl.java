package com.CloudBike.service.impl;

import com.CloudBike.constant.BusinessConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.constant.TypeConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.entity.Bike;
import com.CloudBike.entity.Cart;
import com.CloudBike.entity.Order;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.CartMapper;
import com.CloudBike.service.ICartService;
import com.CloudBike.vo.CartInfoVO;
import com.CloudBike.vo.OrderSubmitVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
@RequiredArgsConstructor
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
        {
            throw new BaseException(MessageConstant.BUSINESS_EXISTS);
        }
        // 3、获取单车信息
        Integer bikeId = cart.getBikeId();
        Bike bike = Db.lambdaQuery(Bike.class)
                .eq(Bike::getId, bikeId)
                .one();

        // 3、判断业务类型
        Integer payment = 0;

        // 3.1、租赁业务
        if (Objects.equals(cart.getType(), BusinessConstant.DAILY))
        {
            payment = bike.getDaily() * cart.getCount();
        }
        if (Objects.equals(cart.getType(), BusinessConstant.MONTHLY))
        {
            payment = bike.getMonthly() * cart.getCount();
        }

        // 3.2、购买业务
        if (Objects.equals(cart.getType(), BusinessConstant.PURCHASE))
        {
            payment = bike.getPrice();
        }

        // 4、属性补充
        cart.setPayment(payment);

        // 5、添加购物车
        // 5.1、如果此时单车已被租赁或购买，返回提示信息
        if (!Objects.equals(bike.getStatus(), StatusConstant.AVAILABLE))
        {
            throw new BaseException(MessageConstant.BIKE_TOO_HOT);
        }

        // 5.2、添加购物车
        save(cart);
    }

    /**
     * 查询我的购物车
     *
     * @return
     */
    @Override
    @Transactional
    public List<CartInfoVO> listAll()
    {
        // 1、获取用户信息
        Integer userId = BaseContext.getCurrentId();

        // 2、查询用户的购物车信息
        List<Cart> carts = lambdaQuery()
                .eq(Cart::getUserId, userId)
                .list();

        // 2.1、如果购物车为空，返回空结果
        if (carts == null || carts.isEmpty())
        {
            return Collections.emptyList();
        }

        // 3、获取购物车关联的单车
        // 3.1、获取关联的单车id
        List<Integer> bikeIds = carts.stream()
                .map(Cart::getBikeId)
                .toList();

        // 3.2、获取id获取单车集合
        List<Bike> bikes = Db.lambdaQuery(Bike.class)
                .in(Bike::getId, bikeIds)
                .list();

        // 4、补充属性
        List<CartInfoVO> cartInfoVOS = new ArrayList<>();

        // 4.1、将单车id与单车组成Map
        Map<Integer, Bike> bikeMap = bikes.stream()
                .collect(Collectors.toMap(Bike::getId, l -> l));

        // 4.2、封装VOS结果
        carts.stream()
                .forEach(l ->
                {

                    // 4.2.1、属性拷贝
                    CartInfoVO cartInfoVO = new CartInfoVO();
                    BeanUtils.copyProperties(l, cartInfoVO);

                    // 4.2.2、属性补充（单车编号，单车名称）
                    Bike bike = bikeMap.get(l.getBikeId());
                    cartInfoVO.setNumber(bike.getNumber());
                    cartInfoVO.setName(bike.getName());

                    // 4.2.3、将图片路径字符串转换为集合
                    String image = bike.getImage();
                    List<String> images = new ArrayList<>();
                    if (image != null && !image.isEmpty())
                    {
                        images = Arrays.asList(image.split(","));
                    }

                    // 4.2.4、属性补充（图片路径集合）
                    cartInfoVO.setImages(images);

                    // 4.2.5、将VO存入VOS
                    cartInfoVOS.add(cartInfoVO);
                });

        // 5、返回结果
        return cartInfoVOS;
    }

    /**
     * 修改购物车数量
     *
     * @param type
     * @param id
     */
    @Override
    @Transactional
    public void updateCount(Integer type, Integer id)
    {
        // 1、判断当前购物车是否可用
        Cart cart = getById(id);
        Integer bikeId = cart.getBikeId();

        // 1.1、如果不可用，删除购物车，并返回提示信息
        Bike bike = Db.getById(bikeId, Bike.class);
        Integer status = bike.getStatus();
        if (!Objects.equals(status, StatusConstant.AVAILABLE))
        {
            throw new BaseException(MessageConstant.BIKE_TOO_HOT);
        }

        // 2、修改业务叠加数量
        Integer count = cart.getCount();

        // 2.1、如果为数量减少业务
        if (Objects.equals(type, TypeConstant.MINUS))
        {
            // 2.1.1、如果数量为1，删除该购物车
            if (count == 1)
            {
                removeById(id);
                return;
            }

            // 2.1.2、如果数量不为1，数量-1
            count -= 1;
        }

        // 2.2、如果为数量增加业务
        if (Objects.equals(type, TypeConstant.PLUS))
        {
            count += 1;
        }

        // 3、重新计算共计
        Integer payment = cart.getPayment();
        Integer cartType = cart.getType();
        if (Objects.equals(cartType, BusinessConstant.DAILY))
        {
            payment = bike.getDaily() * count;
        }
        if (Objects.equals(cartType, BusinessConstant.MONTHLY))
        {
            payment = bike.getMonthly() * count;
        }

        // 4、更新购物车
        lambdaUpdate()
                .eq(Cart::getId, id)
                .set(Cart::getCount, count)
                .set(Cart::getPayment, payment)
                .update();
    }

    /**
     * 根据购物车id批量删除购物车
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeBatch(List<Integer> ids)
    {
        // 根据ids删除购物车
        removeBatchByIds(ids);
    }

    /**
     * 提交订单
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(Integer id)
    {
        // 1、获取当前用户信息
        Integer userId = BaseContext.getCurrentId();

        // 2、获取购物车信息
        Cart cart = getById(id);

        // 3、获取单车
        Integer bikeId = cart.getBikeId();
        Bike bike = Db.getById(bikeId, Bike.class);

        // 4、判断单车状态
        // 4.1、如果单车不为空闲状态，则返回提示信息
        Integer status = bike.getStatus();
        if (!Objects.equals(status, StatusConstant.AVAILABLE))
        {
            throw new BaseException(MessageConstant.BIKE_TOO_HOT);
        }

        // 5、保存订单
        // 5.1、封装结果
        Order order=new Order();
        order.setNumber(UUID.randomUUID().toString().replace("-","").substring(0,10))
                .setType(cart.getType())
                .setCount(cart.getCount())
                .setPayment(cart.getPayment())
                .setBikeId(bikeId)
                .setUserId(userId);
        Db.save(order);

        // 6、返回订单id
        OrderSubmitVO orderSubmitVO=new OrderSubmitVO();
        orderSubmitVO.setId(order.getId());
        return orderSubmitVO;
    }
}
