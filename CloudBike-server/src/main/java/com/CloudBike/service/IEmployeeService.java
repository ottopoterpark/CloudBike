package com.CloudBike.service;

import com.CloudBike.dto.EmployeeInfoPageQuery;
import com.CloudBike.dto.LoginDTO;
import com.CloudBike.dto.PageQuery;
import com.CloudBike.entity.Employee;
import com.CloudBike.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 运营团队表 服务类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 员工登录
     * @param loginDTO
     * @return
     */
    Employee login(LoginDTO loginDTO);

    /**
     * 员工分页查询
     * @param employeeInfoPageQuery
     * @return
     */
    PageResult page(EmployeeInfoPageQuery employeeInfoPageQuery);

    /**
     * 新增员工
     * @param employee
     */
    void insert(Employee employee);

    /**
     * 修改员工信息
     * @param employee
     */
    void update(Employee employee);

    /**
     * 根据员工id查询信息
     * @param id
     * @return
     */
    Employee one(Integer id);
}
