package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.mapper.*;
import com.trainingsystem.trainingSystem.pojo.*;
import com.trainingsystem.trainingSystem.service.NormalTestService;
import com.trainingsystem.trainingSystem.service.TestResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.service.TestSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Service
@DS("db1")
public class TestResultServiceImpl extends ServiceImpl<TestResultMapper, TestResult> implements TestResultService {

    @Autowired
    TestResultMapper testResultMapper;
    @Autowired
    NormalTestMapper normalTestMapper;
    @Autowired
    SpecialTestMapper specialTestMapper;
    @Autowired
    TestSetMapper testSetMapper;
    @Autowired
    NormalTestService normalTestService;
    @Autowired
    TestSetService testSetService;
    @Autowired
    SpecialQuestionMapper specialQuestionMapper;


    //统计学生一次练习的正确的题目的数量并返回
    @Override
    public int getScore(List<QuestionRecord> questionRecordList) {
        int number = 0;

        for (QuestionRecord questionRecord : questionRecordList) {
            if (questionRecord.getSituation() == 1) {
                number += questionRecord.getSituation();
            }
        }

        return number;
    }


    //统计学生一次普通/专项练习的状态，并返回
    @Override
    public int getTestStatus(Long testId, Integer score, Integer testType) {
        int status = 0;

        //判断普通练习
        if (testType == 1) {
            NormalTest normalTest = normalTestMapper.selectById(testId);
            TestSet testSet = testSetMapper.selectById(normalTest.getSetId());
            int threshold = testSet.getThreshold();

            if (score >= threshold) {
                status = 1;
            }
        }

        //判断专项练习
        if (testType == 0) {
            SpecialTest specialTest = specialTestMapper.selectById(testId);
            //得到专项练习的阈值
            int special_threshold = specialTest.getSpecialThreshold();

            if (score >= special_threshold) {
                status = 1;
            }
        }

        return status;
    }


    //将学生完成的一普通/专项练习张试卷写入t_test_result里面
    @Override
    public int insertTestResult(Long userId, Long testId, Integer testType,
                                Integer score, Integer status) {
        TestResult testResult = new TestResult();
        testResult.setUserId(userId);
        testResult.setTestId(testId);
        testResult.setScore(score);
        testResult.setStatus(status);
        testResult.setTestType(testType);
        int number;
        if (testType == 1) {
            number = 10;
        } else {
            QueryWrapper<SpecialQuestion> wrapper = new QueryWrapper<>();
            wrapper.eq("special_id", testId);
            number = specialQuestionMapper.selectCount(wrapper);
        }
        testResult.setQuestionNumber(number);
        int insert = testResultMapper.insert(testResult);
        return insert;
    }


    //取得一个学生所有的TestResult
    @Override
    public List<TestResult> getTestResultByUserId(Long userId) {
        List<TestResult> testResultByUserId = testResultMapper.getTestResultByUserId(userId);
        return testResultByUserId;
    }


    //得到学生某一套套题的平均成绩，需要把这个学生传进来
    @Override
    public Map<String, Object> getNormalTestAverage(Long setId, Long userId) {
        Map<String, Object> map = new HashMap<>();

        QueryWrapper<TestResult> testResultQueryWrapper = new QueryWrapper<>();
        //找到一套套题的所有试卷
        List<NormalTest> normalTestListBySetId = normalTestService.getNormalTestListBySetId(setId);

        //找到testIdList
        List<Long> testIdList = normalTestService.getTestIdList(normalTestListBySetId);

        //找到这个人最近做的10张普通练习试卷
        testResultQueryWrapper.in("test_id", testIdList);
        testResultQueryWrapper.eq("user_id", userId);
        testResultQueryWrapper.eq("test_type", 1);
        testResultQueryWrapper.orderByDesc("gmt_create");
        testResultQueryWrapper.last("limit 10");
        List<TestResult> testResults = testResultMapper.selectList(testResultQueryWrapper);

        //计算平均分
        int sum = 0;

        //得到超过3次的错误知识点
        List<String> wrongNumberOver3 = getWrongNumberOver3(userId, testResults.get(9).getResultId(), testResults.get(0).getResultId());
        map.put("wrongNumberOver3", wrongNumberOver3);
        Iterator<TestResult> iterator = testResults.iterator();

        while (iterator.hasNext()) {
            sum += iterator.next().getScore();
        }

        double avg = sum / 10.0;
        map.put("avg", avg);
        return map;
    }


    //取得某一个学生所有普通练习的试题的情况
    @Override
    public List<TestResult> getNormalTestResultByUserId(Long userId) {
        return testResultMapper.getNormalTestResultByUserId(userId);
    }


    //取得某一个学生所有专项练习的试题的情况
    @Override
    public List<TestResult> getSpecialTestResultByUserId(Long userId) {
        return testResultMapper.getSpecialTestResultByUserId(userId);
    }


    //找出在做第一张普通练习和最后一张普通练习之间所有的题目记录中错误超过3次的所有知识点
    @Override
    public List<String> getWrongNumberOver3(Long userId, Long firstId, Long lastId) {
        List<String> wrongNumberOver3 = testResultMapper.getWrongNumberOver3(userId, firstId, lastId);
        return wrongNumberOver3;
    }


    //得到一个人做模式1的时候，做专项错误次数
    @Override
    public int getWrongTimesInModel1(Long userId) {
        int wrongTimesInModel1 = testResultMapper.getWrongTimesInModel1(userId);
        return wrongTimesInModel1;
    }

}
