package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.mapper.QuestionImgMapper;
import com.trainingsystem.trainingSystem.pojo.QuestionImg;
import com.trainingsystem.trainingSystem.service.QuestionImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("db1")
public class QuestionImgServiceImpl extends ServiceImpl<QuestionImgMapper, QuestionImg> implements QuestionImgService {

    @Autowired
    QuestionImgMapper questionImgMapper;


    @Override
    public List<QuestionImg> getOneQuestionImgsById(Long questionId) {
        QueryWrapper<QuestionImg> wrapper = new QueryWrapper<>();
        wrapper.eq("question_id", questionId);
        List<QuestionImg> questionImgs = questionImgMapper.selectList(wrapper);
        return questionImgs;
    }
}
