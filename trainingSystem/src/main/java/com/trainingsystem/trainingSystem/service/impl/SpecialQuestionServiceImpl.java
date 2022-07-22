package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.pojo.SpecialQuestion;
import com.trainingsystem.trainingSystem.mapper.SpecialQuestionMapper;
import com.trainingsystem.trainingSystem.pojo.SpecialTest;
import com.trainingsystem.trainingSystem.service.SpecialQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Service
@DS("db1")
public class SpecialQuestionServiceImpl extends ServiceImpl<SpecialQuestionMapper, SpecialQuestion> implements SpecialQuestionService {

    @Autowired
    SpecialQuestionMapper specialQuestionMapper;


    //把老师选的题目放入t_special_question里
    @Override
    public int insertSpecialQuestion(Long specialId, List<Long> questionIdList) {
        SpecialQuestion specialQuestion = null;
        Iterator<Long> iterator = questionIdList.iterator();
        int count = 1;
        int insert = 0;
        while (iterator.hasNext()){
            specialQuestion = new SpecialQuestion();
            specialQuestion.setQuestionId(iterator.next());
            specialQuestion.setSpecialQuestionNumber(count);
            specialQuestion.setSpecialId(specialId);
            insert += specialQuestionMapper.insert(specialQuestion);
            count++;
        }

        return insert;

    }
}
