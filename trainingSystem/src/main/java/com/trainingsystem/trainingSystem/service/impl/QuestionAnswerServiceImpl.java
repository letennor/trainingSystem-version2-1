package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.pojo.QuestionAnswer;
import com.trainingsystem.trainingSystem.mapper.QuestionAnswerMapper;
import com.trainingsystem.trainingSystem.service.QuestionAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class QuestionAnswerServiceImpl extends ServiceImpl<QuestionAnswerMapper, QuestionAnswer> implements QuestionAnswerService {

    @Autowired
    QuestionAnswerMapper questionAnswerMapper;

    //返回一张试卷题目的答案
    @Override
    public List<QuestionAnswer> getQuestionAnswer(List<Long> questionIdList) {
        QueryWrapper<QuestionAnswer> questionAnswerQueryWrapper = new QueryWrapper<>();

        questionAnswerQueryWrapper.in("question_id", questionIdList);


        List<QuestionAnswer> questionAnswers = questionAnswerMapper.selectList(questionAnswerQueryWrapper);
        return questionAnswers;
    }


    //返回一道题目的答案
    @Override
    public List<QuestionAnswer> getOneQuestionAnswer(Long questionId) {
        QueryWrapper<QuestionAnswer> wrapper = new QueryWrapper<>();
        wrapper.eq("question_id", questionId);
        List<QuestionAnswer> questionAnswerList = questionAnswerMapper.selectList(wrapper);
        return questionAnswerList;
    }


    //插入一道题的答案
    @Override
    public int insertQuestionAnswer(Long questionId, Integer language, String standardAnswer) {
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setQuestionId(questionId);
        questionAnswer.setStandardAnswer(standardAnswer);
        questionAnswer.setLanguage(language);

        return questionAnswerMapper.insert(questionAnswer);

    }


    //修改一道题的答案
    @Override
    public int modifyQuestionAnswer(Long questionId, Integer language, String standardAnswer) {
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setLanguage(language);
        questionAnswer.setStandardAnswer(standardAnswer);

        QueryWrapper<QuestionAnswer> questionAnswerQueryWrapper = new QueryWrapper<>();
        questionAnswerQueryWrapper.eq("question_id", questionId);
        questionAnswerQueryWrapper.eq("language", language);
        int status = questionAnswerMapper.update(questionAnswer, questionAnswerQueryWrapper);
        //如果没找到，则新增一个答案
        if (status == 0) {
            QuestionAnswer newQuestionAnswer = new QuestionAnswer();
            newQuestionAnswer.setQuestionId(questionId);
            newQuestionAnswer.setLanguage(language);
            newQuestionAnswer.setStandardAnswer(standardAnswer);
            questionAnswerMapper.insert(newQuestionAnswer);
        }

        return status;
    }
}
