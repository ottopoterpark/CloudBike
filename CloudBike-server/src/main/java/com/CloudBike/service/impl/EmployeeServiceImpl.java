package com.CloudBike.service.impl;

import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.dto.LoginDTO;
import com.CloudBike.dto.PageQuery;
import com.CloudBike.entity.Employee;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.EmployeeMapper;
import com.CloudBike.result.PageResult;
import com.CloudBike.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 运营团队表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    /**
     * 员工登录
     * @param loginDTO
     * @return
     */
    public Employee login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = lambdaQuery().eq(Employee::getUsername, username).one();

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null)
            //账号不存在
            throw new BaseException(MessageConstant.ACCOUNT_NOT_FOUND);

        //密码比对
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new BaseException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLED) {
            //账号被锁定
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 员工分页查询
     * @param pageQuery
     * @return
     */
    @Override
    public PageResult page(PageQuery pageQuery)
    {
        // 获取分页参数
        Page<Employee> p = Page.of(pageQuery.getPage(), pageQuery.getPageSize());

        // 分页查询
        lambdaQuery().page(p);

        // 返回结果
        return PageResult.builder()
                .total(p.getTotal())
                .records(p.getRecords())
                .build();
    }

}
