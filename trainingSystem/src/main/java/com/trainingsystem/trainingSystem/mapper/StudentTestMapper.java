package com.trainingsystem.trainingSystem.mapper;

import com.trainingsystem.trainingSystem.pojo.SpecialTest;
import com.trainingsystem.trainingSystem.pojo.StudentTest;
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
public interface StudentTestMapper extends BaseMapper<StudentTest> {

    //找出某一套套题学生训练计划里的所有普通试卷
    List<StudentTest> getStudentTestBySetId(@Param("setId") Long setId);

    //得到一个用户所有的训练计划（只有专项）
    List<SpecialTest> getAllStudentTestByUserId(@Param("userId") Long userId);

}
