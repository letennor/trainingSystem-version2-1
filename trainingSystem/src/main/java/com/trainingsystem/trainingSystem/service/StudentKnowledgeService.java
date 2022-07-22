package com.trainingsystem.trainingSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trainingsystem.trainingSystem.pojo.StudentKnowledge;

import java.util.List;

public interface StudentKnowledgeService extends IService<StudentKnowledge> {

    //增加学生学习学习过的知识点
    int insertStudentKnowledgePoint(Long userId, Long knowledgePointId);

    //获得学生学过的所有知识点
    List<StudentKnowledge> getAllStudentKnowlegePoint(Long userId);

}
