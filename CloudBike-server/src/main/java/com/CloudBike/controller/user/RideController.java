package com.CloudBike.controller.user;


import com.CloudBike.entity.Ride;
import com.CloudBike.result.Result;
import com.CloudBike.service.IRideService;
import com.CloudBike.service.impl.RideServiceImpl;
import com.CloudBike.vo.RideDetailVO;
import com.CloudBike.vo.RideOverviewVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 骑行团表 前端控制器
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@RestController("userRideController")
@RequestMapping("/user/ride")
@RequiredArgsConstructor
@Slf4j
public class RideController {
    private final IRideService rideService;
    private final RideServiceImpl rideServiceImpl;

    /**
     * 申请骑行团
     * @param ride
     * @return
     */
    @PostMapping
    public Result insert(@RequestBody Ride ride)
    {
        log.info("申请骑行团：{}",ride);
        rideService.insert(ride);
        return Result.success();
    }

    /**
     * 查询最近的骑行团
     * @param name
     * @return
     */
    @GetMapping("/list")
    public Result<List<RideOverviewVO>> list(String name)
    {
        log.info("查询最近的骑行团：{}",name);
        List<RideOverviewVO> rideOverviewVOS= rideService.list(name);
        return Result.success(rideOverviewVOS);
    }

    /**
     * 根据id查询骑行团详情
     * @param id
     * @return
     */
    @GetMapping
    public Result<RideDetailVO> one(Integer id)
    {
        log.info("根据id查询骑行团详情：{}",id);
        RideDetailVO rideDetailVO=rideService.one(id);
        return Result.success(rideDetailVO);
    }

    /**
     * 加入骑行团
     * @param id
     * @return
     */
    @PutMapping("/join")
    public Result join(Integer id)
    {
        log.info("加入骑行团：{}",id);
        rideService.join(id);
        return Result.success();
    }
}
