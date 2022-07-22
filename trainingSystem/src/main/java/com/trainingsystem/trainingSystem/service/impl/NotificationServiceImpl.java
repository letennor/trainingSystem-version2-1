package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.mapper.NormalTestMapper;
import com.trainingsystem.trainingSystem.mapper.NotificationMapper;
import com.trainingsystem.trainingSystem.pojo.NormalTest;
import com.trainingsystem.trainingSystem.pojo.Notification;
import com.trainingsystem.trainingSystem.service.NormalTestService;
import com.trainingsystem.trainingSystem.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("db1")
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    NotificationMapper notificationMapper;


    //取得一个用户的所有未确认的通知
    @Override
    public List<Notification> getNotificationByUserId(Long userId) {
        QueryWrapper<Notification> notificationQueryWrapper = new QueryWrapper<>();
        notificationQueryWrapper.eq("receive_user_id", userId);
        notificationQueryWrapper.eq("is_check", 0);

        List<Notification> notificationList = notificationMapper.selectList(notificationQueryWrapper);

        return notificationList;
    }


    //通知确认
    @Override
    public int confirmNotification(Long notificationId) {
        Notification notification = new Notification();
        notification.setNotificationId(notificationId);
        notification.setIsCheck(1);

        return notificationMapper.updateById(notification);
    }


    //学生向管理员发送申请更换模式的通知
    @Override
    public int addOneNotification(Notification notification) {
        int status = notificationMapper.insert(notification);
        return status;
    }


    //向管理员展示所有通知请求
    @Override
    public List<Notification> getAllNotification(Long adminId, Integer userType) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.in("receive_user_id", adminId, 3)
                .eq("user_type", userType);
        List<Notification> notificationList = notificationMapper.selectList(wrapper);
        return notificationList;
    }


    //更新通知处理状态
    @Override
    public int updateCheck(Long notificationId, int check) {
        int s = notificationMapper.updateCheck(notificationId, check);
        return s;
    }


    //用户查看自己发送过的通知
    @Override
    public List<Notification> notificationSend(Long userId) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Notification> notificationList = notificationMapper.selectList(wrapper);
        return notificationList;
    }


    //用户查看自己接收的通知.emmm可以和前一个方法合并一下
    @Override
    public List<Notification> notificationReceived(Long userId) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.in("receive_user_id", userId, 0, 1);
        List<Notification> notificationList = notificationMapper.selectList(wrapper);
        return notificationList;
    }
}
