package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.*;
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
public interface StudentTestService extends IService<StudentTest> {

    //学生传入user_id，将该user_id学生的下一套试卷拿出来
    StudentTest getNextTest(Long userId);


    //将学生错误题目对应的专项题目放入TStusentTest里面
    int insertSpecialTest(Long userId, List<String> knowledgePointList, int specialLevel);


    //将某一张专项试卷插入到TStudentTest里面
    int insertTest(Long userId, Object test, Integer testType);


    //模式3中系统放入用户错误最多的3个知识点的专项
    int insertSpecialTestBySystem(Long userId, List<String> knowledgePointList);


    //将用户接下来要写的一套试卷放入TStudentTest中
    void insertNormalTestSet(Long userId, TestSet nextTestSet);


    //通过testId删掉试卷
    int deleteTestByTestId(Long testId);


    //根据=testId,testType,找到这套题，返回这套test的题数
    int getTestNumber(Long testId, Integer testType);


    //找出某一套套题学生训练计划里的所有普通试卷
    List<StudentTest> getStudentTestBySetId(Long setId);


    //更换模式时，重新整理该学生的student_test表
    void modifyModel(Long userId);


    //取得学生下一次要训练的情况
    Map<String, Object> getNextTrainingInfo(Long userId, Integer userLevel);


    //系统生成这个学生最弱的5个知识点专项
    Integer systemGenerate(Long userId);


    //得到一个用户所有的训练计划（只有专项）
    List<SpecialTest> getAllStudentTestByUserId(Long userId);

}
