package com.CloudBike.controller.admin;


import com.CloudBike.dto.BikeInfoDTO;
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
     * @param bikeInfoDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody BikeInfoDTO bikeInfoDTO)
    {
        log.info("新增单车：{}",bikeInfoDTO);
        bikeService.insert(bikeInfoDTO);
        return Result.success();
    }

    /**
     * 根据id查询单车详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<BikeInfoDTO> getById(@PathVariable Integer id)
    {
        log.info("查看单车详情：{}",id);
        BikeInfoDTO bikeInfoDTO= bikeService.get(id);
        return Result.success(bikeInfoDTO);
    }

    /**
     * 修改单车基本信息
     * @param bikeInfoDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody BikeInfoDTO bikeInfoDTO)
    {
        log.info("修改单车基本信息：{}",bikeInfoDTO);
        bikeService.update(bikeInfoDTO);
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
