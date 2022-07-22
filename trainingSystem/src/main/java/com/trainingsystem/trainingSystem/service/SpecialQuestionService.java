package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.SpecialQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trainingsystem.trainingSystem.pojo.SpecialTest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
public interface SpecialQuestionService extends IService<SpecialQuestion> {


    //把老师选的题目放入t_special_question里
    int insertSpecialQuestion(Long specialId, List<Long> questionIdList);

    //取得一张专项试卷的基本题目信息


}
