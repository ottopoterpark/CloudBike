package com.CloudBike.service.impl;

import com.CloudBike.constant.BusinessConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.constant.TypeConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.dto.OrderInfoPageQuery;
import com.CloudBike.entity.Bike;
import com.CloudBike.entity.Order;
import com.CloudBike.entity.User;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.OrderMapper;
import com.CloudBike.result.PageResult;
import com.CloudBike.service.IOrderService;
import com.CloudBike.vo.OrderCheckDetailVO;
import com.CloudBike.vo.OrderCheckOverviewVO;
import com.CloudBike.vo.OrderOverviewVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    /**
     * 查看我的订单
     *
     * @param category
     * @return
     */
    @Override
    public List<OrderOverviewVO> list(Integer category)
    {
        // 1、获取用户id
        Integer userId = BaseContext.getCurrentId();

        // 2、根据用户id获取用户所有订单（以创建时间倒序）
        List<Order> orders = lambdaQuery()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreateTime)
                .list();

        // 2.1、如果订单为空，返回提示信息
        if (orders == null || orders.isEmpty())
        {
            throw new BaseException(MessageConstant.EMPTY_RESULT);
        }

        // 3、根据category筛选
        if (!Objects.equals(category, TypeConstant.ALL))
        {
            orders = orders.stream()
                    .filter(l -> Objects.equals(l.getStatus(), category))
                    .toList();
        }

        // 3.1、如果查询结果为空，返回提示信息
        if (orders.isEmpty())
        {
            throw new BaseException(MessageConstant.EMPTY_RESULT);
        }

        // 4、封装属性
        // 4.1、获取这些订单关联的单车
        List<Integer> bikeIds = orders.stream()
                .map(Order::getBikeId)
                .toList();
        Map<Integer, Bike> bikeMap = Db.lambdaQuery(Bike.class)
                .in(Bike::getId, bikeIds)
                .list()
                .stream()
                .collect(Collectors.toMap(Bike::getId, l -> l));

        // 4.2、补充属性
        List<OrderOverviewVO> orderOverviewVOS = new ArrayList<>();
        orders.stream()
                .forEach(l ->
                {
                    OrderOverviewVO orderOverviewVO = new OrderOverviewVO();

                    // 4.2.1、属性拷贝
                    BeanUtils.copyProperties(l, orderOverviewVO);

                    // 4.2.2、单车属性补充
                    Bike bike = bikeMap.get(l.getBikeId());
                    orderOverviewVO.setNumber(bike.getNumber());
                    orderOverviewVO.setName(bike.getName());

                    // 4.2.3、图片路径字符串转集合
                    String image = bike.getImage();
                    List<String> images = new ArrayList<>();
                    if (image != null && !image.isEmpty())
                    {
                        images = Arrays.asList(image.split(","));
                    }
                    orderOverviewVO.setImages(images);

                    // 4.2.4、将VO存入VOS
                    orderOverviewVOS.add(orderOverviewVO);
                });

        // 5、返回数据
        return orderOverviewVOS;
    }

    /**
     * 根据订单id查询订单
     *
     * @param id
     * @return
     */
    @Override
    public OrderOverviewVO one(Integer id)
    {
        // 1、根据id获取订单
        Order order = getById(id);

        // 2、获取订单关联的单车
        Integer bikeId = order.getBikeId();
        Bike bike = Db.getById(bikeId, Bike.class);

        // 3、封装属性
        OrderOverviewVO orderOverviewVO = new OrderOverviewVO();

        // 3.1、属性拷贝
        BeanUtils.copyProperties(order, orderOverviewVO);

        // 3.2、单车属性补充
        orderOverviewVO.setNumber(bike.getNumber());
        orderOverviewVO.setName(bike.getName());

        // 3.3、将图片字符串转集合
        String image = bike.getImage();
        List<String> images = new ArrayList<>();
        if (image != null && !image.isEmpty())
        {
            images = Arrays.asList(image.split(","));
        }
        orderOverviewVO.setImages(images);

        // 4、返回结果
        return orderOverviewVO;
    }

    /**
     * 订单支付
     *
     * @param id
     */
    @Override
    @Transactional
    public void pay(Integer id)
    {
        // 1、根据id获取订单
        Order order = getById(id);

        // 2、判断订单关联单车是否空闲
        // 2.1、获取关联单车
        Integer bikeId = order.getBikeId();
        Bike bike = Db.getById(bikeId, Bike.class);

        // 2.2、如果单车状态不为空闲，则返回提示信息
        Integer status = bike.getStatus();
        if (!Objects.equals(status, StatusConstant.AVAILABLE))
        {
            throw new BaseException(MessageConstant.BIKE_TOO_HOT);
        }

        // 3、扣减余额
        // 3.1、获取当前用户信息
        Integer userId = BaseContext.getCurrentId();
        User user = Db.getById(userId, User.class);

        // 3.2、获取当前用户余额
        Integer balance = user.getBalance();

        // 3.3、如果余额不足扣减，则返回提示信息
        if (balance < order.getPayment())
        {
            throw new BaseException(MessageConstant.TOO_POOR);
        }

        // 3.4、扣减余额
        Db.lambdaUpdate(User.class)
                .eq(User::getId, userId)
                .set(User::getBalance, balance - order.getPayment())
                .update();

        // 4、更新单车信息
        Db.lambdaUpdate(Bike.class)
                .eq(Bike::getId, bikeId)
                .set(Bike::getStatus, StatusConstant.UNPICKED)
                .update();

        // 5、更新订单信息
        lambdaUpdate()
                .eq(Order::getId, id)
                .set(Order::getStatus, StatusConstant.UNPICKED)
                .update();
    }

    /**
     * 根据订单ids批量删除订单
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeBatch(List<Integer> ids)
    {
        // 1、根据ids获取订单
        List<Order> orders = lambdaQuery()
                .in(Order::getId, ids)
                .list();

        // 2、判断这些订单的状态是否可删
        // 2.1、获取订单的状态
        List<Integer> statuses = orders.stream()
                .map(Order::getStatus)
                .toList();

        // 2.2、筛选其中为已完成或已取消的状态
        List<Integer> doneStatus = statuses.stream()
                .filter(l -> Objects.equals(l, StatusConstant.COMPLETED) || Objects.equals(l, StatusConstant.CANCEL))
                .toList();

        // 2.3、如果不全为已完成或已取消，则无法删除，并返回提示信息
        if (!Objects.equals(statuses.size(), doneStatus.size()))
        {
            throw new BaseException(MessageConstant.LIVE_ORDER);
        }

        // 3、删除订单
        removeBatchByIds(ids);

    }

    /**
     * 取消订单
     *
     * @param id
     */
    @Override
    @Transactional
    public void cancel(Integer id)
    {
        // 1、获取当前订单信息
        Order order = getById(id);

        // 2、判断当前订单状态
        Integer status = order.getStatus();

        // 2.1、如果当前订单状态为待付款或待提车，则允许取消订单
        if (!Objects.equals(status, StatusConstant.UNPAID) && !Objects.equals(status, StatusConstant.UNPICKED))
        {
            throw new BaseException(MessageConstant.LIVE_ORDER);
        }

        // 3、更新用户信息（余额）
        // 3,1、如果订单状态为未提车，则需要更新余额
        if (Objects.equals(status, StatusConstant.UNPICKED))
        {
            // 3.2、获取订单共计
            Integer payment = order.getPayment();

            // 3.3、获取用户余额
            Integer userId = BaseContext.getCurrentId();
            User user = Db.getById(userId, User.class);
            Integer balance = user.getBalance() + payment;

            // 3.4、更新用户余额
            Db.lambdaUpdate(User.class)
                    .eq(User::getId, user.getId())
                    .set(User::getBalance, balance)
                    .update();
        }

        // 4、更新订单信息（状态）
        lambdaUpdate()
                .eq(Order::getId, id)
                .set(Order::getStatus, StatusConstant.CANCEL)
                .update();
    }

    /**
     * 订单分页查询
     *
     * @param pageQuery
     * @return
     */
    @Override
    public PageResult page(OrderInfoPageQuery pageQuery)
    {
        // 1、获取查询参数
        Integer page = pageQuery.getPage();
        Integer pageSize = pageQuery.getPageSize();
        Integer category = pageQuery.getCategory();
        String number = pageQuery.getNumber();
        String username = pageQuery.getUsername();
        LocalDate beginDate = pageQuery.getBeginDate();
        LocalDate endDate = pageQuery.getEndDate();

        // 2、分页查询
        Page<Order> p = Page.of(page, pageSize);
        lambdaQuery()
                // 2.1、根据订单编号模糊匹配（如有）
                .like(number != null && !number.isEmpty(), Order::getNumber, number)
                // 2.2、根据下单时间区间筛选（如有）
                .ge(beginDate != null, Order::getCreateTime, beginDate == null ? null : beginDate.atStartOfDay())
                .lt(endDate != null, Order::getCreateTime, endDate == null ? null : endDate.plusDays(1).atStartOfDay())
                .page(p);

        // 2.3、如果无符合条件的结果，返回提示信息
        List<Order> orders = p.getRecords();
        if (orders == null || orders.isEmpty())
        {
            throw new BaseException(MessageConstant.EMPTY_RESULT);
        }

        // 3、根据业务类型筛选
        if (!Objects.equals(category, TypeConstant.ALL))
        {
            orders = orders.stream()
                    .filter(l -> Objects.equals(l.getStatus(), category))
                    .toList();
        }

        // 3.1、如果无符合条件的结果，返回提示信息
        if (orders.isEmpty())
        {
            throw new BaseException(MessageConstant.EMPTY_RESULT);
        }

        // 4、获取关联的用户信息
        // 4.1、获取关联用户id
        List<Integer> userIds = orders.stream()
                .map(Order::getUserId)
                .toList();

        // 4.2、将用户id与用户名组成Map
        Map<Integer, String> usernameMap = Db.lambdaQuery(User.class)
                .in(User::getId, userIds)
                .list().stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        // 5、根据用户名筛选（如有）
        if (username != null && !username.isEmpty())
        {
            // 5.1、遍历筛选用户名模糊匹配的结果
            Set<Integer> ids = usernameMap.keySet();
            userIds = ids.stream()
                    .filter(l -> usernameMap.get(l).contains(username))
                    .toList();

            // 5.2、根据筛选结果筛选订单
            List<Integer> finalUserIds = userIds;
            orders = orders.stream()
                    .filter(l -> finalUserIds.contains(l.getUserId()))
                    .toList();

            // 5.3、如果筛选结果为空
            if (orders.isEmpty())
            {
                throw new BaseException(MessageConstant.EMPTY_RESULT);
            }
        }

        // 6、获取订单关联的单车信息
        // 6.1、获取订单关联单车id
        List<Integer> bikeIds = orders.stream()
                .map(Order::getBikeId)
                .toList();

        // 6.2、将单车id与单车编号组成Map
        Map<Integer, String> numberMap = Db.lambdaQuery(Bike.class)
                .in(Bike::getId, bikeIds)
                .list()
                .stream()
                .collect(Collectors.toMap(Bike::getId, Bike::getNumber));

        // 7、封装结果
        List<OrderCheckOverviewVO> orderCheckOverviewVOS = new ArrayList<>();
        orders.stream()
                .forEach(l ->
                {
                    // 7.1、订单信息属性拷贝
                    OrderCheckOverviewVO orderCheckOverviewVO = new OrderCheckOverviewVO();
                    BeanUtils.copyProperties(l, orderCheckOverviewVO);

                    // 7.2、用户名属性补充
                    orderCheckOverviewVO.setUsername(usernameMap.get(l.getUserId()));

                    // 7.3、单车编号熟悉补充
                    orderCheckOverviewVO.setBikeNumber(numberMap.get(l.getBikeId()));

                    // 7.4、将VO存入VOS
                    orderCheckOverviewVOS.add(orderCheckOverviewVO);
                });

        // 8、返回结果
        return PageResult.builder()
                .total((long) orderCheckOverviewVOS.size())
                .records(orderCheckOverviewVOS)
                .build();
    }

    /**
     * 根据订单id查看订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderCheckDetailVO checkOne(Integer id)
    {
        // 1、获取订单信息
        Order order = getById(id);

        // 2、获取订单关联用户信息
        Integer userId = order.getUserId();
        User user = Db.getById(userId, User.class);

        // 3、获取订单关联单车信息
        Integer bikeId = order.getBikeId();
        Bike bike = Db.getById(bikeId, Bike.class);

        // 4、封装结果
        // 4.1、订单信息属性拷贝
        OrderCheckDetailVO orderCheckDetailVO = new OrderCheckDetailVO();
        BeanUtils.copyProperties(order, orderCheckDetailVO);

        // 4.2、用户信息属性补充
        orderCheckDetailVO.setUsername(user.getUsername());
        if (user.getPhone() != null && !user.getPhone().isEmpty())
        {
            orderCheckDetailVO.setPhone(user.getOpenid());
        }
        orderCheckDetailVO.setCredit(user.getCredit());

        // 4.3、单车信息属性补充
        orderCheckDetailVO.setBikeNumber(bike.getNumber());
        orderCheckDetailVO.setName(bike.getName());

        // 4.3.1、将图片路径字符串转为集合
        String image = bike.getImage();
        List<String> images = new ArrayList<>();
        if (image != null && !image.isEmpty())
        {
            images = Arrays.asList(image.split(","));
        }
        orderCheckDetailVO.setImages(images);

        // 5、返回结果
        return orderCheckDetailVO;
    }

    /**
     * 提车
     *
     * @param id
     */
    @Override
    @Transactional
    public void update(Integer id)
    {
        // 1、获取订单信息
        Order order = getById(id);

        // 2、判断订单状态
        // 2.1、获取订单状态
        Integer status = order.getStatus();

        // 2.2、如果订单状态不为待提车，返回提示信息
        if (!Objects.equals(status, StatusConstant.UNPICKED))
        {
            throw new BaseException(MessageConstant.STATUS_ILLEGAL);
        }

        // 3、修改订单状态和单车状态
        // 3.1、如果订单为租赁
        if (!Objects.equals(order.getType(), BusinessConstant.PURCHASE))
        {
            lambdaUpdate()
                    .eq(Order::getId, id)
                    // 3.1.1、将订单状态设为租赁中
                    .set(Order::getStatus, StatusConstant.RENTING)
                    // 3.1.2、更新提车时间
                    .set(Order::getPickTime, LocalDateTime.now())
                    .update();
            Db.lambdaUpdate(Bike.class)
                    .eq(Bike::getId, order.getBikeId())
                    // 3.1.2、将单车状态设置为租赁中
                    .set(Bike::getStatus, StatusConstant.RENTING)
                    .update();
        }
        // 3.2、如果订单为购买
        if (Objects.equals(order.getType(), BusinessConstant.PURCHASE))
        {
            lambdaUpdate()
                    .eq(Order::getId, id)
                    // 3.2.1、将订单状态设为已完成
                    .set(Order::getStatus, StatusConstant.COMPLETED)
                    // 3.2.2、更新提车时间
                    .set(Order::getPickTime, LocalDateTime.now())
                    .update();
            Db.lambdaUpdate(Bike.class)
                    .eq(Bike::getId, order.getBikeId())
                    // 3.2.3、将单车状态设置为已完成
                    .set(Bike::getStatus, StatusConstant.COMPLETED)
                    .update();
        }
    }

    /**
     * 还车
     *
     * @param id
     */
    @Override
    public void back(Integer id)
    {
        // 1、获取订单信息
        Order order = getById(id);

        // 2、判断订单状态
        // 2.1、获取订单状态
        Integer status = order.getStatus();

        // 2.2、如果订单状态不为租赁中或待归还，返回提示信息
        if (!Objects.equals(status, StatusConstant.RENTING) && !Objects.equals(status, StatusConstant.TO_RETURN))
        {
            throw new BaseException(MessageConstant.STATUS_ILLEGAL);
        }

        // 3、修改订单状态和单车状态
        // 3.1、修改订单状态（已完成）
        lambdaUpdate()
                .eq(Order::getId, id)
                .set(Order::getStatus, StatusConstant.COMPLETED)
                .update();

        // 3.2、修改单车状态（空闲）
        Db.lambdaUpdate(Bike.class)
                .eq(Bike::getId, order.getBikeId())
                // 3.1.2、将单车状态设置为租赁中
                .set(Bike::getStatus, StatusConstant.FREE)
                .update();
    }
}
