package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.mapper.UserMapper;
import com.trainingsystem.trainingSystem.pojo.UserLogin;
import com.trainingsystem.trainingSystem.mapper.UserLoginMapper;
import com.trainingsystem.trainingSystem.service.UserLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Service
@DS("db1")
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements UserLoginService {

    @Autowired
    UserLoginMapper userLoginMapper;
    @Autowired
    UserMapper userMapper;

    //验证用户登录信息是否正确
    @Override
    public UserLogin ifInfoMatch(Integer account, String pwd, Integer userType) {
        UserLogin userLogin = userLoginMapper.ifInfoMatch(account, pwd, userType);
        return userLogin;
    }


    //修改密码
    @Override
    public int updatePassword(UserLogin userLogin) {
        QueryWrapper<UserLogin> userLoginQueryWrapper = new QueryWrapper<>();
        userLoginQueryWrapper.eq("account", userLogin.getAccount());

        return userLoginMapper.update(userLogin, userLoginQueryWrapper);
    }


    //修改密码
    @Override
    public int modifyPwd(Long userId, String oldPwd, String newPwd) {
        Long pwdId = userMapper.selectById(userId).getPwdId();//据userId找到pwdid
        String truePwd = userLoginMapper.selectById(pwdId).getPwd();//找到真实密码
        if (oldPwd.equals(truePwd)) {//原密码验证成功，可以修改
            UserLogin userLogin = new UserLogin();
            userLogin.setPwdId(pwdId);
            userLogin.setPwd(newPwd);//新密码
            int status = userLoginMapper.updateById(userLogin);//更新
            return status;
        } else
            return 0;
    }


}
