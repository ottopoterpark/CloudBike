package com.CloudBike.mapper;

import com.CloudBike.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author unique
 * @since 2024-11-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
