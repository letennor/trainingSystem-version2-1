package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.UserLogin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
public interface UserLoginService extends IService<UserLogin> {

    //验证用户登录信息是否正确
    UserLogin ifInfoMatch(Integer account, String pwd, Integer userType);


    //修改密码
    int updatePassword(UserLogin userLogin);

    //修改密码
    int modifyPwd(Long userId, String oldPwd, String newPwd);
}
