package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.Comment;
import com.trainingsystem.trainingSystem.service.CommentService;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    //查看某道题目的所有评论信息
    @PostMapping("/showAllComment")
    public Result<?> showAllComment(@RequestBody JSONObject jsonObject) {
        Long questionId = jsonObject.getLong("questionId");
        //主评论
        List<Comment> commentParentList = commentService.showAllComment(questionId);
        //子评论
        List<Comment> commentSonList = commentService.allSonComment(questionId);
        //结果
        List<Comment> result = new ArrayList<>();
        //给每个主评论匹配子评论
        for (Comment parent : commentParentList) {
            List<Comment> sons = new ArrayList<>();
            for (Comment son : commentSonList) {
                if (son.getFatherCommentId().equals(parent.getCommentId())) {
                    sons.add(son);
                }
                parent.setSonComments(sons);
            }
            result.add(parent);
        }
        return ResultUtil.success(result);
    }

    //返回某个评论的具体信息
    @PostMapping("/showOneComment")
    public Result<?> showOneComment(@RequestBody JSONObject jsonObject) {
        Long commentId = jsonObject.getLong("commentId");
        Comment comment = commentService.showOneComment(commentId);
        return ResultUtil.success(comment);
    }


    //用户对某道题目发表自己的评论
    @PostMapping("/sendComment")
    public Result<?> sendComment(@RequestBody JSONObject jsonObject) {
        Long questionId = jsonObject.getLong("questionId");
        Long userId = jsonObject.getLong("userId");
        String content = jsonObject.getString("comment");

        Comment comment = new Comment();
        comment.setQuestionId(questionId);
        comment.setUserId(userId);
        comment.setFatherCommentId(0L);
        comment.setReplytoUserId(0L);
        comment.setComment(content);
        int status = commentService.sendComment(comment);
        if (status == 1) {
            return ResultUtil.success(1);
        } else {
            return ResultUtil.defineFail(0, "评论失败！！！");
        }
    }


    //用户回复别人的评论
    @PostMapping("/replayComment")
    public Result<?> replayComment(@RequestBody JSONObject jsonObject) {
        Long questionId = jsonObject.getLong("questionId");
        Long userId = jsonObject.getLong("userId");
        String content = jsonObject.getString("comment");
        Long fatherCommentId = jsonObject.getLong("fatherCommentId");
        Long replytoUserId = jsonObject.getLong("replytoUserId");
        Comment comment = new Comment();
        comment.setQuestionId(questionId);
        comment.setUserId(userId);
        comment.setFatherCommentId(fatherCommentId);
        comment.setReplytoUserId(replytoUserId);
        comment.setComment(content);
        int status = commentService.sendComment(comment);
        if (status == 1) {
            return ResultUtil.success(1);
        } else {
            return ResultUtil.defineFail(0, "回复失败！！！");
        }
    }

    //删除自己发表的评论
    @PostMapping("/deleteComment")
    @MyTransactional
    public Result<?> deleteComment(@RequestBody JSONObject jsonObject) {
        Long commentId = jsonObject.getLong("commentId");
        int status = commentService.deletdComment(commentId);
        if (status == 1) {
            return ResultUtil.success(1);
        } else {
            return ResultUtil.defineFail(0, "删除失败！！！");
        }
    }

}
