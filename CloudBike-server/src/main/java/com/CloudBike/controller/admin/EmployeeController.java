package com.CloudBike.controller.admin;

import com.CloudBike.constant.JwtClaimsConstant;
import com.CloudBike.dto.EmployeeInfoPageQuery;
import com.CloudBike.dto.LoginDto;
import com.CloudBike.dto.PasswordDto;
import com.CloudBike.entity.Employee;
import com.CloudBike.properties.JwtProperties;
import com.CloudBike.result.PageResult;
import com.CloudBike.result.Result;
import com.CloudBike.service.IEmployeeService;
import com.CloudBike.utils.JwtUtil;
import com.CloudBike.vo.LoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 运营团队表 前端控制器
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeService employeeService;
    private final JwtProperties jwtProperties;

    /**
     * 员工登录
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto)
    {
        log.info("员工登录：{}", loginDto);

        Employee employee = employeeService.login(loginDto);

        // 登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        // 返回登录信息
        LoginVo loginVO = LoginVo.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(loginVO);
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public Result insert(@RequestBody Employee employee)
    {
        log.info("新增员工：{}",employee);
        employeeService.insert(employee);
        return Result.success();
    }

    /**
     * 分页查询员工信息
     * @param employeeInfoPageQuery
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeeInfoPageQuery employeeInfoPageQuery)
    {
        log.info("员工分页查询：{}",employeeInfoPageQuery);
        PageResult pageResult=employeeService.page(employeeInfoPageQuery);
        return Result.success(pageResult);
    }

    /**
     * 根据员工id查询信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> one(@PathVariable Integer id)
    {
        log.info("根据员工id查询信息：{}",id);
        Employee employee=employeeService.one(id);
        return Result.success(employee);
    }

    /**
     * （批量）删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids)
    {
        log.info("批量删除：{}",ids);
        employeeService.delete(ids);
        return Result.success();
    }

    /**
     * 修改员工账号状态信息
     * @param status
     * @param id
     * @return
     */
    @PutMapping("/status/{status}")
    public Result changeStatus(@PathVariable Integer status,Integer id)
    {
        log.info("修改员工账号状态信息：{} {}",status,id);
        employeeService.changeStatus(status,id);
        return Result.success();
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @PutMapping("/reset")
    public Result resetPassword(Integer id)
    {
        log.info("重置密码：{}",id);
        employeeService.resetPassword(id);
        return Result.success();
    }

    /**
     * 修改密码
     * @param passwordDto
     * @return
     */
    @PutMapping("/password")
    public Result changePassword(@RequestBody PasswordDto passwordDto)
    {
        log.info("修改密码：{}",passwordDto);
        employeeService.changePassword(passwordDto);
        return Result.success();
    }
}
