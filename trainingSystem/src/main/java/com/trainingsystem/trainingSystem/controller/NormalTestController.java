package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.service.NormalQuestionService;
import com.trainingsystem.trainingSystem.service.NormalTestService;
import com.trainingsystem.trainingSystem.util.common.SnowFlakeGenerateIdWorker;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
public class NormalTestController {
    @Autowired
    NormalTestService normalTestService;
    @Autowired
    NormalQuestionService normalQuestionService;

    //插入普通练习试卷
    @RequestMapping("/createNormalTest")
    @MyTransactional
    public void insertNormalTest(@RequestBody JSONObject jsonObject) {

        Integer normalTestNumber = jsonObject.getInteger("normalTestNumber");
        String testName = jsonObject.getString("testName");
        Integer limitTime = jsonObject.getInteger("limitTime");
        Long setId = jsonObject.getLong("setId");
        List<String> questionIdListString = (List<String>) jsonObject.get("questionIdList");
        List<Long> questionIdList = questionIdListString.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        //雪花算法生成SpecialId
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker =
                new SnowFlakeGenerateIdWorker(0L, 0L);
        String idString = snowFlakeGenerateIdWorker.generateNextId();

        Long normalId = Long.parseLong(idString);

        normalTestService.insertNormalTest(normalId, normalTestNumber, testName, limitTime, setId);

        //插入题目
        normalQuestionService.insertNormalQuestion(normalId, questionIdList);

    }


    //修改普通练习试卷
    @RequestMapping("/modifyNormalTest")
    public Result<?> modifyNormalTest(@RequestBody JSONObject jsonObject) {
        Long normalId = jsonObject.getLong("normalId");
        String testName = jsonObject.getString("testName");
        Integer limitTime = jsonObject.getInteger("limitTime");

        int i = normalTestService.modifyNormalTest(normalId, testName, limitTime);
        return ResultUtil.success(i);
    }

}

