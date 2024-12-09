package com.CloudBike.controller.user;


import com.CloudBike.dto.BikeInfoDTO;
import com.CloudBike.result.Result;
import com.CloudBike.service.IBikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 自行车表 前端控制器
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@RestController("userBikeController")
@RequestMapping("/user/bike")
@Slf4j
@RequiredArgsConstructor
public class BikeController {

    private final IBikeService bikeService;

    /**
     * 单车分类查询
     * @param type
     * @return
     */
    @GetMapping("/category")
    public Result<List<BikeInfoDTO>> category(Integer type)
    {
        log.info("单车分类查询：{}",type);
        List<BikeInfoDTO> bikeInfoDTOS=bikeService.category(type);
        return Result.success(bikeInfoDTOS);
    }
}
