package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trainingsystem.trainingSystem.pojo.UserLogin;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
public interface UserService extends IService<User> {

    //判断注册的学生是否已经存在
    boolean isStudentExists(Integer account);


    //注册一名学生
    int insertUser(Long userId, String name, String college, String major, Integer account,
                   String email, String pwd, Integer userType);


    //根据account获取一个用户
    User getUserByAccount(Integer account);


    //根据一个学生的userId返回userLevel
    int getUserUserLevel(Long userId);


    //管理员更改user的userLevel
    int changeUserLevel(Long userId, Integer userLevel);


    //修改用户信息
    int modifyUserInfo(User user);


    //返回所有账号信息
    List<User> getAllAccount(Integer userType);


    //根据userId注销账号，同时删除user_login里面的信息
    int deleteAccount(Long userId, Integer account);


    //根据userLevel查找符合条件的用户
    List<User> getUserListByUserLevel(Integer userLevel);

    //根据学生所在的model选择用户
    List<User> getUserListByUserModel(Integer userModel);

    //查找所有学生
    List<User> getAllStudent();


    //取得最近30min在线的用户
    Integer getOnlineUser();


    //用户登出处理
    void userOffLine(Long userId);


    //管理员获得最近一周用户的登录量
    List<String> getWeeklyUserNumber();

    //更新用户等级
    int updateLevel(Long userId, Integer userLevel);


    //测试count
    int testCount();

}
