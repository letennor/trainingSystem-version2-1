package com.trainingsystem.trainingSystem.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.mapper.StudentKnowledgeMapper;
import com.trainingsystem.trainingSystem.pojo.StudentKnowledge;
import com.trainingsystem.trainingSystem.service.StudentKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("db1")
public class StudentKnowledgeImpl  extends ServiceImpl<StudentKnowledgeMapper, StudentKnowledge> implements StudentKnowledgeService {
    @Autowired
    StudentKnowledgeMapper studentKnowledgeMapper;

    //增加学生学习学习过的知识点
    @Override
    public int insertStudentKnowledgePoint(Long userId, Long knowledgePointId) {
        StudentKnowledge studentKnowledge = new StudentKnowledge();
        studentKnowledge.setUserId(userId);
        studentKnowledge.setKnowledgePointId(knowledgePointId);

        int insert = studentKnowledgeMapper.insert(studentKnowledge);
        return insert;
    }


    //获得学生学过的所有知识点
    @Override
    public List<StudentKnowledge> getAllStudentKnowlegePoint(Long userId) {
        List<StudentKnowledge> allStudentKnowlegePoint = studentKnowledgeMapper.getAllStudentKnowlegePoint(userId);
        return allStudentKnowlegePoint;
    }
}
