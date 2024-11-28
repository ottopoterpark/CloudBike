package com.CloudBike.service.impl;

import com.CloudBike.constant.AuthorityConstant;
import com.CloudBike.constant.DefaultConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.context.BaseContext;
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
     * @param loginDTO
     * @return
     */
    public Employee login(LoginDTO loginDTO) {

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
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            throw new BaseException(MessageConstant.PASSWORD_ERROR);
        }

        // 3.3、账户被锁定
        if (employee.getStatus() == StatusConstant.DISABLED) {
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 4、返回实体对象
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

    /**
     * 新增员工
     * @param employee
     */
    @Override
    @Transactional
    public void insert(Employee employee)
    {
        // 1、获取新增员工信息
        String username=employee.getUsername();
        String password=employee.getPassword();

        // 2、获取当前后台人员的账户
        Integer empId = BaseContext.getCurrentId();
        Employee emp = getById(empId);

        // 3、获取当前后台人员的权限
        Integer authority = emp.getAuthority();

        // 4、处理各种异常信息
        // 4.1 权限不足
        if (authority== AuthorityConstant.COMMON)
        {
            throw new BaseException(MessageConstant.AUTHORITY_TOO_LOW);
        }

        // 4.2 用户名重复
        if (username!=null)
        {
            List<Employee> list = lambdaQuery().eq(Employee::getUsername, username).list();
            if (list!=null&&!list.isEmpty())
                throw new BaseException(MessageConstant.DUPLICATE_USERNAME);
        }
        
        // 5、向数据库插入员工信息
        // 5.1 对密码进行MD5加密
        if (password==null)
            password= DefaultConstant.DEFAULT_PASSWORD;
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);

        // 5.2 插入新员工数据
        save(employee);
    }
}
