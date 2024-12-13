package com.CloudBike.service;

import com.CloudBike.dto.UserInfoDTO;
import com.CloudBike.dto.UserLoginDTO;
import com.CloudBike.entity.User;
import com.CloudBike.vo.BalanceVO;
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
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * 充值
     * @param discount
     */
    void deposit(Integer discount);

    /**
     * 查看个人信息
     * @return
     */
    UserInfoDTO one();

    /**
     * 修改个人信息
     * @param userInfoDTO
     */
    void update(UserInfoDTO userInfoDTO);

    /**
     * 查询个人余额
     * @return
     */
    BalanceVO balance();
}
