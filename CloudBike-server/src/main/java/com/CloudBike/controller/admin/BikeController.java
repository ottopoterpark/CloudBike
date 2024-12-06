package com.CloudBike.controller.admin;


import com.CloudBike.dto.BikeInfoPageQuery;
import com.CloudBike.result.PageResult;
import com.CloudBike.result.Result;
import com.CloudBike.service.IBikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 自行车表 前端控制器
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@RestController("adminBikeController")
@RequestMapping("/admin/bike")
@RequiredArgsConstructor
@Slf4j
public class BikeController {

    private final IBikeService bikeService;

    /**
     * 单车分页查询
     * @param bikeInfoPageQuery
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(BikeInfoPageQuery bikeInfoPageQuery)
    {
        log.info("单车分页查询：{}",bikeInfoPageQuery);
        PageResult pageResult = bikeService.page(bikeInfoPageQuery);
        return Result.success(pageResult);
    }
}
