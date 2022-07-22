package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.CountOnlineNumber;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.KnowledgePoint;
import com.trainingsystem.trainingSystem.service.KnowledgePointService;
import com.trainingsystem.trainingSystem.service.StudentKnowledgeService;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@RestController
public class KnowledgeController {

    @Autowired
    KnowledgePointService knowledgePointService;
    @Autowired
    StudentKnowledgeService studentKnowledgeService;


    @GetMapping("/study")
    public Result<?> studyPage() {
        List<KnowledgePoint> knowledgePointList = knowledgePointService.getAllKnowledgePoint();
        return ResultUtil.success(knowledgePointList);
    }


    @PostMapping("/tolearn")
    @CountOnlineNumber
    @MyTransactional
    public Result<?> toLearnKnowledge(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        Long knowledgePointId = jsonObject.getLong("knowledgePointId");
        studentKnowledgeService.insertStudentKnowledgePoint(userId, knowledgePointId);

        KnowledgePoint knowledgePoint = knowledgePointService.getOneKPById(knowledgePointId);
        return ResultUtil.success(knowledgePoint);

    }
}

