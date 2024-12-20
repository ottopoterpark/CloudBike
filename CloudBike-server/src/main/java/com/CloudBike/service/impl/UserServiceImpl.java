package com.CloudBike.service.impl;

import com.CloudBike.constant.DiscountConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.dto.UserInfoDto;
import com.CloudBike.dto.UserLoginDto;
import com.CloudBike.entity.User;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.UserMapper;
import com.CloudBike.properties.WeChatProperties;
import com.CloudBike.service.IUserService;
import com.CloudBike.utils.HttpClientUtil;
import com.CloudBike.vo.BalanceVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    private final WeChatProperties weChatProperties;

    /**
     * 微信用户登录
     *
     * @param userLoginDto
     * @return
     */
    @Override
    @Transactional
    public User login(UserLoginDto userLoginDto)
    {
        // 调用微信接口服务，获得当前微信用户的openid
        // 封装请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", userLoginDto.getCode());
        paramMap.put("grant_type", "authorization_code");

        // 微信官方获得微信用户openid的url
        String loginUrl = "https://api.weixin.qq.com/sns/jscode2session";
        String json = HttpClientUtil.doGet(loginUrl, paramMap);

        // 获得响应结果
        JSONObject jsonObject = JSON.parseObject(json);

        // 解析响应结果
        String openid = jsonObject.getString("openid");

        //        todo 后端开发跳过微信登陆
        if (openid == null || openid.isEmpty())
        {
            openid = "3";
        }

        // 判断openid是否为空，如果为空表示登陆失败，抛出业务异常
        if (openid == null || openid.isEmpty())
        {
            throw new BaseException(MessageConstant.LOGIN_FAILED);
        }

        // 判断当前微信用户是不是新用户
        User user = lambdaQuery()
                .eq(User::getOpenid, openid).one();

        // 如果是新用户，自动完成注册
        if (user == null)
        {
            user = User.builder()
                    .openid(openid)
                    .username("用户" + Long.parseLong(UUID.randomUUID().toString().replace("-", "").substring(0, 15), 16))
                    .build();
            save(user);
        }

        // 返回微信用户对象
        return user;
    }

    /**
     * 充值
     *
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
            case 1 -> DiscountConstant.DISCOUNT_1;
            case 2 -> DiscountConstant.DISCOUNT_2;
            case 3 -> DiscountConstant.DISCOUNT_3;
            case 4 -> DiscountConstant.DISCOUNT_4;
            case 5 -> DiscountConstant.DISCOUNT_5;
            case 6 -> DiscountConstant.DISCOUNT_6;
            default -> throw new BaseException(MessageConstant.DISCOUNT_ILLEGAL);
        };

        // 3、更新虚拟货币
        lambdaUpdate()
                .eq(userId != null, User::getId, userId)
                .set(balance != null, User::getBalance, balance)
                .update();
    }

    /**
     * 查看个人信息
     *
     * @return
     */
    @Override
    public UserInfoDto one()
    {
        // 1、获取当前用户id
        Integer userId = BaseContext.getCurrentId();

        // 2、根据用户id查询用户信息
        User user = getById(userId);

        // 3、封装结果
        UserInfoDto userInfoDTO = new UserInfoDto();
        BeanUtils.copyProperties(user, userInfoDTO);

        // 4、返回结果
        return userInfoDTO;
    }

    /**
     * 修改个人信息
     *
     * @param userInfoDto
     */
    @Override
    @Transactional
    public void update(UserInfoDto userInfoDto)
    {
        // 1、获取当前用户id
        Integer userId = BaseContext.getCurrentId();

        // 2、对用户名进行唯一校验
        // 2.1、查询用户名一致的用户
        String username = userInfoDto.getUsername();
        String phone = userInfoDto.getPhone();
        String image = userInfoDto.getImage();
        User user = lambdaQuery()
                .eq(username != null && !username.isEmpty(), User::getUsername, username)
                .one();

        // 2.2、如果无或者只有一个用户id，且为当前用户id，则允许修改
        if (user != null)
        {
            Integer id = user.getId();
            // 2.3、如果用户id不为当前用户id，则返回提示信息
            if (!Objects.equals(userId, id))
            {
                throw new BaseException(MessageConstant.DUPLICATE_USERNAME);
            }
        }

        // 3、对用户信息进行修改
        lambdaUpdate()
                .eq(User::getId, userId)
                .set(username != null && !username.isEmpty(), User::getUsername, username)
                .set(phone != null && !phone.isEmpty(), User::getPhone, phone)
                .set(image!=null&&!image.isEmpty(),User::getImage,image)
                .update();
    }

    /**
     * 查询个人余额
     * @return
     */
    @Override
    public BalanceVo balance()
    {
        // 获取用户信息
        Integer userId = BaseContext.getCurrentId();
        User user = getById(userId);

        // 封装结果
        BalanceVo balanceVO=new BalanceVo();
        balanceVO.setBalance(user.getBalance());

        // 返回结果
        return balanceVO;
    }
}
