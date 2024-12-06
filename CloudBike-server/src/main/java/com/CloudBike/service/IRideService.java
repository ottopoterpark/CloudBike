package com.CloudBike.service;

import com.CloudBike.dto.RideInfoPageQuery;
import com.CloudBike.entity.Ride;
import com.CloudBike.result.PageResult;
import com.CloudBike.vo.RideCheckDetailVO;
import com.CloudBike.vo.RideDetailVO;
import com.CloudBike.vo.RideOverviewVO;
import com.CloudBike.vo.RideRecordOverviewVO;
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

    /**
     * 根据id查询骑行团详情
     * @param id
     * @return
     */
    RideDetailVO one(Integer id);

    /**
     * 加入骑行团
     * @param id
     */
    void join(Integer id);

    /**
     * 骑行团信息分页查询
     * @param rideInfoPageQuery
     * @return
     */
    PageResult page(RideInfoPageQuery rideInfoPageQuery);

    /**
     * 根据id查看骑行团信息详情
     * @param id
     * @return
     */
    RideCheckDetailVO getone(Integer id);

    /**
     * 审核骑行团信息
     * @param id
     * @param status
     */
    void check(Integer id, Integer status);

    /**
     * 查询我的骑行活动
     * @param status
     * @return
     */
    List<RideRecordOverviewVO> history(Integer status);
}