package com.trainingsystem.trainingSystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trainingsystem.trainingSystem.pojo.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper extends BaseMapper<Message> {

    //获得某个老师所有的提问
    List<Message> getTeacherMessage(@Param("teacherId") Long teacherId, @Param("isReplied") Integer isReplied);

    //根据学生Id 返回其所有留言
    List<Message> getOneStuAllMessage(@Param("userId") Long userId);


}
