package com.CloudBike.service.impl;

import com.CloudBike.constant.AuthorityConstant;
import com.CloudBike.constant.DefaultConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.dto.EmployeeInfoPageQuery;
import com.CloudBike.dto.LoginDTO;
import com.CloudBike.dto.PasswordDTO;
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
import java.util.Objects;

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
    @Override
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
        {
            throw new BaseException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 3.2 密码错误
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword()))
        {
            throw new BaseException(MessageConstant.PASSWORD_ERROR);
        }

        // 3.3、账户被锁定
        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLED))
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
        if (Objects.equals(authority, AuthorityConstant.COMMON))
        {
            throw new BaseException(MessageConstant.AUTHORITY_TOO_LOW);
        }

        // 4.2 用户名重复
        if (username != null)
        {
            List<Employee> list = lambdaQuery().eq(Employee::getUsername, username).list();
            if (list != null && !list.isEmpty())
            {
                throw new BaseException(MessageConstant.DUPLICATE_USERNAME);
            }
        }

        // 5、向数据库插入员工信息
        // 5.1 对密码进行MD5加密
        if (password == null || password.isEmpty())
        {
            password = DefaultConstant.DEFAULT_PASSWORD;
        }
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
     *
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
        if (Objects.equals(empId, id))
        {
            return getById(id);
        }

        // 3、如果当前后台人员为管理员，直接查询信息
        if (Objects.equals(employee.getAuthority(), AuthorityConstant.ADMINISTRATOR))
        {
            return getById(id);
        }

        // 4、否则普通后台人员无法查询其他人信息
        throw new BaseException(MessageConstant.AUTHORITY_TOO_LOW);
    }

    /**
     * （批量）删除员工
     *
     * @param ids
     */
    @Override
    @Transactional
    public void delete(List<Integer> ids)
    {
        // 1、获取当前员工的权限
        Integer empId = BaseContext.getCurrentId();
        Employee employee = getById(empId);

        // 2、如果为普通员工，无法删除
        if (Objects.equals(employee.getAuthority(), AuthorityConstant.COMMON))
        {
            throw new BaseException(MessageConstant.AUTHORITY_TOO_LOW);
        }

        // 3、ids不能含有管理员id（唯一）
        if (ids.contains(1))
        {
            throw new BaseException(MessageConstant.AUTHORITY_TOO_HIGN);
        }

        // 4、执行删除操作
        removeBatchByIds(ids);
    }

    /**
     * 修改员工帐号状态信息
     *
     * @param status
     * @param id
     */
    @Override
    @Transactional
    public void changeStatus(Integer status, Integer id)
    {
        // 1、获取当前员工的权限信息
        Integer empId = BaseContext.getCurrentId();
        Employee employee = getById(empId);
        Integer authority = employee.getAuthority();

        // 2、普通员工无法执行该操作
        if (Objects.equals(authority, AuthorityConstant.COMMON))
        {
            throw new BaseException(MessageConstant.AUTHORITY_TOO_LOW);
        }

        // 3、管理员无法修改自己的账号信息（保持可用）
        if (id == 1)
        {
            throw new BaseException(MessageConstant.AUTHORITY_TOO_HIGN);
        }

        // 4、执行操作
        lambdaUpdate()
                .eq(id != null, Employee::getId, id)
                .set(status != null, Employee::getStatus, status)
                .update();
    }

    /**
     * 重置密码
     *
     * @param id
     */
    @Override
    @Transactional
    public void resetPassword(Integer id)
    {
        // 1、获取当前员工的权限
        Integer empId = BaseContext.getCurrentId();
        Employee employee = getById(empId);
        Integer authority = employee.getAuthority();

        // 2、如果为普通员工，拒绝操作
        if (Objects.equals(authority, AuthorityConstant.COMMON))
        {
            throw new BaseException(MessageConstant.AUTHORITY_TOO_LOW);
        }

        // 3、判断操作对象是否为管理员（非法操作）
        if (id == 1)
        {
            throw new BaseException(MessageConstant.AUTHORITY_TOO_HIGN);
        }

        // 4、执行操作
        String password=DefaultConstant.DEFAULT_PASSWORD;
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        lambdaUpdate()
                .eq(id!=null,Employee::getId,id)
                .set(password!=null&&!password.isEmpty(),Employee::getPassword,password)
                .update();
    }

    /**
     * 修改密码
     * @param passwordDTO
     */
    @Override
    public void changePassword(PasswordDTO passwordDTO)
    {
        // 1、获取原始密码和新密码
        String oldPassword = passwordDTO.getOldPassword();
        String newPassword = passwordDTO.getNewPassword();

        // 2、获取当前后台员工id
        Integer empId = BaseContext.getCurrentId();

        // 3、获取当前后台员工密码（MD5加密版）
        Employee employee = getById(empId);
        String password = employee.getPassword();

        // 4、对旧密码加密然后对比，如果不一致提示信息
        oldPassword=DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!oldPassword.equals(password))
        {
            throw new BaseException(MessageConstant.OLDPASSWORD_ERROR);
        }

        // 5、对新密码进行MD5加密然后存储
        newPassword=DigestUtils.md5DigestAsHex(newPassword.getBytes());
        lambdaUpdate()
                .eq(empId!=null,Employee::getId,empId)
                .set(newPassword!=null&&!newPassword.isEmpty(),Employee::getPassword,newPassword)
                .update();
    }
}
