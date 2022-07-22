package com.trainingsystem.trainingSystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trainingsystem.trainingSystem.pojo.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    //查看某道题目的所有主评论
    List<Comment> showAllComment(@Param("questionId") Long questionId);

    //查看谋道题目的子评论信息
    List<Comment> allSonComment(@Param("questionId") Long questionId);

}
