package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.QuestionAnswer;
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
public interface QuestionAnswerService extends IService<QuestionAnswer> {

    //返回一套题目的答案
    List<QuestionAnswer> getQuestionAnswer(List<Long> questionIdList);


    //返回一道题目的答案
    List<QuestionAnswer> getOneQuestionAnswer(Long questionId);


    //插入一道题的答案
    int insertQuestionAnswer(Long questionId, Integer language, String standardAnswer);


    //修改一道题的答案
    int modifyQuestionAnswer(Long questionId, Integer language, String standardAnswer);


}
