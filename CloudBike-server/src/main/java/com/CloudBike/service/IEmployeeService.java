package com.CloudBike.service;

import com.CloudBike.dto.EmployeeInfoPageQuery;
import com.CloudBike.dto.LoginDto;
import com.CloudBike.dto.PasswordDto;
import com.CloudBike.entity.Employee;
import com.CloudBike.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
    Employee login(LoginDto loginDto);

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
     * 根据员工id查询信息
     * @param id
     * @return
     */
    Employee one(Integer id);


    /**
     * （批量删除员工）
     * @param ids
     */
    void delete(List<Integer> ids);

    /**
     * 修改员工账号状态信息
     * @param status
     * @param id
     */
    void changeStatus(Integer status, Integer id);

    /**
     * 重置密码
     * @param id
     */
    void resetPassword(Integer id);

    /**
     * 修改密码
     * @param passwordDTO
     */
    void changePassword(PasswordDto passwordDto);
}
