package com.CloudBike.mapper;

import com.CloudBike.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author unique
 * @since 2024-12-13
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

}
