package com.trainingsystem.trainingSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trainingsystem.trainingSystem.pojo.KnowledgePoint;

import java.util.List;

public interface KnowledgePointService extends IService<KnowledgePoint> {

    //返回所有的知识点信息
    List<KnowledgePoint> getAllKnowledgePoint();


    //根据知识点id返回这个知识点
    KnowledgePoint getOneKPById(Long knowledgePointId);
}
