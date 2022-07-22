package com.trainingsystem.trainingSystem.mapper;

import com.trainingsystem.trainingSystem.pojo.SpecialTest;
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
public interface SpecialTestMapper extends BaseMapper<SpecialTest> {

    //取得一张专项试卷的所有信息
    SpecialTest getSpecialTest(@Param("specialId") Long specialId);


    //根据老师选题库里的题的id组成的list和知识点，组成专向测试的套题
    SpecialTest makeSpecialTest(List<Long> questionIds, String knowledge);


    //找到某个老师的所有专项试卷
    List<SpecialTest> getSpecialTestByTeacherId(@Param("teacherId") Long teacherId);


}
