package com.CloudBike.service;

import com.CloudBike.dto.UserLoginDTO;
import com.CloudBike.entity.User;
import com.CloudBike.vo.RideRecordOverviewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

}
