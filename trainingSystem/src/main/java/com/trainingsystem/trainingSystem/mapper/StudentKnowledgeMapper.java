package com.trainingsystem.trainingSystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trainingsystem.trainingSystem.pojo.StudentKnowledge;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentKnowledgeMapper extends BaseMapper<StudentKnowledge> {


    //获得学生学过的所有知识点
    List<StudentKnowledge> getAllStudentKnowlegePoint(@Param("userId") Long userId);


}
