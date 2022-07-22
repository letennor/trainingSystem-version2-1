package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.NormalTest;
import com.trainingsystem.trainingSystem.pojo.QuestionRecord;
import com.trainingsystem.trainingSystem.pojo.TestResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
public interface TestResultService extends IService<TestResult> {


    //统计学生一次普通/专项练习的正确的题目的数量并返回
    int getScore(List<QuestionRecord> questionRecordList);


    //统计学生一次普通/专项练习的状态，并返回
    int getTestStatus(Long testId, Integer score, Integer testType);


    //将学生完成的一张普通/专项练习试卷写入t_test_result里面
    int insertTestResult(Long userId, Long testId, Integer testType, Integer score, Integer status);


    //取得一个学生所有的TTestResult
    List<TestResult> getTestResultByUserId(Long userId);

    //得到学生某一套套题的平均成绩
    Map<String, Object> getNormalTestAverage(Long setId, Long userId);


    //取得某一个学生所有普通练习的试题的情况
    List<TestResult> getNormalTestResultByUserId(Long userId);


    //取得某一个学生所有专项练习的试题的情况
    List<TestResult> getSpecialTestResultByUserId(Long userId);


    //找出在做第一张普通练习和最后一张普通练习之间所有的题目记录中错误超过3次的所有知识点
    List<String> getWrongNumberOver3(Long userId, Long firstId, Long lastId);

    //得到一个人做模式1的时候，做专项错误次数
    int getWrongTimesInModel1(Long userId);

}
