package com.trainingsystem.trainingSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trainingsystem.trainingSystem.pojo.QuestionImg;

import java.util.List;

public interface QuestionImgService extends IService<QuestionImg> {


    //根据题目id返回这道题包含的图片信息，
    List<QuestionImg> getOneQuestionImgsById(Long questionId);
}
