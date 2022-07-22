package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.CountOnlineNumber;
import com.trainingsystem.trainingSystem.pojo.Notification;
import com.trainingsystem.trainingSystem.pojo.TestSet;
import com.trainingsystem.trainingSystem.service.NotificationService;
import com.trainingsystem.trainingSystem.service.StudentTestService;
import com.trainingsystem.trainingSystem.service.TestSetService;
import com.trainingsystem.trainingSystem.service.UserService;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    StudentTestService studentTestService;
    @Autowired
    TestSetService testSetService;
    @Autowired
    UserService userService;


    //取得一个用户的所有未确认的通知
    @RequestMapping("/getNotificationByUserId")
    @CountOnlineNumber
    public Result<?> getNotificationByUserId(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        List<Notification> notificationList = notificationService.getNotificationByUserId(userId);

        return ResultUtil.success(notificationList);
    }


    //通知确认
    @RequestMapping("/confirmNotification")
    public Result<?> confirmNotification(@RequestBody JSONObject jsonObject) {
        Long notificationId = jsonObject.getLong("notificationId");
        int i = notificationService.confirmNotification(notificationId);

        return ResultUtil.success(i);
    }


    //学生申请更换模式
    @PostMapping("/modifyModel")
    public Result<?> modifyModel(@RequestBody JSONObject jsonObject) {

        Long userId = jsonObject.getLong("userId");
        Integer userType = jsonObject.getInteger("userType");
        String info = jsonObject.getString("reason");

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setUserType(userType);
        notification.setReceiveUserId(3L);//
        notification.setNotificationInfo(info);

        int status = notificationService.addOneNotification(notification);
        if (status == 1) {
            return ResultUtil.define(200, "请等待管理员审核", 1);
        } else {
            return ResultUtil.define(400, "未知错误，提交失败", 0);
        }
    }


    //展示某一条通知（申请）的具体内容
    @PostMapping("/showNotification")
    public Result<?> showNotification(@RequestBody JSONObject jsonObject) {
        Long notificationId = jsonObject.getLong("notificationId");
        Notification notification = notificationService.getById(notificationId);
        return ResultUtil.success(notification);
    }


    //向管理员展示所有学生请求通知
    @PostMapping("/admin/studentAsk")
    public Result<?> studentAsk(@RequestBody JSONObject jsonObject) {
        Long adminId = jsonObject.getLong("adminId");
        List<Notification> notificationList = notificationService.getAllNotification(adminId, 1);
        return ResultUtil.success(notificationList);
    }


    //管理员审核处理学生的申请通知,前提是：查看的该条请求的is_check为0，未被处理，才能处理
    @PostMapping("/admin/checkStuApplication")
    public Result<?> adminCheckStuApplication(@RequestBody JSONObject jsonObject) {
        Long notificationId = jsonObject.getLong("notificationId");
        Long studentId = jsonObject.getLong("studentId");
        Integer decision = jsonObject.getInteger("decision");
        String reply = jsonObject.getString("reply");
        Integer newLevel = jsonObject.getInteger("newLevel");
        Long adminId = jsonObject.getLong("adminId");
        Notification notification = new Notification();

        //给学生发通知，不管同意或拒绝都要通知学生
        notification.setUserId(adminId);
        notification.setUserType(3);
        notification.setNotificationInfo(reply);
        notification.setReceiveUserId(studentId);
        notificationService.addOneNotification(notification);
        //更改原通知状态为已处理 is_check==1
        notificationService.updateCheck(notificationId, 1);

        if (decision == 1) { //同意处理，即更改学生的模式
            //先清理student——test表该学生的相关记录
            studentTestService.modifyModel(studentId);
            //更改学生等级
            userService.updateLevel(studentId, newLevel);
            //根据该学生的新模式给他放题
            if (newLevel == 2) {
                TestSet testSet = testSetService.getTestSetBySetId(954811036794355712L);//模式一，level2
                studentTestService.insertNormalTestSet(studentId, testSet);
            } else if (newLevel == 3) {
                TestSet testSet = testSetService.getTestSetBySetId(954825154939060224L);//模式一，level3
                studentTestService.insertNormalTestSet(studentId, testSet);
            } else if (newLevel == 4) {
                //进入2模式,难度为1的试卷放进t_student_test里面
                //第一次进入，将setlevel为1的试卷放进去
                TestSet testSet = testSetService.getTestSetBySetLevel(1);
                studentTestService.insertNormalTestSet(studentId, testSet);
            } else if (newLevel == 5) {
                //如果更换到了模式三，应该再发一条通知给学生
                //放入学生最弱的3个知识点的专项
                studentTestService.systemGenerate(studentId);
            }

            return ResultUtil.defineFail(1, "更改成功！已给学生发放通知");
        } else {//不同意更换
            return ResultUtil.defineFail(0, "已给学生发放拒绝通知");
        }

    }


    //向管理员展示所有教师请求通知
    @PostMapping("/admin/teacherAsk")
    public Result<?> teacherAsk(@RequestBody JSONObject jsonObject) {
        Long adminId = jsonObject.getLong("adminId");
        List<Notification> notificationList = notificationService.getAllNotification(adminId, 2);
        return ResultUtil.success(notificationList);
    }


    //管理员审核处理教师的申请后反馈,前提是：查看的该条请求的is_check为0，未被处理，才能处理
    @PostMapping("/admin/checkTeaApplication")
    public Result<?> adminCheckTeaApplication(@RequestBody JSONObject jsonObject) {
        Long notificationId = jsonObject.getLong("notificationId");
        Long teacherId = jsonObject.getLong("teacherId");
        String reply = jsonObject.getString("reply");
        Long adminId = jsonObject.getLong("adminId");
        Notification notification = new Notification();

        //给教师发反馈通知
        notification.setUserId(adminId);
        notification.setUserType(3);
        notification.setNotificationInfo(reply);
        notification.setReceiveUserId(teacherId);
        notificationService.addOneNotification(notification);

        //更改原通知状态为已处理 is_check==1
        notificationService.updateCheck(notificationId, 1);

        return ResultUtil.defineFail(0, "已给教师发放反馈通知");

    }


    //用户查看自己发过的所有通知（已发送的）
    @PostMapping("/notification/send")
    public Result<?> notificationSend(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        List<Notification> notificationList = notificationService.notificationSend(userId);
        return ResultUtil.success(notificationList);
    }


    // 用户查看自己接收过的通知（已接收的）
    @PostMapping("/notification/received")
    public Result<?> notificationReceived(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        List<Notification> notificationList = notificationService.notificationReceived(userId);
        return ResultUtil.success(notificationList);
    }


}
