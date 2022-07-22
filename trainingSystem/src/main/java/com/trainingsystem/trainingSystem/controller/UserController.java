package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.CountOnlineNumber;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.mapper.TestResultMapper;
import com.trainingsystem.trainingSystem.mapper.UserMapper;
import com.trainingsystem.trainingSystem.pojo.*;
import com.trainingsystem.trainingSystem.service.*;
import com.trainingsystem.trainingSystem.util.common.SnowFlakeGenerateIdWorker;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserLoginService userLoginService;
    @Autowired
    QuestionService questionService;
    @Autowired
    SpecialTestService specialTestService;
    @Autowired
    TestSetService testSetService;
    @Autowired
    MessageService messageService;
    @Autowired
    TestResultService testResultService;
    @Autowired
    TestResultMapper testResultMapper;

    //个人信息修改
    @PostMapping("/student/message")
    @MyTransactional
    public Result<?> modifyUserInfo(@RequestBody User user) {
        int i = userService.modifyUserInfo(user);

        if (i <= 0) {
            return ResultUtil.defineFail(0, "用户找不到");
        } else {
            return ResultUtil.success(null);
        }
    }


    //注册个人信息检查与提交
    @PostMapping("/register/basicInfo")
    @MyTransactional
    public Result<?> register(@RequestBody User user) {
        Integer account = user.getAccount();
        String name = user.getName();
        String college = user.getCollege();
        String major = user.getMajor();
        String email = user.getEmail();
        String pwd = user.getPwd();
        //学生没有注册过
        if (!userService.isStudentExists(account)) {

            //雪花算法生成userId
            SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker =
                    new SnowFlakeGenerateIdWorker(0L, 0L);
            String idString = snowFlakeGenerateIdWorker.generateNextId();
            Long userId = Long.parseLong(idString);

            int status = userService.insertUser(userId, name, college, major, account, email, pwd, 1);

            //在这个学生的test_result里面放入一条假的信息
            TestResult testResult = new TestResult();
            testResult.setUserId(userId);
            testResult.setTestId(-1L);
            testResult.setTestType(-1);
            testResult.setScore(0);
            testResult.setStatus(1);
            testResult.setQuestionNumber(0);
            testResultMapper.insert(testResult);
            return ResultUtil.success(status);
        } else {
            return ResultUtil.defineFail(0, "学生已经注册过");
        }

    }


    //获得用户个人信息
    /*
        参数：
        url地址栏
        userId

        返回：
        User
     */
    @PostMapping("/getInfo")
    public Result<?> getInfo(@RequestBody JSONObject jsonObject) {
        Integer account = jsonObject.getInteger("account");
        Long userId = jsonObject.getLong("userId");

        User user2 = userService.getUserByAccount(account);
        if (user2 == null) {
            //用userId登录
            if (userId != null) {
                user2 = userMapper.selectById(userId);
            }
        }
        if (user2 == null) {
            return ResultUtil.defineFail(0, "没有这个人");
        }
        return ResultUtil.success(user2);
    }


    //管理员添加教师账号
    @PostMapping("/admin/addTeacher")
    @MyTransactional
    public Result<?> addTeacherAccount(@RequestBody User user) {
        Integer account = user.getAccount();
        String name = user.getName();
        String college = user.getCollege();
        String major = user.getMajor();
        String email = user.getEmail();
        String pwd = user.getPwd();
        //教师工号没被注册
        if (!userService.isStudentExists(account)) {
            //雪花算法生成userId
            SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker =
                    new SnowFlakeGenerateIdWorker(0L, 0L);
            String idString = snowFlakeGenerateIdWorker.generateNextId();
            Long userId = Long.parseLong(idString);

            int status = userService.insertUser(userId, name, college, major, account, email, pwd, 2);
            return ResultUtil.success(status);
        } else {
            return ResultUtil.defineFail(0, "该教师工号已经注册过");
        }
    }


    //管理员查看某类型的所有账号
    @PostMapping("/admin/allAccount")
    public Result<?> findAllAccount(@RequestBody JSONObject jsonObject) {
        Integer userType = jsonObject.getInteger("userType");
        List<User> userList = userService.getAllAccount(userType);
        return ResultUtil.success(userList);
    }


    //注销某个账号
    @PostMapping("/deleteAccount")
    @MyTransactional
    public Result<?> deleteAccount(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        Integer account = jsonObject.getInteger("account");
        int status = userService.deleteAccount(userId, account);
        if (status >= 1) {
            return ResultUtil.success(status);
        } else {
            return ResultUtil.defineFail(0, "注销失败");
        }
    }


    //通过用户所在模式取得所有符合条件的用户
    @RequestMapping("/getStudentInfoByUserModel")
    public Result<?> getStudentInfoByUserModel(@RequestBody JSONObject jsonObject) {
        Integer chooseUserModel = jsonObject.getInteger("chooseUserModel");

        List<User> userListByUserLevel = userService.getUserListByUserModel(chooseUserModel);
        return ResultUtil.success(userListByUserLevel);
    }


    //通过用户所在模式取得所有符合条件的用户
    @RequestMapping("/getStudentInfoByUserLevel")
    public Result<?> getStudentInfoByUserLevel(@RequestBody JSONObject jsonObject) {
        Integer userLevel = jsonObject.getInteger("userLevel");

        List<User> userListByUserLevel = userService.getUserListByUserLevel(userLevel);
        return ResultUtil.success(userListByUserLevel);
    }


    //查找所有学生
    @RequestMapping("/getAllStudent")
    public Result<?> getAllStudent() {
        List<User> allStudent = userService.getAllStudent();
        return ResultUtil.success(allStudent);
    }


    //返回老师的统计信息
    /*
        要统计的教师信息：
        1. 自己出的专项试题/所有专项试题
        2. 自己出的套题/所有套题
        3. 出的题目/所有题目
        4. 自己回答的问题/所有被回答的问题
     */
    @RequestMapping("/getTeacherStatisticInfo")
    public Result<?> getTeacherStatisticInfo(@RequestBody JSONObject jsonObject) {
        Long teacherId = jsonObject.getLong("teacherId");

        int mySpecialTestNumber = 0;
        int allSpecialNumber = 0;

        int myTestSetNumber = 0;
        int allTestSetNumber = 0;

        int myQuestionNumber = 0;
        int allQuestionNumber = 0;

        int myMessageNumber = 0;
        int allMessageNumber = 0;


        //取得一个老师制作的所有题目数量
        myQuestionNumber = questionService.getQuestionNumberByTeacherId(teacherId);

        //取得题库中的所有题目数量
        allQuestionNumber = questionService.getQuestionNumber();

        //取得一个老师制作的所有专项数量
        mySpecialTestNumber = specialTestService.getSpecialTestNumberByTeacherId(teacherId);

        //取得系统中的所有专项数量
        allSpecialNumber = specialTestService.getSpecialTestNumber();

        //取得一个老师制作的所有套题数量
        myTestSetNumber = testSetService.getTeacherTestSetNumber(teacherId);

        //取得系统中的所有套题数量
        allTestSetNumber = testSetService.getAllTestSetNumber();

        //取得这个老师所有的回答数量
        myMessageNumber = messageService.getTeacherMessageNumber(teacherId, 1);

        //取得系统中所有的回答数量
        allMessageNumber = messageService.getAllMessageNumber(1);

        Map<String, Object> map = new HashMap<>();
        map.put("myQuestionNumber", myQuestionNumber);
        map.put("allQuestionNumber", allQuestionNumber);
        map.put("mySpecialTestNumber", mySpecialTestNumber);
        map.put("allSpecialNumber", allSpecialNumber);
        map.put("myTestSetNumber", myTestSetNumber);
        map.put("allTestSetNumber", allTestSetNumber);
        map.put("myMessageNumber", myMessageNumber);
        map.put("allMessageNumber", allMessageNumber);
        return ResultUtil.success(map);
    }


    //管理员统计过去一周在线的人
    @RequestMapping("/getLastWeekUserNumber")
    public Result<?> getLastWeekUserNumber() {
        List<String> weeklyUserNumber = userService.getWeeklyUserNumber();
        return ResultUtil.success(weeklyUserNumber);
    }


    //管理员统计过去30min在线的人
    @RequestMapping("/getOnlineUserNumber")
    public Result<?> getOnlineUserNumber() {
        Integer onlineUser = userService.getOnlineUser();
        return ResultUtil.success(onlineUser);
    }


    //请求所有教师的账号信息
    @GetMapping("/allTeacher")
    public Result<?> allTeacher() {
        List<User> teacherList = userService.getAllAccount(2);//教师账号
        return ResultUtil.success(teacherList);
    }

}

