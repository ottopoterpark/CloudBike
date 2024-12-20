package com.CloudBike.controller.user;


import com.CloudBike.dto.RideInfoDto;
import com.CloudBike.result.Result;
import com.CloudBike.service.IRideService;
import com.CloudBike.service.impl.RideServiceImpl;
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
     * @param rideInfoDto
     * @return
     */
    @PostMapping
    public Result insert(@RequestBody RideInfoDto rideInfoDto)
    {
        log.info("申请骑行团：{}",rideInfoDto);
        rideService.insert(rideInfoDto);
        return Result.success();
    }

    /**
     * 查询最近的骑行团
     * @param name
     * @return
     */
    @GetMapping("/list")
    public Result<List<RideInfoDto>> list(String name)
    {
        log.info("查询最近的骑行团：{}",name);
        List<RideInfoDto> rideOverviewVOS= rideService.list(name);
        return Result.success(rideOverviewVOS);
    }

    /**
     * 根据id查询骑行团详情
     * @param id
     * @return
     */
    @GetMapping
    public Result<RideInfoDto> one(Integer id)
    {
        log.info("根据id查询骑行团详情：{}",id);
        RideInfoDto rideInfoDTO=rideService.one(id);
        return Result.success(rideInfoDTO);
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

    /**
     * 查询我的骑行活动
     * @param status
     * @return
     */
    @GetMapping("/history")
    public Result<List<RideInfoDto>> history(Integer status)
    {
        log.info("查询我的骑行活动：{}",status);
        List<RideInfoDto> list=rideService.history(status);
        return Result.success(list);
    }

}
