package com.trainingsystem.trainingSystem.mapper;

import com.trainingsystem.trainingSystem.pojo.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Repository
public interface QuestionMapper extends BaseMapper<Question> {

    //从题库里找某一知识点的所有题目
    List<Question> getAllQuestionByKnowledge(String knowledge);

    //从题库找某一知识点的所有题目
    List<Question> getAllQuestionByLevel(int level);

    //取得一套专项试卷的基本题目信息
    List<Question> getSpecialTestQuestion(@Param("specialId") Long specialId);


    //取得一套普通试卷的基本题目信息
    List<Question> getNormalTestQuestion(@Param("normalId") Long normalId);

}
