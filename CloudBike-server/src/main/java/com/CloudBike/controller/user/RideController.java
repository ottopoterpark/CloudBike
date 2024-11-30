package com.CloudBike.controller.user;


import com.CloudBike.entity.Ride;
import com.CloudBike.result.Result;
import com.CloudBike.service.IRideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
    private final IRideService groupService;

    /**
     * 申请骑行团
     * @param ride
     * @return
     */
    @PostMapping
    public Result insert(@RequestBody Ride ride)
    {
        log.info("申请骑行团：{}",ride);
        groupService.insert(ride);
        return Result.success();
    }
}
