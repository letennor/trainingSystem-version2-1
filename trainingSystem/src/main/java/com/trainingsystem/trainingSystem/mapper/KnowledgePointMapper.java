package com.trainingsystem.trainingSystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trainingsystem.trainingSystem.pojo.KnowledgePoint;
import com.trainingsystem.trainingSystem.pojo.NormalQuestion;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgePointMapper  extends BaseMapper<KnowledgePoint> {



}
