package com.CloudBike.service.impl;

import com.CloudBike.constant.AuthorityConstant;
import com.CloudBike.constant.DefaultConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.dto.EmployeeInfoPageQuery;
import com.CloudBike.dto.LoginDTO;
import com.CloudBike.dto.PageQuery;
import com.CloudBike.entity.Employee;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.EmployeeMapper;
import com.CloudBike.result.PageResult;
import com.CloudBike.result.Result;
import com.CloudBike.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

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
     *
     * @param loginDTO
     * @return
     */
    public Employee login(LoginDTO loginDTO)
    {

        // 1、获取用户名和密码
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        // 2、根据用户名查询数据库中的数据
        Employee employee = lambdaQuery().eq(Employee::getUsername, username).one();

        // 3、判断各种异常信息
        // 3.1、账号不存在
        if (employee == null)
            throw new BaseException(MessageConstant.ACCOUNT_NOT_FOUND);

        // 3.2 密码错误
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword()))
        {
            throw new BaseException(MessageConstant.PASSWORD_ERROR);
        }

        // 3.3、账户被锁定
        if (employee.getStatus() == StatusConstant.DISABLED)
        {
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 4、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employee
     */
    @Override
    @Transactional
    public void insert(Employee employee)
    {
        // 1、获取新增员工信息
        String username = employee.getUsername();
        String password = employee.getPassword();

        // 2、获取当前后台人员的账户
        Integer empId = BaseContext.getCurrentId();
        Employee emp = getById(empId);

        // 3、获取当前后台人员的权限
        Integer authority = emp.getAuthority();

        // 4、处理各种异常信息
        // 4.1 权限不足
        if (authority == AuthorityConstant.COMMON)
        {
            throw new BaseException(MessageConstant.AUTHORITY_TOO_LOW);
        }

        // 4.2 用户名重复
        if (username != null)
        {
            List<Employee> list = lambdaQuery().eq(Employee::getUsername, username).list();
            if (list != null && !list.isEmpty())
                throw new BaseException(MessageConstant.DUPLICATE_USERNAME);
        }

        // 5、向数据库插入员工信息
        // 5.1 对密码进行MD5加密
        if (password == null || password.isEmpty())
            password = DefaultConstant.DEFAULT_PASSWORD;
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);

        // 5.2 插入新员工数据
        save(employee);
    }

    /**
     * 员工分页查询
     *
     * @param employeeInfoPageQuery
     * @return
     */
    @Override
    public PageResult page(EmployeeInfoPageQuery employeeInfoPageQuery)
    {
        // 1、获取分页参数
        Integer page = employeeInfoPageQuery.getPage();
        Integer pageSize = employeeInfoPageQuery.getPageSize();
        Page<Employee> p = Page.of(page, pageSize);

        // 2、进行分页查询
        lambdaQuery()
                .like(employeeInfoPageQuery.getUsername() != null && !employeeInfoPageQuery.getUsername().isEmpty(), Employee::getUsername, employeeInfoPageQuery.getUsername())
                .page(p);

        // 3、封装结果
        return PageResult.builder()
                .total(p.getTotal())
                .records(p.getRecords())
                .build();
    }

    /**
     * 根据员工id查询信息
     * @param id
     * @return
     */
    @Override
    public Employee one(Integer id)
    {
        // 1、获取当前后台人员信息
        Integer empId = BaseContext.getCurrentId();
        Employee employee = getById(empId);

        // 2、如果是查看自己的信息，直接返回
        if (empId==id)
        {
            return getById(id);
        }

        // 3、如果当前后台人员为管理员，直接查询信息
        if (employee.getAuthority()==AuthorityConstant.ADMINISTRATOR)
        {
            return getById(id);
        }

        // 4、否则普通后台人员无法查询其他人信息
        throw new BaseException(MessageConstant.AUTHORITY_TOO_LOW);
    }

    /**
     * 修改员工信息
     *
     * @param employee
     */
    @Override
    public void update(Employee employee)
    {
        return;
    }
}
