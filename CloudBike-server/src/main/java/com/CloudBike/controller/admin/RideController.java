package com.CloudBike.controller.admin;


import com.CloudBike.dto.RideInfoPageQuery;
import com.CloudBike.result.PageResult;
import com.CloudBike.result.Result;
import com.CloudBike.service.IRideService;
import com.CloudBike.vo.RideCheckDetailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 骑行团表 前端控制器
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@RestController("adminRideController")
@RequestMapping("/admin/ride")
@Slf4j
@RequiredArgsConstructor
public class RideController {

    private final IRideService rideService;

    /**
     * 骑行团信息分页查询
     * @param rideInfoPageQuery
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(RideInfoPageQuery rideInfoPageQuery)
    {
        log.info("骑行团信息分页查询：{}",rideInfoPageQuery);
        PageResult pageResult=rideService.page(rideInfoPageQuery);
        return Result.success(pageResult);
    }

    /**
     * 根据id查看骑行团信息详情
     * @param id
     * @return
     */
    @GetMapping
    public Result<RideCheckDetailVO> one(Integer id)
    {
        log.info("查看骑行团信息详情：{}",id);
        RideCheckDetailVO rideCheckDetailVO=rideService.getone(id);
        return Result.success(rideCheckDetailVO);
    }

    /**
     * 审核骑行团信息
     * @param status
     * @param id
     * @return
     */
    @PutMapping("/status/{status}")
    public Result check(Integer id,@PathVariable Integer status)
    {
        log.info("审核骑行团信息：{}  {}",id,status);
        rideService.check(id,status);
        return Result.success();
    }
}