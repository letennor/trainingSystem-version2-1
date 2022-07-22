package com.trainingsystem.trainingSystem.mapper;

import com.trainingsystem.trainingSystem.pojo.QuestionRecord;
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
public interface QuestionRecordMapper extends BaseMapper<QuestionRecord> {

    //取得某一个学生的某一次试卷的所有题目信息
    List<QuestionRecord> getQuestionRecordByTestId(@Param("userId") Long userId,
                                                   @Param("testId") Long testId);


    //统计某一用户question_record的所有信息，找出错得最多的三个知识点
    List<String> getWrongKnowlegePointList(@Param("userId") Long userId);

}
