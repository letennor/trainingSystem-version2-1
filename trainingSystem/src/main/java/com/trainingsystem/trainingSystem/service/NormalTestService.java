package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.NormalTest;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
public interface NormalTestService extends IService<NormalTest> {

    //根据老师选题库里的题的id组成的list，组成普通测试的套题
    NormalTest makeNormalTest(List<Long> questionIds);

    //通过normalId取得试卷
    NormalTest getNormalTestById(Long normalId);

    //通过setId获取这套试题的所有试卷
    List<NormalTest> getNormalTestListBySetId(Long setId);

    //获取一套试卷的TestIdList
    List<Long> getTestIdList(List<NormalTest> normalTestList);

    //取得一张普通练习试卷的所有信息
    NormalTest getNormalTest(Long normalId);

    //插入一张普通练习的试卷
    int insertNormalTest(Long normalId, Integer normalTestNumber, String testName,
                         Integer limitTime, Long setId);


    //返回该普通练习的第一道题的知识点 的 url ———— 模式一用到
    Map getNormalKnowledgeUrl(Long normalId);


    //返回表中所有普通试题
    List<NormalTest> getAllNormalTest();


    //修改一张普通练习试卷
    int modifyNormalTest(Long normalId, String testName, Integer limitTime);

    //获得一张普通试卷包含的所有知识点
    List<String> getKnowledgePointListInNormalTest(Long normalId);

}
