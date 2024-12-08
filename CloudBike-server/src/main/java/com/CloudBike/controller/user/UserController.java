package com.CloudBike.controller.user;

import com.CloudBike.constant.JwtClaimsConstant;
import com.CloudBike.dto.UserInfoDTO;
import com.CloudBike.dto.UserLoginDTO;
import com.CloudBike.entity.User;
import com.CloudBike.properties.JwtProperties;
import com.CloudBike.result.Result;
import com.CloudBike.service.IUserService;
import com.CloudBike.utils.JwtUtil;
import com.CloudBike.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final JwtProperties jwtProperties;

    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO)
    {
        log.info("微信用户登录：{}",userLoginDTO);

        // 微信登录
        User user =userService.login(userLoginDTO);

        // 为微信用户生成jwt令牌
        Map<String, Object> claims=new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        // 封装结果
        UserLoginVO data = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        // 返回结果
        return Result.success(data);
    }

    /**
     * 充值
     * @param discount
     * @return
     */
    @PutMapping("/deposit")
    public Result deposit(Integer discount)
    {
        log.info("充值：{}",discount);
        userService.deposit(discount);
        return Result.success();
    }

    /**
     * 查看个人信息
     * @return
     */
    @GetMapping
    public Result<UserInfoDTO> one()
    {
        log.info("查看个人信息");
        UserInfoDTO userInfoDTO=userService.one();
        return Result.success(userInfoDTO);
    }

}
