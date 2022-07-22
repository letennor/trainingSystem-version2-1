package com.trainingsystem.trainingSystem.service;


import com.trainingsystem.trainingSystem.pojo.Question;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
public interface QuestionService extends IService<Question> {

    //从题库里找所有题目
    List<Question> getQuestion();

    //得到题库中所有题目的数量
    Integer getQuestionNumber();


    //从题库中找符合条件的题目
    List<Question> getQuestion(List<String> knowlegePoint, List<Integer> questionLevel,
                               List<Integer> questionType);


    //随机取10道填空题，难度为1，题目类型为c和数据结构和算法
    List<Question> getEnterTest();


    //插入1条题目
    int insertQuestion(Long questionId, String questionTitle, String knowledgePoint, Integer questionLevel,
                       Integer questionForm, Integer questionType, Long teacherId);


    //根据题目id中返回这道题
    Question getOneQuestionById(Long questionId);


    //判断某一条题目是否能够修改
    boolean isQuestionChangable(Long questionId);


    //根据题目id删除一道题，若能删除，
    int deleteOneQuestionById(Long questionId);


    //取得某一个老师做的所有题目
    List<Question> getQuestionByTeacherId(Long teacherId);

    //取得一个老师制作的所有题目的数量
    Integer getQuestionNumberByTeacherId(Long teacherId);


    //修改一条题目
    int modifyQuestion(Long questionId, String questionTitle, String knowledgePoint,
                       Integer questionLevel, Integer questionType);


    //取得某一张专项试卷的基本信息
    List<Question> getSpecialTestQuestion(Long specialId);


    //取得一套普通试卷的基本题目信息
    List<Question> getNormalTestQuestion(Long normalId);

    //取得一条题目所有的参数类型
    List<Integer> getArgumentType(Long qustionId);

}
