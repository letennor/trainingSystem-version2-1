package com.trainingsystem.trainingSystem.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.mapper.CommentMapper;
import com.trainingsystem.trainingSystem.mapper.KnowledgePointMapper;
import com.trainingsystem.trainingSystem.pojo.Comment;
import com.trainingsystem.trainingSystem.pojo.KnowledgePoint;
import com.trainingsystem.trainingSystem.service.CommentService;
import com.trainingsystem.trainingSystem.service.KnowledgePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("db1")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<Comment> showAllComment(Long questionId) {
        List<Comment> allparent = commentMapper.showAllComment(questionId);
        return allparent;
    }

    @Override
    //查看谋道题目的子评论信息
    public List<Comment> allSonComment(Long questionId) {
        List<Comment> kids = commentMapper.allSonComment(questionId);
        return kids;
    }

    //返回某个具体评论
    @Override
    public Comment showOneComment(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        return comment;
    }

    //发表/回复评论——新增一条
    @Override
    public int sendComment(Comment comment) {
        int status = commentMapper.insert(comment);
        return status;
    }

    //删除自己发表的评论
    @Override
    public int deletdComment(Long commentId) {
        int status = commentMapper.deleteById(commentId);
        return status;
    }

}
