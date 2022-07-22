package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.CountOnlineNumber;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.SpecialTest;
import com.trainingsystem.trainingSystem.service.SpecialQuestionService;
import com.trainingsystem.trainingSystem.service.SpecialTestService;
import com.trainingsystem.trainingSystem.util.common.SnowFlakeGenerateIdWorker;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@RestController
public class SpecialTestController {
    @Autowired
    SpecialTestService specialTestService;
    @Autowired
    SpecialQuestionService specialQuestionService;

    //生成一套专项练习试卷
    @RequestMapping("/specialQuestion/receive")
    @CountOnlineNumber
    @MyTransactional
    public void receiveSpecialQuestionId(@RequestBody JSONObject jsonObject) {

        String userIdString = jsonObject.getString("userId");
        Long userId = Long.parseLong(userIdString);
        List<String> questionIdListString = (List<String>) jsonObject.get("questionIdList");
        //List<String>转List<Long>
        List<Long> questionIdList = questionIdListString.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        String testName = jsonObject.getString("testName");
        List<String> knowledgePointList = (List<String>) jsonObject.get("knowledgePointList");
        Integer specialLevel = jsonObject.getInteger("specialLevel");
        Integer threshold = jsonObject.getInteger("threshold");


        //雪花算法生成SpecialId
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker =
                new SnowFlakeGenerateIdWorker(0L, 0L);
        String idString = snowFlakeGenerateIdWorker.generateNextId();
        Long specialId = Long.parseLong(idString);


        //创建专项试卷
        specialTestService.insertSpecialTest(specialId, testName, knowledgePointList.get(0),
                specialLevel, threshold, userId);

        //将题目插入specialQuestion
        specialQuestionService.insertSpecialQuestion(specialId, questionIdList);
    }


    //删除一套专项试卷，前提是这套试卷没有被放在student——test表
    @PostMapping("/deleteOneSpecialTest")
    @MyTransactional
    public Result<?> deleteOneSpecialTest(@RequestBody JSONObject jsonObject) {
        Long specialId = jsonObject.getLong("specialId");
        int status = specialTestService.deleteOneSpecialTest(specialId);
        if (status == 1) {
            return ResultUtil.success(1);
        } else {
            return ResultUtil.defineFail(0, "专项试卷删除失败！！！");
        }

    }


    //找出某一个老师的所有专项试卷
    @RequestMapping("/getTeacherSpecialTest")
    public Result<?> getTeacherSpecialTest(@RequestBody JSONObject jsonObject) {
        Long teacherId = jsonObject.getLong("teacherId");
        List<SpecialTest> specialTestByTeacherId = specialTestService.getSpecialTestByTeacherId(teacherId);

        return ResultUtil.success(specialTestByTeacherId);
    }


    //判断一张专项能否被修改
    @RequestMapping("/isSpecialTestChangable")
    public Result<?> isSpecialTestChangable(@RequestBody JSONObject jsonObject) {
        Long specialId = jsonObject.getLong("specialId");
        boolean specialTestChangable = specialTestService.isSpecialTestChangable(specialId);

        if (specialTestChangable) {
            return ResultUtil.success(1);
        } else {
            return ResultUtil.defineFail(0, "专项无法被修改");
        }
    }


    //修改专项试卷
    @RequestMapping("/modifySpecialTest")
    public Result<?> modifySpecialTest(@RequestBody JSONObject jsonObject) {
        Long specialId = jsonObject.getLong("specialId");
        String testName = jsonObject.getString("testName");
        Integer specialTestThreshold = jsonObject.getInteger("specialTestThreshold");

        int i = specialTestService.modifySpecialTest(specialId, testName, specialTestThreshold);

        return ResultUtil.success(i);
    }

}

