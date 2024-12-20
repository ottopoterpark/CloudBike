package com.CloudBike.controller.admin;


import com.CloudBike.dto.BikeInfoDto;
import com.CloudBike.dto.BikeInfoPageQuery;
import com.CloudBike.result.PageResult;
import com.CloudBike.result.Result;
import com.CloudBike.service.IBikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 新增单车
     * @param bikeInfoDto
     * @return
     */
    @PostMapping
    public Result save(@RequestBody BikeInfoDto bikeInfoDto)
    {
        log.info("新增单车：{}",bikeInfoDto);
        bikeService.insert(bikeInfoDto);
        return Result.success();
    }

    /**
     * 根据id查询单车详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<BikeInfoDto> getById(@PathVariable Integer id)
    {
        log.info("查看单车详情：{}",id);
        BikeInfoDto bikeInfoDTO= bikeService.get(id);
        return Result.success(bikeInfoDTO);
    }

    /**
     * 修改单车基本信息
     * @param bikeInfoDto
     * @return
     */
    @PutMapping
    public Result update(@RequestBody BikeInfoDto bikeInfoDto)
    {
        log.info("修改单车基本信息：{}",bikeInfoDto);
        bikeService.update(bikeInfoDto);
        return Result.success();
    }

    /**
     * 批量删除单车
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result remove(@RequestParam List<Integer> ids)
    {
        log.info("批量删除单车：{}",ids);
        bikeService.remove(ids);
        return Result.success();
    }
}
