package com.CloudBike.controller.user;


import com.CloudBike.dto.UserLoginDTO;
import com.CloudBike.entity.User;
import com.CloudBike.properties.JwtProperties;
import com.CloudBike.result.Result;
import com.CloudBike.service.IUserService;
import com.CloudBike.utils.JwtUtil;
import com.CloudBike.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@RestController("userUserController")
@RequestMapping("/user/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO)
    {
        return Result.success();
    }
}
