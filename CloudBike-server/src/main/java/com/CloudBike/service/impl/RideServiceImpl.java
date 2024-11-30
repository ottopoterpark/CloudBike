package com.CloudBike.service.impl;

import com.CloudBike.constant.CreditConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.entity.Ride;
import com.CloudBike.entity.User;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.RideMapper;
import com.CloudBike.service.IRideService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (user.getCredit()== CreditConstant.ABNORMAL)
            throw new BaseException(MessageConstant.CREDIT_LIMIT);
        // 2.2、如果信用状态良好，允许发起
        ride.setUserId(userId);
        save(ride);
    }
}
