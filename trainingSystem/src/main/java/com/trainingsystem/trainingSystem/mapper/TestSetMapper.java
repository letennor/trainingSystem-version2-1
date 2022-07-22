package com.trainingsystem.trainingSystem.mapper;

import com.trainingsystem.trainingSystem.pojo.TestSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Repository
public interface TestSetMapper extends BaseMapper<TestSet> {

    //取得一个学生最近做的一套套题
    TestSet getRecentTestSetByUserId(@Param("userId") Long userId);

}
