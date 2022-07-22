package com.trainingsystem.trainingSystem.mapper;

import com.trainingsystem.trainingSystem.pojo.TestResult;
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
public interface TestResultMapper extends BaseMapper<TestResult> {


    //取得某一个学生的所有试题情况
    List<TestResult> getTestResultByUserId(@Param("userId") Long userId);


    //取得某一个学生所有普通练习的试题的情况
    List<TestResult> getNormalTestResultByUserId(@Param("userId") Long userId);


    //取得某一个学生所有专项练习的试题的情况
    List<TestResult> getSpecialTestResultByUserId(@Param("userId") Long userId);

    //找出在做第一张普通练习和最后一张普通练习之间所有的题目记录中错误超过3次的所有知识点
    List<String> getWrongNumberOver3(@Param("userId") Long userId, @Param("firstId") Long firstId, @Param("lastId") Long lastId);


    //得到一个人做模式1的时候，做专项错误次数
    int getWrongTimesInModel1(@Param("userId") Long userId);

}
