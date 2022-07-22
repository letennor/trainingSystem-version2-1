package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.mapper.KnowledgePointMapper;
import com.trainingsystem.trainingSystem.pojo.KnowledgePoint;
import com.trainingsystem.trainingSystem.service.KnowledgePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("db1")
public class KnowledgePointServiceImpl extends ServiceImpl<KnowledgePointMapper, KnowledgePoint> implements KnowledgePointService {

    @Autowired
    KnowledgePointMapper knowledgePointMapper;

    @Override
    public List<KnowledgePoint> getAllKnowledgePoint() {
        List<KnowledgePoint> knowledgePointList = knowledgePointMapper.selectList(null);
        return knowledgePointList;
    }

    @Override
    public KnowledgePoint getOneKPById(Long knowledgePointId) {
        KnowledgePoint knowledgePoint = knowledgePointMapper.selectById(knowledgePointId);
        return knowledgePoint;
    }
}