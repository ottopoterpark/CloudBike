package com.CloudBike.service.impl;

import com.CloudBike.constant.DiscountConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.dto.UserLoginDTO;
import com.CloudBike.entity.User;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.UserMapper;
import com.CloudBike.properties.WeChatProperties;
import com.CloudBike.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 微信官方获得微信用户openid的url
     */
    private final String loginUrl = "https://api.weixin.qq.com/sns/jscode2session";
    private final WeChatProperties weChatProperties;

    /**
     * 微信用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    @Transactional
    public User login(UserLoginDTO userLoginDTO)
    {
        // 调用微信接口服务，获得当前微信用户的openid
        Map<String, String> paramMap = new HashMap<>();                   // 封装请求参数
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", userLoginDTO.getCode());
        paramMap.put("grant_type", "authorization_code");
//        todo 后端开发跳过微信登陆
//        String json = HttpClientUtil.doGet(loginUrl, paramMap);         // 获得响应结果
//        JSONObject jsonObject = JSON.parseObject(json);                 // 解析响应结果
//        String openid = jsonObject.getString("openid");
        String openid = "2";

        // 判断openid是否为空，如果为空表示登陆失败，抛出业务异常
        if (openid == null)
            throw new BaseException(MessageConstant.LOGIN_FAILED);

        // 判断当前微信用户是不是新用户
        User user = lambdaQuery()
                .eq(User::getOpenid, openid).one();

        // 如果是新用户，自动完成注册
        if (user == null)
        {
            user = User.builder()
                    .openid(openid)
                    .username("用户"+ Long.parseLong(UUID.randomUUID().toString().replace("-","").substring(0,15),16))
                    .build();
            save(user);
        }

        // 返回微信用户对象
        return user;
    }

    /**
     * 充值
     * @param discount
     */
    @Override
    @Transactional
    public void deposit(Integer discount)
    {
        // 1、获取当前用户信息
        Integer userId = BaseContext.getCurrentId();
        User user = getById(userId);
        Integer balance = user.getBalance();

        // 2、获取套餐信息
        balance += switch (discount)
        {
            case 1-> DiscountConstant.DISCOUNT_1;
            case 2-> DiscountConstant.DISCOUNT_2;
            case 3-> DiscountConstant.DISCOUNT_3;
            case 4-> DiscountConstant.DISCOUNT_4;
            case 5-> DiscountConstant.DISCOUNT_5;
            case 6-> DiscountConstant.DISCOUNT_6;
            default -> throw new BaseException(MessageConstant.DISCOUNT_ILLEGAL);
        };

        // 3、更新虚拟货币
        lambdaUpdate()
                .eq(userId!=null,User::getId,userId)
                .set(balance!=null,User::getBalance,balance)
                .update();
    }
}
