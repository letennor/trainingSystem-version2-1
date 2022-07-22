package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.mapper.KnowledgePointMapper;
import com.trainingsystem.trainingSystem.mapper.SpecialQuestionMapper;
import com.trainingsystem.trainingSystem.mapper.StudentTestMapper;
import com.trainingsystem.trainingSystem.pojo.KnowledgePoint;
import com.trainingsystem.trainingSystem.pojo.SpecialQuestion;
import com.trainingsystem.trainingSystem.pojo.SpecialTest;
import com.trainingsystem.trainingSystem.mapper.SpecialTestMapper;
import com.trainingsystem.trainingSystem.pojo.StudentTest;
import com.trainingsystem.trainingSystem.service.SpecialTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Service
@DS("db1")
public class SpecialTestServiceImpl extends ServiceImpl<SpecialTestMapper, SpecialTest> implements SpecialTestService {
    @Autowired
    SpecialTestMapper specialTestMapper;
    @Autowired
    SpecialQuestionMapper specialQuestionMapper;
    @Autowired
    StudentTestMapper studentTestMapper;
    @Autowired
    KnowledgePointMapper knowledgePointMapper;


    //取得一张专项试卷的所有信息
    @Override
    public SpecialTest getSpecialTest(Long specialId) {
        SpecialTest specialTest = specialTestMapper.getSpecialTest(specialId);
        return specialTest;
    }


    /*
    老师组一份专项试题
     试卷名、难度等级也可以由前端传入的参数而来
     */
    @Override
    public SpecialTest makeSpecialTest(List<Long> questionIds, String knowledge) {
        //先构成一套SpecialTest
        SpecialTest specialTest = new SpecialTest();
        specialTest.setTestName("高老师组成的一套专向试卷");//试卷名
        specialTest.setKnowledgePoint(knowledge);//知识点
        specialTest.setSpecialTestLevel(1);//难度等级
        specialTestMapper.insert(specialTest);
        //根据前端勾选的专向知识点的题目的id组成list来组题，
        //把勾的题放入对应的tSpecialQuestion表
        for (Long qid : questionIds) {
            int i = 1;
            SpecialQuestion specialQuestion = new SpecialQuestion();
            specialQuestion.setSpecialId(specialTest.getSpecialId());
            specialQuestion.setQuestionId(qid);
            specialQuestion.setSpecialQuestionNumber(i);
            i++;
            specialQuestionMapper.insert(specialQuestion);
        }
        return specialTest;
    }



    //从专项list中随机拿出一套
    @Override
    public SpecialTest getRandomSpecialTest(List<SpecialTest> specialTestList) {
        int specialTestNumber = specialTestList.size();//一共找到这么多符合条件的specialTest，需要随机选出一张。
        int random=(int)(Math.random()*specialTestNumber+1);//找到了某一张
        SpecialTest specialTest = specialTestList.get(random - 1/*索引的位置*/);
        return specialTest;
    }


    //生成一张专项试卷，插入t_special_test
    @Override
    public int insertSpecialTest(Long specialId, String testName, String knowledgePoint,
                                 Integer specialTestLevel, Integer specialThreshold, Long teacherId) {
        SpecialTest specialTest = new SpecialTest();
        specialTest.setSpecialId(specialId);
        specialTest.setTestName(testName);
        specialTest.setKnowledgePoint(knowledgePoint);
        specialTest.setSpecialTestLevel(specialTestLevel);
        specialTest.setSpecialThreshold(specialThreshold);
        specialTest.setTeacherId(teacherId);

        int insert = specialTestMapper.insert(specialTest);
        return insert;
    }


    //找到某一个知识点和某一个setlevel的所有专项试卷
    @Override
    public List<SpecialTest> getSpecialTestByKnowledgePointAndLevel(String knowledgePoint,
                                                                    Integer setLevel) {
        QueryWrapper<SpecialTest> specialTestQueryWrapper = new QueryWrapper<>();
        specialTestQueryWrapper.eq("knowledge_point", knowledgePoint);
        specialTestQueryWrapper.eq("special_test_level", setLevel);
        List<SpecialTest> specialTests = specialTestMapper.selectList(specialTestQueryWrapper);
        return specialTests;
    }


    //返回该专项的知识点 的 url ———— 模式一用到
    @Override
    public Map getSpecialKnowledgeUrl(Long specialId) {

        String url = null;
        String knowledge;
        SpecialTest specialTest = specialTestMapper.getSpecialTest(specialId);
        knowledge = specialTest.getKnowledgePoint();
        //找到这个知识点对用的url
        QueryWrapper<KnowledgePoint> wrapper = new QueryWrapper<>();
        wrapper.eq("knowledge_point",knowledge);
        KnowledgePoint knowledgePoint = knowledgePointMapper.selectOne(wrapper);
        url = knowledgePoint.getUrl();



        Map<String,Object> data = new HashMap<>();
        data.put("url",url);
        data.put("knowledge",knowledge);


        return data;
    }


    //取出所有的专项试题
    @Override
    public List<SpecialTest> getAllSpecialTest() {
        List<SpecialTest> specialTestList = specialTestMapper.selectList(null);
        return specialTestList;
    }


    //取得系统中所有专项的数量
    @Override
    public Integer getSpecialTestNumber() {
        Integer integer = specialTestMapper.selectCount(null);
        return integer;
    }


    //删除一套专项试卷，前提是这套专项没有被放到student_test中
    @Override
    public int deleteOneSpecialTest(Long specialId) {

        if(isSpecialTestChangable(specialId)){//没有，则删除
            int i = specialTestMapper.deleteById(specialId);//在special——test表中删除

            QueryWrapper<SpecialQuestion> wrapper1 = new QueryWrapper<>();//special_question中删除
            wrapper1.eq("special_id",specialId);
            specialQuestionMapper.delete(wrapper1);

            return i;
        }
        else {
            return 0;
        }
    }


    //查找某一个老师制作的所有专项试卷
    @Override
    public List<SpecialTest> getSpecialTestByTeacherId(Long teacherId) {
        List<SpecialTest> specialTestList = specialTestMapper.getSpecialTestByTeacherId(teacherId);
        return specialTestList;
    }


    //取得一个老师制作的所有专项的试卷
    @Override
    public Integer getSpecialTestNumberByTeacherId(Long teacherId) {
        QueryWrapper<SpecialTest> specialTestQueryWrapper = new QueryWrapper<>();
        specialTestQueryWrapper.eq("teacher_id", teacherId);
        Integer integer = specialTestMapper.selectCount(specialTestQueryWrapper);
        return integer;
    }


    //判断一张专项能否被删除
    @Override
    public boolean isSpecialTestChangable(Long specialId) {
        //排查这套专向是否被放入student——test
        QueryWrapper<StudentTest> wrapper = new QueryWrapper<>();
        wrapper.eq("test_id",specialId);
        List<StudentTest> studentTestList = studentTestMapper.selectList(wrapper);
        if (studentTestList.size() == 0){
            return true;
        }else {
            return false;
        }
    }


    //修改专项试卷
    @Override
    public int modifySpecialTest(Long specialId, String testName, Integer specialTestThreshold) {
        SpecialTest specialTest = new SpecialTest();
        specialTest.setSpecialId(specialId);
        specialTest.setTestName(testName);
        specialTest.setSpecialThreshold(specialTestThreshold);

        return specialTestMapper.updateById(specialTest);
    }


}
