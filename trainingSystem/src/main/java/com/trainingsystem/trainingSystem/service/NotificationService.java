package com.trainingsystem.trainingSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trainingsystem.trainingSystem.pojo.NormalTest;
import com.trainingsystem.trainingSystem.pojo.Notification;

import java.util.List;

public interface NotificationService extends IService<Notification> {


    //取得一个用户的所有未确认的通知
    List<Notification> getNotificationByUserId(Long userId);


    //通知确认
    int confirmNotification(Long notificationId);

    //添加一条通知记录
    int addOneNotification(Notification notification);

    //向管理员展示所有通知请求
    List<Notification> getAllNotification(Long userId, Integer userType);

    //更新通知处理状态
    int updateCheck(Long notificationId, int check);

    //用户查看自己发送过的通知
    List<Notification> notificationSend(Long userId);

    //用户查看自己接收的通知
    List<Notification> notificationReceived(Long userId);

    //删除通知

}
