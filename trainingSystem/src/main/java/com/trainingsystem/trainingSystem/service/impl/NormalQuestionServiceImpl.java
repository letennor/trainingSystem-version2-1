package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.trainingsystem.trainingSystem.pojo.NormalQuestion;
import com.trainingsystem.trainingSystem.mapper.NormalQuestionMapper;
import com.trainingsystem.trainingSystem.service.NormalQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
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
public class NormalQuestionServiceImpl extends ServiceImpl<NormalQuestionMapper, NormalQuestion> implements NormalQuestionService {
    @Autowired
    NormalQuestionMapper normalQuestionMapper;

    //插入普通练习题目
    @Override
    public int insertNormalQuestion(Long normalId, List<Long> questionIdList) {
        NormalQuestion normalQuestion;

        Iterator<Long> iterator = questionIdList.iterator();
        int count = 1;
        int insert = 0;


        while (iterator.hasNext()) {
            normalQuestion = new NormalQuestion();
            normalQuestion.setNormalId(normalId);
            normalQuestion.setQuestionId(iterator.next());
            normalQuestion.setNormalQuestionNumber(count);
            count++;
            insert += normalQuestionMapper.insert(normalQuestion);
        }

        return insert;
    }
}
