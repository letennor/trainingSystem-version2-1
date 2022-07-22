package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.CountOnlineNumber;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.TestSet;
import com.trainingsystem.trainingSystem.pojo.User;
import com.trainingsystem.trainingSystem.pojo.UserLogin;
import com.trainingsystem.trainingSystem.service.*;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@RestController
public class UserLoginController {
    @Autowired
    UserLoginService userLoginService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    StudentTestService studentTestService;
    @Autowired
    TestSetService testSetService;
    @Autowired
    RedisTemplate redisTemplate;


    //登录验证
    @PostMapping("/login")
    public Result<?> login(@RequestBody JSONObject jsonObject) {
        //判断这个用户的类型
        Integer account = jsonObject.getInteger("account");
        String pwd = jsonObject.getString("pwd");
        Integer userType = jsonObject.getInteger("userType");
        UserLogin searchUserLogin = userLoginService.ifInfoMatch(account, pwd, userType);
        if (searchUserLogin != null) {
            //获得这个user
            User userByAccount = userService.getUserByAccount(account);
            //登录，记录用户到redis中
            if (userType == 1) {
                //获得key
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String key = "login_" + format.format(date);

                //加入到redis中
                redisTemplate.opsForHyperLogLog().add(key, userByAccount.getUserId() + "");

            }

            return ResultUtil.success(userByAccount);
        } else {
            return ResultUtil.defineFail(0, "用户名或密码错误");
        }
    }


    //当用户一登陆，就记录到redis里面
    @RequestMapping("/inputRedis")
    @CountOnlineNumber
    public void inputRedis(@RequestBody JSONObject jsonObject) {

    }


    //用户登出
    @PostMapping("/logout")
    public void logout(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        userService.userOffLine(userId);
    }


    //修改密码
    @RequestMapping("/modifyPassword")
    @MyTransactional
    public Result<?> modifyPassword(@RequestBody JSONObject jsonObject) {
        String originPassword = jsonObject.getString("originPassword");
        String newPassword = jsonObject.getString("newPassword");
        Integer account = jsonObject.getInteger("account");
        Integer userType = jsonObject.getInteger("userType");

        //检测这个人的密码是否正确
        UserLogin userLogin = userLoginService.ifInfoMatch(account, originPassword, userType);
        if (userLogin != null) {
            //密码正确，修改密码
            UserLogin modifyUserLogin = new UserLogin();
            modifyUserLogin.setAccount(account);
            modifyUserLogin.setPwd(newPassword);
            int i = userLoginService.updatePassword(modifyUserLogin);
            return ResultUtil.success(i);
        } else {
            //密码不正确
            return ResultUtil.defineFail(0, "密码不正确");
        }

    }


    //用户修改密码
    @PostMapping("/modifyPwd")
    @MyTransactional
    public Result<?> modifyPwd(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        String oldPwd = jsonObject.getString("oldPwd");
        String newPwd = jsonObject.getString("newPwd");

        int status = userLoginService.modifyPwd(userId, oldPwd, newPwd);
        if (status == 1) {
            return ResultUtil.define(200, "修改成功", 1);
        } else {
            return ResultUtil.define(400, "原密码验证错误！！！", 0);
        }
    }
}

