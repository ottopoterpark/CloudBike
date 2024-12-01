package com.CloudBike.service;

import com.CloudBike.entity.Ride;
import com.CloudBike.vo.RideOverviewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 骑行团表 服务类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
public interface IRideService extends IService<Ride> {
    /**
     * 申请骑行团
     * @param ride
     */
    void insert(Ride ride);

    /**
     * 查询最近的骑行团
     * @param name
     * @return
     */
    List<RideOverviewVO> list(String name);
}
