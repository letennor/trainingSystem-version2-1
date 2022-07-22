package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.SpecialTest;
import com.trainingsystem.trainingSystem.pojo.StudentKnowledge;
import com.trainingsystem.trainingSystem.pojo.User;
import com.trainingsystem.trainingSystem.service.SpecialTestService;
import com.trainingsystem.trainingSystem.service.StudentKnowledgeService;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentKnowledgeController {

    @Autowired
    StudentKnowledgeService studentKnowledgeService;
    @Autowired
    SpecialTestService specialTestService;

    //返回学生已学习知识点
    @PostMapping("/student/record/point")
    public Result<?> getStudentKnowledgePoint(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        List<StudentKnowledge> allStudentKnowlegePoint = studentKnowledgeService.getAllStudentKnowlegePoint(userId);

        return ResultUtil.success(allStudentKnowlegePoint);
    }


    //写入学生学习的知识点
    @PostMapping("/insertStudentKnowledgePoint")
    @MyTransactional
    public Result<?> insertStudentKnowlegePoint(@RequestBody JSONObject jsonObject) {

        Long userId = jsonObject.getLong("userId");
        Long knowledgePointId = jsonObject.getLong("knowledgePointId");
        int insert = studentKnowledgeService.insertStudentKnowledgePoint(userId, knowledgePointId);

        if (insert <= 0) {
            return ResultUtil.defineFail(0, "写入错误");
        } else {
            return ResultUtil.success(insert);
        }
    }


    //返回学生请求某一个知识点的小测试
    @PostMapping("/getKnowledgePointTest")
    public Result<?> getKnowledgePointTest(@RequestBody JSONObject jsonObject) {
        String knowledgePoint = (String) jsonObject.get("knowledgePoint");
        //取得所有符合条件的专项试卷
        List<SpecialTest> specialQuestionList = specialTestService.getSpecialTestByKnowledgePointAndLevel(knowledgePoint, 1);

        //取出一套专项试卷
        SpecialTest randomSpecialTest = specialTestService.getRandomSpecialTest(specialQuestionList);

        //取出该套试卷的所有信息
        SpecialTest specialTest = specialTestService.getSpecialTest(randomSpecialTest.getSpecialId());

        return ResultUtil.success(specialTest);

    }


}
