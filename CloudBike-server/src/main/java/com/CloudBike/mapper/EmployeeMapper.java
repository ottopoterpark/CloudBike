package com.CloudBike.mapper;

import com.CloudBike.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 运营团队表 Mapper 接口
 * </p>
 *
 * @author unique
 * @since 2024-11-18
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
