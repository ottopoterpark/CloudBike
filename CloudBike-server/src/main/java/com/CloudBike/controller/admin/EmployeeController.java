package com.CloudBike.controller.admin;


import com.CloudBike.dto.LoginDTO;
import com.CloudBike.dto.PageQuery;
import com.CloudBike.entity.Employee;
import com.CloudBike.properties.JwtProperties;
import com.CloudBike.result.PageResult;
import com.CloudBike.result.Result;
import com.CloudBike.service.IEmployeeService;
import com.CloudBike.utils.JwtUtil;
import com.CloudBike.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO)
    {
        log.info("员工登录：{}", loginDTO);

        Employee employee = employeeService.login(loginDTO);

        // 登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("empId", employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        // 返回登录信息
        LoginVO loginVO = LoginVO.builder()
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
    @Transactional
    public Result save(@RequestBody Employee employee)
    {
        log.info("新增员工：{}",employee);
        employeeService.save(employee);
        return Result.success();
    }

    /**
     * 分页查询员工信息
     * @param pageQuery
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(PageQuery pageQuery)
    {
        log.info("员工分页查询：{}",pageQuery);
        PageResult pageResult=employeeService.page(pageQuery);
        return Result.success(pageResult);
    }

    /**
     * 根据员工id查询信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Transactional
    public Result<Employee> one(@PathVariable Integer id)
    {
        log.info("根据员工id查询信息：{}",id);
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @Transactional
    public Result update(@PathVariable Integer id,@RequestBody Employee employee)
    {
        log.info("员工信息修改：{}  {}",id,employee);
        employee.setId(id);

        // 修改员工信息
        employeeService.lambdaUpdate()
                .eq(employee.getId()!=null,Employee::getId,id)
                .set(employee.getUsername()!=null&&!employee.getUsername().isEmpty(),Employee::getUsername,employee.getUsername())
                .set(employee.getName()!=null&&!employee.getName().isEmpty(),Employee::getName,employee.getName())
                .set(employee.getAuthority()!=null,Employee::getAuthority,employee.getAuthority())
                .update();
        return Result.success();
    }

    /**
     * （批量）删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    public Result delete(@RequestParam List<Integer> ids)
    {
        log.info("批量删除：{}",ids);
        employeeService.removeBatchByIds(ids);
        return Result.success();
    }

}
