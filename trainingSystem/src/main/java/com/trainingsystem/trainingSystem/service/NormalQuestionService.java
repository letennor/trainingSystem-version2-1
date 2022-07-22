package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.NormalQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
public interface NormalQuestionService extends IService<NormalQuestion> {


    //插入普通练习题目
    int insertNormalQuestion(Long normalId, List<Long> questionIdList);

}
