package com.trainingsystem.trainingSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trainingsystem.trainingSystem.pojo.Comment;
import com.trainingsystem.trainingSystem.pojo.KnowledgePoint;

import java.util.List;

public interface CommentService extends IService<Comment> {


    //查看谋道题目的主评论信息
    List<Comment> showAllComment(Long questionId);

    //查看谋道题目的子评论信息
    List<Comment> allSonComment(Long questionId);

    //返回某个具体评论
    Comment showOneComment(Long commentId);

    //发表/回复评论——新增一条
    int sendComment(Comment comment);


    //删除自己发表的评论
    int deletdComment(Long commentId);
}
