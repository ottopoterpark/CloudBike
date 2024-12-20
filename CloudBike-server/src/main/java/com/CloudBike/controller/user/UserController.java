package com.CloudBike.controller.user;

import com.CloudBike.constant.JwtClaimsConstant;
import com.CloudBike.dto.UserInfoDto;
import com.CloudBike.dto.UserLoginDto;
import com.CloudBike.entity.User;
import com.CloudBike.properties.JwtProperties;
import com.CloudBike.result.Result;
import com.CloudBike.service.IUserService;
import com.CloudBike.utils.JwtUtil;
import com.CloudBike.vo.BalanceVo;
import com.CloudBike.vo.UserLoginVo;
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
     * @param userLoginDto
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVo> login(@RequestBody UserLoginDto userLoginDto)
    {
        log.info("微信用户登录：{}",userLoginDto);

        // 微信登录
        User user =userService.login(userLoginDto);

        // 为微信用户生成jwt令牌
        Map<String, Object> claims=new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        // 封装结果
        UserLoginVo data = UserLoginVo.builder()
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
    public Result<UserInfoDto> one()
    {
        log.info("查看个人信息");
        UserInfoDto userInfoDTO=userService.one();
        return Result.success(userInfoDTO);
    }

    /**
     * 修改个人信息
     * @param userInfoDTO
     * @return
     */
    @PutMapping
    public Result update(UserInfoDto userInfoDTO)
    {
        log.info("修改个人信息：{}",userInfoDTO);
        userService.update(userInfoDTO);
        return Result.success();
    }

    /**
     * 查询个人余额
     * @return
     */
    @GetMapping("/balance")
    public Result<BalanceVo> balance()
    {
        log.info("查询个人余额");
        BalanceVo balanceVO=userService.balance();
        return Result.success(balanceVO);
    }

}
