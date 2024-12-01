package com.CloudBike.service.impl;

import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.entity.Ride;
import com.CloudBike.entity.User;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.RideMapper;
import com.CloudBike.service.IRideService;
import com.CloudBike.vo.RideOverviewVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 骑行团表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Service
@Transactional
public class RideServiceImpl extends ServiceImpl<RideMapper, Ride> implements IRideService {

    /**
     * 申请骑行团
     *
     * @param ride
     */
    @Override
    public void insert(Ride ride)
    {
        // 1、获取活动发起者信息
        Integer userId = BaseContext.getCurrentId();
        User user = Db.lambdaQuery(User.class)
                .eq(userId != null, User::getId, userId)
                .one();

        // 2、检测用户的信用状态
        // 2.1、如果信用状态被冻结，无法发起活动
        if (user.getCredit() == StatusConstant.DISABLED)
            throw new BaseException(MessageConstant.CREDIT_LIMIT);

        // 2.2、如果信用状态良好，允许发起
        ride.setUserId(userId);
        save(ride);
    }

    /**
     * 查询最近的骑行团
     *
     * @param name
     * @return
     */
    @Override
    public List<RideOverviewVO> list(String name)
    {
        // 1、查询已通过审核，且未开始的骑行团活动，根据更新时间排序
        List<Ride> list = lambdaQuery()
                .like(name != null && !name.isEmpty(), Ride::getName, name)
                .eq(Ride::getStatus, StatusConstant.PASSED)
                .gt(Ride::getDepartureTime, LocalDateTime.now())
                .orderByDesc(Ride::getUpdateTime)
                .list();

        // 2、如果无符合条件的结果，返回提示信息
        if (list == null || list.isEmpty())
            throw new BaseException(MessageConstant.EMPTY_RESULT);

        // 3、将查询信息转化为对应VO
        List<RideOverviewVO> rideOverviewVOS = new ArrayList<>();

        // 3.1、查询出对应活动的发起者id，与活动id组成Map
        Map<Integer, Integer> userIds = list.stream()
                .collect(Collectors.toMap(Ride::getId, Ride::getUserId));

        // 3.2、查询所有活动发起者的username，与用户id组成Map
        Map<Integer, String> usernames = Db.lambdaQuery(User.class)
                .in(User::getId, userIds.values())
                .list()
                .stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        // 3.3、组装VO结果
        list.stream().forEach(l ->
        {
            RideOverviewVO rideOverviewVO = new RideOverviewVO();
            BeanUtils.copyProperties(l, rideOverviewVO);
            String image = null;
            if (l.getImage1() != null && !l.getImage1().isEmpty())
                image = l.getImage1();
            else if (l.getImage2() != null && !l.getImage2().isEmpty())
                image = l.getImage2();
            else if (l.getImage3() != null && !l.getImage3().isEmpty())
                image = l.getImage3();
            if (image != null && !image.isEmpty())
                rideOverviewVO.setImage(image);
            rideOverviewVO.setUsername(usernames.get(userIds.get(l.getId())));
            rideOverviewVOS.add(rideOverviewVO);
        });

        // 4、返回结果
        return rideOverviewVOS;
    }
}
