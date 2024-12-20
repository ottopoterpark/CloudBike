package com.CloudBike.service;

import com.CloudBike.dto.UserInfoDto;
import com.CloudBike.dto.UserLoginDto;
import com.CloudBike.entity.User;
import com.CloudBike.vo.BalanceVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
public interface IUserService extends IService<User> {
    /**
     * 微信用户登录
     * @param userLoginDto
     * @return
     */
    User login(UserLoginDto userLoginDto);

    /**
     * 充值
     * @param discount
     */
    void deposit(Integer discount);

    /**
     * 查看个人信息
     * @return
     */
    UserInfoDto one();

    /**
     * 修改个人信息
     * @param userInfoDto
     */
    void update(UserInfoDto userInfoDto);

    /**
     * 查询个人余额
     * @return
     */
    BalanceVo balance();
}
