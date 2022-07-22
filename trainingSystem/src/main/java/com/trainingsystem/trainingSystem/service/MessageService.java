package com.trainingsystem.trainingSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trainingsystem.trainingSystem.pojo.Message;

import java.util.List;

public interface MessageService extends IService<Message> {


    //获得某个老师所有的提问
    List<Message> getTeacherMessage(Long teacherId, Integer isReplied);

    //获得某个老师所有提问的数量
    Integer getTeacherMessageNumber(Long teacherId, Integer isReplied);

    //删除某条信息
    int deleteMessage(Long messageId);


    //回复信息
    int replyMessage(Long messageId, String messageReply);


    //取得所有消息
    List<Message> getAllMessage(Integer isReplied);

    //取得系统中所有信息的数量
    Integer getAllMessageNumber(Integer isReplied);

    //学生写向老师提一条留言
    int studentAskTeacher(Message message);

    //根据学生Id 返回其所有留言
    List<Message> getOneStuAllMessage(Long userId);

    //学生更改自己的留言
    int studentModifyMessage(Message message);

    //学生删除自己的留言
    int studentDeleteMessage(Long messageId);

}
