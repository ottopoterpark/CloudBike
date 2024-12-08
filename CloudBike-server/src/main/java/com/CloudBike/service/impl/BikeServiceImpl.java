package com.CloudBike.service.impl;

import com.CloudBike.constant.BusinessConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.dto.BikeInfoPageQuery;
import com.CloudBike.entity.Bike;
import com.CloudBike.entity.Order;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.BikeMapper;
import com.CloudBike.result.PageResult;
import com.CloudBike.result.Result;
import com.CloudBike.service.IBikeService;
import com.CloudBike.vo.BikeCheckOverviewVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 自行车表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Service
public class BikeServiceImpl extends ServiceImpl<BikeMapper, Bike> implements IBikeService {

    /**
     * 单车分页查询
     * @param bikeInfoPageQuery
     * @return
     */
    @Override
    public PageResult page(BikeInfoPageQuery bikeInfoPageQuery)
    {
        // 1、获取分页参数
        Page<Bike> p = Page.of(bikeInfoPageQuery.getPage(), bikeInfoPageQuery.getPageSize());
        String number = bikeInfoPageQuery.getNumber();
        Integer type = bikeInfoPageQuery.getType();
        Integer status = bikeInfoPageQuery.getStatus();

        // 2、进行分页查询
        lambdaQuery()
                .like(number!=null&&!number.isEmpty(),Bike::getNumber,number)
                .eq(type!=null,Bike::getType,type)
                .eq(status!=null,Bike::getStatus,status)
                .page(p);

        // 3、分页查询结果封装
        // 3.1、如果查询结果为空，返回提示信息
        long total = p.getTotal();
        List<Bike> records = p.getRecords();
        if (records==null||records.isEmpty())
            throw new BaseException(MessageConstant.EMPTY_RESULT);

        // 3.2、封装属性
        List<BikeCheckOverviewVO> bikeCheckOverviewVOS=new ArrayList<>();
        records.stream().forEach(l->{
            BikeCheckOverviewVO bikeCheckOverviewVO=new BikeCheckOverviewVO();
            BeanUtils.copyProperties(l,bikeCheckOverviewVO);
            bikeCheckOverviewVOS.add(bikeCheckOverviewVO);
        });

        // 4、查询其中状态为租赁中和待归还的单车（如有）
        // 4.1、获取查询单车的id
        List<Integer> bikeIds = records.stream()
                .filter(l -> l.getStatus() == StatusConstant.RENTING || l.getStatus() == StatusConstant.TO_RETURN)
                .map(Bike::getId)
                .toList();

        if (!bikeIds.isEmpty())
        {
            // 4.2、根据这些单车id查询订单信息
            List<Order> orders = Db.lambdaQuery(Order.class)
                    .in(Order::getBikeId, bikeIds)
                    .list();

            // 4.3、计算应归还时间
            Map<Integer, LocalDateTime> returnTimes = orders.stream()
                    .collect(Collectors.toMap(Order::getBikeId, l ->
                    {
                        LocalDateTime returnTime = l.getPickTime();
                        Integer count = l.getCount();
                        if (l.getType() == BusinessConstant.DAILY)
                            returnTime=returnTime.plusDays(count);
                        if (l.getType() == BusinessConstant.MONTHLY)
                            returnTime=returnTime.plusMonths(count);
                        return returnTime;
                    }));

            // 4.4、补充属性
            bikeCheckOverviewVOS.stream()
                    .filter(l->l.getStatus()==StatusConstant.RENTING||l.getStatus()==StatusConstant.TO_RETURN)
                    .forEach(l->l.setReturnTime(returnTimes.get(l.getId())));
        }

        // 5、返回结果
        return PageResult.builder()
                .total(total)
                .records(bikeCheckOverviewVOS)
                .build();
    }

    /**
     * 新增单车
     * @param bike
     */
    @Override
    @Transactional
    public void insert(Bike bike)
    {
        // 1、检验单车编号的唯一性
        // 1.1、查询单车编号相同的单车
        String number = bike.getNumber();
        List<Bike> bikes = lambdaQuery()
                .eq(Bike::getNumber, number)
                .list();

        // 1.2、如果存在则返回提示信息
        if (bikes!=null&&!bikes.isEmpty())
            throw new BaseException(MessageConstant.DUPLICATE_NUMBER);

        // 1.3、如果不存在则允许新增
        save(bike);
    }

    /**
     * 根据id查询单车详情
     * @param id
     * @return
     */
    @Override
    public Bike get(Integer id)
    {
        // 根据id查询
        return getById(id);
    }

    /**
     * 修改单车基本信息
     * @param bike
     */
    @Override
    @Transactional
    public void update(Bike bike)
    {
        // 1、获取单车id
        Integer id = bike.getId();

        // 2、判断单车编号是否重复
        // 2.1、查询单车编号相同的单车
        Bike one = lambdaQuery()
                .eq(Bike::getNumber,bike.getNumber())
                .one();

        // 2.2、如果无或者该单车id与修改的单车id不同，则不允许修改
        if (one!=null&&one.getId()!=id)
            throw new BaseException(MessageConstant.DUPLICATE_NUMBER);

        // 3、修改单车基本信息
        updateById(bike);
    }
}
