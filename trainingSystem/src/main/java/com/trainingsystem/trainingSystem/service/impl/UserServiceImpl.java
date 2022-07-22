package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.mapper.*;
import com.trainingsystem.trainingSystem.pojo.StudentTest;
import com.trainingsystem.trainingSystem.pojo.User;
import com.trainingsystem.trainingSystem.pojo.UserLogin;
import com.trainingsystem.trainingSystem.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.util.common.SnowFlakeGenerateIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *r
 * @author letennor
 * @since 2022-02-26
 */
@Service
@DS("db1")

public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserLoginMapper userLoginMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    StudentTestMapper studentTestMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    QuestionRecordMapper questionRecordMapper;
    @Autowired
    TestResultMapper testResultMapper;
    @Autowired
    StudentKnowledgeMapper studentKnowledgeMapper;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    UserService userService;


    //判断注册的学生是否已经存在
    @Override
    public boolean isStudentExists(Integer account) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("account", account);


        User user = userMapper.selectOne(userQueryWrapper);

        if (user == null){
            return false;
        }else {
            return true;
        }
    }


    //注册一名学生
    @Override
    @Transactional//事务管理
    public int insertUser(Long userId, String name, String college, String major, Integer account, String email,
                             String pwd, Integer userType) {
        QueryWrapper<UserLogin> userLoginQueryWrapper = new QueryWrapper<>();

        User user = new User();

        user.setUserId(userId);
        user.setName(name);
        user.setMajor(major);
        user.setAccount(account);
        user.setEmail(email);
        user.setUserType(userType);

        //雪花算法产生pwdId
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker =
                new SnowFlakeGenerateIdWorker(0L,0L);
        String idString = snowFlakeGenerateIdWorker.generateNextId();
        Long pwdId = Long.parseLong(idString);

        //存入UserLogin
        UserLogin userLogin = new UserLogin();
        userLogin.setAccount(account);
        userLogin.setPwd(pwd);
        userLogin.setPwdId(pwdId);
        userLoginMapper.insert(userLogin);

        //存入User
        user.setPwdId(pwdId);
        user.setUserLevel(0);
        user.setCollege(college);

        int insert = userMapper.insert(user);
        return insert;
    }


    //根据account获取一个用户
    @Override
    public User getUserByAccount(Integer account) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("account", account);
        User user = userMapper.selectOne(userQueryWrapper);
        return user;
    }


    //根据一个学生的userId返回userLevel
    @Override
    public int getUserUserLevel(Long userId) {
        User user = userMapper.selectById(userId);
        return user.getUserLevel();
    }


    //更改user的userLevel
    @Override
    @Transactional//事务管理
    public int changeUserLevel(Long userId, Integer userLevel) {
        User user = new User();
        user.setUserLevel(userLevel);
        user.setUserId(userId);

        int i = userMapper.updateById(user);

        //需要把该学生的t_student_test重置
        QueryWrapper<StudentTest> studentTestQueryWrapper = new QueryWrapper<>();
        studentTestQueryWrapper.eq("user_id", userId);
        studentTestMapper.delete(studentTestQueryWrapper);


        return i;
    }


    //修改用户信息
    @Override
    @Transactional//事务管理
    public int modifyUserInfo(User user) {
        int i = userMapper.updateById(user);
        return i;
    }


    //根据userType返回所有账号信息
    @Override
    public List<User> getAllAccount(Integer userType){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_type",userType);
        List<User> userList = userMapper.selectList(wrapper);
        return userList;
    }


    //根据userId注销账号，同时注销userLogin里的东西
    public int deleteAccount(Long userId, Integer account){
        //删除User表里面的账号
        int status = userMapper.deleteById(userId);

        //删除user_login里面的账号
        QueryWrapper<UserLogin> userLoginQueryWrapper = new QueryWrapper<>();
        userLoginQueryWrapper.eq("account", account);
        int delete = userLoginMapper.delete(userLoginQueryWrapper);


        Map<String,Object> map= new HashMap<>();
        map.put("user_id",userId);
        questionRecordMapper.deleteByMap(map);//问题记录表
        testResultMapper.deleteByMap(map);//test结果表
        studentKnowledgeMapper.deleteByMap(map);//学生学习知识点
        studentTestMapper.deleteByMap(map);//学生学习计划
        messageMapper.deleteByMap(map);
        notificationMapper.deleteByMap(map);

        return status;
    }


    //根据userLevel查找符合条件的用户
    @Override
    public List<User> getUserListByUserLevel(Integer userLevel) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_level", userLevel);
        userQueryWrapper.eq("user_type", 1);

        List<User> userList = userMapper.selectList(userQueryWrapper);

        return userList;
    }


    //根据学生所在的model选择用户
    @Override
    public List<User> getUserListByUserModel(Integer userModel) {
        List<User> userList = new ArrayList<>();

        //方法：借助getUserListByUserLevel得到List，然后append两个list
        if (userModel == 1){
            //得到模式1的用户
            //得到userLevel为2，3的用户，然后append
            List<User> userLevel2 = userService.getUserListByUserLevel(2);
            List<User> userLevel3 = userService.getUserListByUserLevel(3);
            //合并两个list
            userLevel2.addAll(userLevel3);
            userList = userLevel2;
        }

        if (userModel == 2){
            //得到模式2的用户
            userList = userService.getUserListByUserLevel(4);
        }

        if (userModel == 3){
            //得到模式3的用户
            userList = userService.getUserListByUserLevel(5);
        }

        return userList;
    }


    //查找所有学生
    @Override
    public List<User> getAllStudent() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_type", 1);
        List<User> users = userMapper.selectList(userQueryWrapper);
        return users;
    }


    //取得最近30min在线的用户
    @Override
    public Integer getOnlineUser() {
        String prefix = "ONLINE_";
        Set keys = redisTemplate.keys(prefix + "*");
        return keys.size();
    }


    //用户登出处理
    @Override
    public void userOffLine(Long userId) {
        //清除redis中在线用户的记录
        String prefix = "ONLINE_";
        String key = prefix + userId;

        redisTemplate.delete(key);
    }


    //管理员获得最近一周用户的登录量
    @Override
    public List<String> getWeeklyUserNumber() {

        List<String> lastWeek = new ArrayList<>();

        //当前时间戳
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date nowDate = new Date(timestamp.getTime());
        String prefix = "login_";


        //过去七天
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Long last7 = timestamp.getTime() - 7*24*60*60*1000;
        Long last6 = timestamp.getTime() - 6*24*60*60*1000;
        Long last5 = timestamp.getTime() - 5*24*60*60*1000;
        Long last4 = timestamp.getTime() - 4*24*60*60*1000;
        Long last3 = timestamp.getTime() - 3*24*60*60*1000;
        Long last2 = timestamp.getTime() - 2*24*60*60*1000;
        Long last1 = timestamp.getTime() - 1*24*60*60*1000;

        Date last_7 = new Date(last7);
        Date last_6 = new Date(last6);
        Date last_5 = new Date(last5);
        Date last_4 = new Date(last4);
        Date last_3 = new Date(last3);
        Date last_2 = new Date(last2);
        Date last_1 = new Date(last1);

        lastWeek.add(redisTemplate.opsForHyperLogLog().size(prefix + format.format(last_1)) +  "");
        lastWeek.add(redisTemplate.opsForHyperLogLog().size(prefix + format.format(last_2)) +  "");
        lastWeek.add(redisTemplate.opsForHyperLogLog().size(prefix + format.format(last_3)) +  "");
        lastWeek.add(redisTemplate.opsForHyperLogLog().size(prefix + format.format(last_4)) +  "");
        lastWeek.add(redisTemplate.opsForHyperLogLog().size(prefix + format.format(last_5)) +  "");
        lastWeek.add(redisTemplate.opsForHyperLogLog().size(prefix + format.format(last_6)) +  "");
        lastWeek.add(redisTemplate.opsForHyperLogLog().size(prefix + format.format(last_7)) +  "");

        return lastWeek;
    }


    @Override
    public int updateLevel(Long userId, Integer userLevel) {
        int s = userMapper.updateLevel(userId,userLevel);
        return s;
    }


    //测试count
    @Override
    public int testCount() {
        Integer integer = questionRecordMapper.selectCount(null);
        return integer;
    }

}