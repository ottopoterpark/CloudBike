package com.CloudBike.service.impl;

import com.CloudBike.entity.Employee;
import com.CloudBike.mapper.EmployeeMapper;
import com.CloudBike.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运营团队表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-18
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
