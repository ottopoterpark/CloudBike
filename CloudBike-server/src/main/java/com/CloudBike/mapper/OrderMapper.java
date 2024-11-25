package com.CloudBike.mapper;

import com.CloudBike.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
