package com.trainingsystem.trainingSystem.mapper;

import com.trainingsystem.trainingSystem.pojo.NormalTest;
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
public interface NormalTestMapper extends BaseMapper<NormalTest> {

    //取得一张普通练习试卷的所有信息
    NormalTest getNormalTest(@Param("normalId") Long normalId);

    //获得一张普通试卷包含的所有知识点
    List<String> getKnowledgePointListInNormalTest(@Param("normalId") Long normalId);

}
