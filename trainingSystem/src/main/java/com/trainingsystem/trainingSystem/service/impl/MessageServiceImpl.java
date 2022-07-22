package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.mapper.KnowledgePointMapper;
import com.trainingsystem.trainingSystem.mapper.MessageMapper;
import com.trainingsystem.trainingSystem.pojo.KnowledgePoint;
import com.trainingsystem.trainingSystem.pojo.Message;
import com.trainingsystem.trainingSystem.service.KnowledgePointService;
import com.trainingsystem.trainingSystem.service.MessageService;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("db1")
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Autowired
    MessageMapper messageMapper;

    //获得某个老师所有的提问
    @Override
    public List<Message> getTeacherMessage(Long teacherId, Integer isReplied) {
        List<Message> messageList = messageMapper.getTeacherMessage(teacherId, isReplied);
        return messageList;
    }


    //获得某个老师所有提问的数量
    @Override
    public Integer getTeacherMessageNumber(Long teacherId, Integer isReplied) {
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        messageQueryWrapper.eq("teacher_id", teacherId);
        messageQueryWrapper.eq("is_replied", isReplied);
        Integer integer = messageMapper.selectCount(messageQueryWrapper);
        return integer;
    }


    //删除某条信息
    @Override
    public int deleteMessage(Long messageId) {
        Message message = new Message();
        message.setMessageId(messageId);
        message.setIsReplied(-1);

        return messageMapper.updateById(message);
    }


    //回复信息
    @Override
    public int replyMessage(Long messageId, String messageReply) {

        Message message = new Message();
        message.setMessageId(messageId);
        message.setMessageReply(messageReply);
        message.setIsReplied(1);

        return messageMapper.updateById(message);
    }


    //取得所有消息
    @Override
    public List<Message> getAllMessage(Integer isReplied) {
        List<Message> messages = null;

        if (isReplied == null) {
            messages = messageMapper.selectList(null);
        }

        if (isReplied == 1) {
            QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
            messageQueryWrapper.eq("is_replied", 1);
            messages = messageMapper.selectList(messageQueryWrapper);
        }

        if (isReplied == 0) {
            QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
            messageQueryWrapper.eq("is_replied", 0);
            messages = messageMapper.selectList(messageQueryWrapper);
        }

        return messages;

    }


    //取得系统中所有信息的数量
    @Override
    public Integer getAllMessageNumber(Integer isReplied) {
        Integer messageNumber = 0;

        if (isReplied == null) {
            messageNumber = messageMapper.selectCount(null);
        }

        if (isReplied == 1) {
            QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
            messageQueryWrapper.eq("is_replied", 1);
            messageNumber = messageMapper.selectCount(messageQueryWrapper);
        }

        if (isReplied == 0) {
            QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
            messageQueryWrapper.eq("is_replied", 0);
            messageNumber = messageMapper.selectCount(messageQueryWrapper);
        }

        return messageNumber;
    }


    //学生写向老师提一条留言
    @Override
    public int studentAskTeacher(Message message) {
        int status = messageMapper.insert(message);
        return status;
    }


    //根据学生Id 返回其所有留言
    @Override
    public List<Message> getOneStuAllMessage(Long userId) {
        List<Message> messageList = messageMapper.getOneStuAllMessage(userId);
        return messageList;
    }


    //学生更改自己的留言
    @Override
    public int studentModifyMessage(Message message) {
        int status = messageMapper.updateById(message);
        return status;
    }


    //学生删除自己的留言
    @Override
    public int studentDeleteMessage(Long messageId) {
        int status = messageMapper.deleteById(messageId);
        return status;
    }
}
