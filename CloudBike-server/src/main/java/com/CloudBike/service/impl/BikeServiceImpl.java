package com.CloudBike.service.impl;

import com.CloudBike.entity.Bike;
import com.CloudBike.mapper.BikeMapper;
import com.CloudBike.service.IBikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 自行车表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Service
public class BikeServiceImpl extends ServiceImpl<BikeMapper, Bike> implements IBikeService {

}
