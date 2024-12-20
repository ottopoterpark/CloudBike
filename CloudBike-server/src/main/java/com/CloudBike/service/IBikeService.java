package com.CloudBike.service;

import com.CloudBike.dto.BikeInfoDto;
import com.CloudBike.dto.BikeInfoPageQuery;
import com.CloudBike.entity.Bike;
import com.CloudBike.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自行车表 服务类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
public interface IBikeService extends IService<Bike> {
    /**
     * 单车分页查询
     * @param bikeInfoPageQuery
     * @return
     */
    PageResult page(BikeInfoPageQuery bikeInfoPageQuery);

    /**
     * 新增单车
     * @param bikeInfoDTO
     */
    void insert(BikeInfoDto bikeInfoDto);

    /**
     * 根据id查询订单详情
     * @param id
     * @return
     */
    BikeInfoDto get(Integer id);

    /**
     * 修改单车基本信息
     * @param bikeInfoDTO
     */
    void update(BikeInfoDto bikeInfoDto);

    /**
     * 批量删除单车
     * @param ids
     */
    void remove(List<Integer> ids);

    /**
     * 单车分类查询
     * @param type
     * @return
     */
    List<BikeInfoDto> category(Integer type);
}
