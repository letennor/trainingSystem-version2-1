package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.mapper.*;
import com.trainingsystem.trainingSystem.pojo.*;
import com.trainingsystem.trainingSystem.service.NormalTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Service
@DS("db1")
public class NormalTestServiceImpl extends ServiceImpl<NormalTestMapper, NormalTest> implements NormalTestService {
    @Autowired
    NormalTestMapper normalTestMapper;
    @Autowired
    TestSetMapper testSetMapper;
    @Autowired
    NormalQuestionMapper normalQuestionMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    KnowledgePointMapper knowledgePointMapper;


    /*老师设定的 难度等级、试题阈值、试题名字、试卷序号、限制时间都可以设定为参数，由前端传入
         再根据老师选题库里的题的id组成的list，组成普通测试的套题
       */
    @Override
    public NormalTest makeNormalTest(List<Long> questionIds) {
        //先在套题里创建一条记录，设置套题难度等级和套题阈值
        TestSet testSet = new TestSet();
        testSet.setSetLevel(1);//难度等级
        testSet.setThreshold(5);//阈值
        testSetMapper.insert(testSet);

        //组该套题的第一份试卷
        NormalTest normalTest = new NormalTest();
        normalTest.setTestName("高老师组成的一套试卷的名字");//试题名字
        normalTest.setSetId(testSet.getSetId());
        normalTest.setNormalTestNumber(1);//试卷序号，设置为参数
        normalTest.setLimitTime(50);//限制时间
        normalTestMapper.insert(normalTest);

        //将勾选的题目，加入normalQuestion表
        for (Long qid : questionIds) {
            NormalQuestion normalQuestion = new NormalQuestion();
            normalQuestion.setNormalId(normalTest.getNormalId());
            normalQuestion.setNormalQuestionNumber(normalTest.getNormalTestNumber());
            normalQuestion.setQuestionId(qid);
            normalQuestionMapper.insert(normalQuestion);
        }
        return normalTest;
    }


    //通过normalId取得试卷
    @Override
    public NormalTest getNormalTestById(Long normalId) {
        NormalTest normalTest = normalTestMapper.selectById(normalId);
        return normalTest;
    }


    //通过setId获取这套试题的所有试卷
    @Override
    public List<NormalTest> getNormalTestListBySetId(Long setId) {
        QueryWrapper<NormalTest> normalTestQueryWrapper = new QueryWrapper<>();
        normalTestQueryWrapper.eq("set_id", setId);


        List<NormalTest> normalTests = normalTestMapper.selectList(normalTestQueryWrapper);
        return normalTests;
    }


    //获取一套普通试卷的TestIdList
    @Override
    public List<Long> getTestIdList(List<NormalTest> normalTestList) {
        List<Long> normalIdList = new ArrayList<>();

        Iterator<NormalTest> iterator = normalTestList.iterator();

        while (iterator.hasNext()) {
            normalIdList.add(iterator.next().getNormalId());
        }

        return normalIdList;
    }


    //取得一张普通练习试卷的所有信息
    @Override
    public NormalTest getNormalTest(Long normalId) {
        NormalTest normalTest = normalTestMapper.getNormalTest(normalId);
        return normalTest;
    }


    //插入一张普通练习的试卷
    @Override
    public int insertNormalTest(Long normalId, Integer normalTestNumber, String testName, Integer limitTime, Long setId) {
        NormalTest normalTest = new NormalTest();
        normalTest.setNormalId(normalId);
        normalTest.setNormalTestNumber(normalTestNumber);
        normalTest.setTestName(testName);
        normalTest.setLimitTime(limitTime);
        normalTest.setSetId(setId);
        return normalTestMapper.insert(normalTest);
    }


    //返回该普通练习的第一道题的知识点 的 url ———— 模式一用到
    @Override
    public Map getNormalKnowledgeUrl(Long normalId) {

        //找到这份练习中的第一道题
        Map<String, Object> map = new HashMap<>();
        map.put("normal_id", normalId);
        map.put("normal_question_number", 1);

        List<NormalQuestion> normalQuestions = normalQuestionMapper.selectByMap(map);
        NormalQuestion normalQuestion = normalQuestions.get(0);
        //找这道题的知识点
        Question question = questionMapper.selectById(normalQuestion.getQuestionId());
        String knowledge = question.getKnowledgePoint();
        //找这道题知识点对应的url
        QueryWrapper<KnowledgePoint> wrapper = new QueryWrapper<>();
        wrapper.eq("knowledge_point", knowledge);
        KnowledgePoint knowledgePoint = knowledgePointMapper.selectOne(wrapper);

        String url = knowledgePoint.getUrl();


        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("knowledge", knowledge);

        return data;
    }


    // 返回表中所有普通试题
    @Override
    public List<NormalTest> getAllNormalTest() {
        List<NormalTest> normalTestList = normalTestMapper.selectList(null);
        return normalTestList;
    }


    //修改一张普通练习试卷
    @Override
    public int modifyNormalTest(Long normalId, String testName, Integer limitTime) {
        NormalTest normalTest = new NormalTest();
        normalTest.setNormalId(normalId);
        normalTest.setTestName(testName);
        normalTest.setLimitTime(limitTime);

        return normalTestMapper.updateById(normalTest);
    }


    //获得一张普通试卷包含的所有知识点
    @Override
    public List<String> getKnowledgePointListInNormalTest(Long normalId) {
        List<String> knowledgePointListInNormalTest = normalTestMapper.getKnowledgePointListInNormalTest(normalId);
        return knowledgePointListInNormalTest;
    }

}
