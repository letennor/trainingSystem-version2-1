package com.trainingsystem.trainingSystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.Message;
import com.trainingsystem.trainingSystem.pojo.User;
import com.trainingsystem.trainingSystem.service.MessageService;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    //获得某个老师的提问
    @RequestMapping("/getTeacherMessage")
    public Result<?> getTeacherMessage(@RequestBody JSONObject jsonObject) {
        Long teacherId = jsonObject.getLong("teacherId");
        Integer isReplied = jsonObject.getInteger("isReplied");

        List<Message> messageList = messageService.getTeacherMessage(teacherId, isReplied);

        return ResultUtil.success(messageList);
    }

    //删除某条信息
    @RequestMapping("/deleteMessage")
    @MyTransactional
    public Result<?> deleteMessageByTeacher(@RequestBody JSONObject jsonObject) {
        Long messageId = jsonObject.getLong("messageId");
        int i = messageService.deleteMessage(messageId);
        return ResultUtil.success(i);
    }


    //老师回复
    @RequestMapping("/replyMessage")
    public Result<?> replyMessage(@RequestBody JSONObject jsonObject) {
        Long messageId = jsonObject.getLong("messageId");
        String messageReply = jsonObject.getString("messageReply");

        Message message = new Message();
        message.setMessageReply(messageReply);
        message.setIsReplied(1);

        int i = messageService.replyMessage(messageId, messageReply);
        return ResultUtil.success(i);
    }


    //学生发表留言
    @PostMapping("/student/submitMessage")
    public Result<?> submitMessage(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        Long teacherId = jsonObject.getLong("teacherId");
        String title = jsonObject.getString("title");
        String content = jsonObject.getString("content");

        Message message = new Message();
        message.setUserId(userId);
        message.setTeacherId(teacherId);
        message.setMessageTitle(title);
        message.setMessageContent(content);

        int status = messageService.studentAskTeacher(message);
        if (status == 1) {
            return ResultUtil.define(200, "留言提交成功", 1);
        } else {
            return ResultUtil.define(400, "未知错误，提交失败", 0);
        }

    }


    //学生查看自己发表的所有留言
    @PostMapping("/student/allMessage")
    public Result<?> studentAllMessage(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        List<Message> messageList = messageService.getOneStuAllMessage(userId);
        return ResultUtil.success(messageList);
    }

    //学生修改留言，前提是老师还未回复
    @PostMapping("/student/modifyMessage")
    public Result<?> studentModifyMessage(@RequestBody JSONObject jsonObject) {
        Long messageId = jsonObject.getLong("messageId");//留言id
//      Long userId = jsonObject.getLong("userId");//用户id
        String title = jsonObject.getString("title");//新标题
        Long teacherId = jsonObject.getLong("teacherId");
        String content = jsonObject.getString("content");//新内容
        Message message = new Message();
        message.setMessageId(messageId);
        message.setMessageTitle(title);
        message.setMessageContent(content);
        message.setTeacherId(teacherId);

        int status = messageService.studentModifyMessage(message);
        if (status == 1) {
            return ResultUtil.define(200, "留言修改成功", 1);
        } else {
            return ResultUtil.define(400, "未知错误，修改失败", 0);
        }
    }


    //学生删除自己的留言
    @PostMapping("/student/deleteMessage")
    @MyTransactional
    public Result<?> studentDeleteMessage(@RequestBody JSONObject jsonObject) {
        Long messageId = jsonObject.getLong("messageId");//留言id
        int status = messageService.studentDeleteMessage(messageId);
        if (status == 1) {
            return ResultUtil.define(200, "留言已删除", 1);
        } else {
            return ResultUtil.define(400, "未知错误，修改失败", 0);
        }
    }

}
