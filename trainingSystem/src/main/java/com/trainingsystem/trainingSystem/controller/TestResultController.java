package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.mapper.TestResultMapper;
import com.trainingsystem.trainingSystem.pojo.TestResult;
import com.trainingsystem.trainingSystem.pojo.User;
import com.trainingsystem.trainingSystem.service.TestResultService;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@RestController
public class TestResultController {


    @Autowired
    TestResultService testResultService;
    @Autowired
    TestResultMapper testResultMapper;


    //取得某一个人得所有试卷记录
    @RequestMapping("/student/record")//check
    public Result<?> getStudyInfo(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        List<TestResult> testResultByUserId = testResultService.getTestResultByUserId(userId);
        return ResultUtil.success(testResultByUserId);
    }

    //取得一个人的所有普通试卷（用来判断模式3是否应该开启）
    @RequestMapping("/getUserNormalTest")
    public Result<?> getUserNormalTest(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");

        QueryWrapper<TestResult> testResultQueryWrapper = new QueryWrapper<>();
        testResultQueryWrapper.eq("user_id", userId);
        testResultQueryWrapper.eq("test_type", 1);
        Integer integer = testResultMapper.selectCount(testResultQueryWrapper);

        return ResultUtil.success(integer);
    }

}

