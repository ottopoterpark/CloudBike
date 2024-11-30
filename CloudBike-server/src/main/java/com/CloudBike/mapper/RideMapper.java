package com.CloudBike.mapper;

import com.CloudBike.entity.Ride;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 骑行团表 Mapper 接口
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Mapper
public interface RideMapper extends BaseMapper<Ride> {

}
