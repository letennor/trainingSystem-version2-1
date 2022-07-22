package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.mapper.*;
import com.trainingsystem.trainingSystem.pojo.*;
import com.trainingsystem.trainingSystem.service.NormalTestService;
import com.trainingsystem.trainingSystem.service.QuestionRecordService;
import com.trainingsystem.trainingSystem.service.SpecialTestService;
import com.trainingsystem.trainingSystem.service.StudentTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class StudentTestServiceImpl extends ServiceImpl<StudentTestMapper, StudentTest> implements StudentTestService {

    @Autowired
    StudentTestMapper studentTestMapper;
    @Autowired
    NormalTestMapper normalTestMapper;
    @Autowired
    SpecialTestMapper specialTestMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    SpecialTestService specialTestService;
    @Autowired
    SpecialQuestionMapper specialQuestionMapper;
    @Autowired
    NormalTestService normalTestService;
    @Autowired
    QuestionRecordService questionRecordService;
    @Autowired
    TestSetServiceImpl testSetService;
    @Autowired
    UserMapper userMapper;

    //学生传入user_id，将该user_id学生的下一套试卷拿出来
    @Override
    public StudentTest getNextTest(Long userId) {

        QueryWrapper<StudentTest> studentTestQueryWrapper = new QueryWrapper<>();

        //通过user_id取得一套下次应该做的试卷
        //先取得test_type，然后再根据test_type看是调用TNormalTest接口的方法还是TSpecialTest接口的方法
        studentTestQueryWrapper.eq("user_id", userId);
        studentTestQueryWrapper.orderByAsc("test_type");
        studentTestQueryWrapper.orderByAsc("student_test_id");
        studentTestQueryWrapper.last("limit 1");

        StudentTest studentTest = studentTestMapper.selectOne(studentTestQueryWrapper);

        //如果是专项，重新取
        if (studentTest != null && studentTest.getTestType() == 0) {
            QueryWrapper<StudentTest> studentTestQueryWrapperSpecialTest = new QueryWrapper<>();
            studentTestQueryWrapperSpecialTest.eq("user_id", userId);
            studentTestQueryWrapperSpecialTest.eq("test_type", 0);
            studentTestQueryWrapperSpecialTest.orderByDesc("student_test_id");
            studentTestQueryWrapperSpecialTest.last("limit 1");
            studentTest = studentTestMapper.selectOne(studentTestQueryWrapperSpecialTest);
        }

        return studentTest;
    }


    //将学生某一知识点的某一难度的专项练习放入学生的t_student_test里面
    @Override
    @Transactional//事务管理
    public int insertSpecialTest(Long userId, List<String> knowledgePointList,
                                 int specialLevel) {
        /*
        步骤：
        1、先把所有符合该知识点和该难度的专项试卷全部找出来
        2、从里面随机选一套插入到TStudentTest里面
         */

        int insert = 0;
        List<SpecialTest> specialTests;
        //迭代知识点数组
        //如果两道题目错的是同一个知识点的话，有可能选到同一张专项试卷
        for (String knowledgePoint : knowledgePointList) {

            //符合条件的专项试卷
            specialTests = specialTestService.getSpecialTestByKnowledgePointAndLevel(knowledgePoint, specialLevel);
            SpecialTest randomSpecialTest = specialTestService.getRandomSpecialTest(specialTests);

            //将找到的试卷插入TStudentTest里面
            insert = insertTest(userId, randomSpecialTest, 0);
        }
        return insert;
    }


    //将某一张普通/专项试卷插入到TStudentTest里面
    @Override
    @Transactional//事务管理
    public int insertTest(Long userId, Object test, Integer testType) {

        //将找到的试卷插入TStudentTest里面
        StudentTest studentTest = new StudentTest();
        studentTest.setUserId(userId);
        studentTest.setTestType(testType);
        if (testType == 1) {
            NormalTest normalTest = (NormalTest) test;
            studentTest.setTestId(normalTest.getNormalId());
        }

        if (testType == 0) {
            SpecialTest specialTest = (SpecialTest) test;
            studentTest.setTestId(specialTest.getSpecialId());
        }


        int insert = studentTestMapper.insert(studentTest);
        return insert;
    }


    //模式3中系统放入用户错误最多的3个知识点的专项
    @Override
    public int insertSpecialTestBySystem(Long userId, List<String> knowledgePointList) {
        int randomLevel = 0;
        int randomIndex = 0;
        int insert = 0;

        Iterator<String> iterator = knowledgePointList.iterator();
        StudentTest studentTest;

        while (iterator.hasNext()) {
            randomLevel = (int) (Math.random() * 3 + 3);
            List<SpecialTest> specialTestList = specialTestService.getSpecialTestByKnowledgePointAndLevel(iterator.next(), randomLevel);
            if (specialTestList.size() != 0) {
                randomIndex = (int) (Math.random() * specialTestList.size() + 1);
                SpecialTest specialTest = specialTestList.get(randomIndex);

                //插入t_student_test
                studentTest = new StudentTest();
                studentTest.setUserId(userId);
                studentTest.setTestId(specialTest.getSpecialId());
                studentTest.setTestType(0);

                insert += studentTestMapper.insert(studentTest);
            }

        }


        return insert;
    }


    @Override//将用户接下来要写的一套试卷放入TStudentTest中
    @Transactional//事务管理
    public void insertNormalTestSet(Long userId, TestSet nextTestSet) {
        QueryWrapper<NormalTest> normalTestQueryWrapper = new QueryWrapper<>();
        normalTestQueryWrapper.eq("set_id", nextTestSet.getSetId());

        List<NormalTest> normalTests = normalTestMapper.selectList(normalTestQueryWrapper);

        Iterator<NormalTest> iterator = normalTests.iterator();

        NormalTest normalTest;

        while (iterator.hasNext()) {
            insertTest(userId, iterator.next(), 1);
        }

    }


    //通过testId删掉试卷
    @Override   
    @Transactional//事务管理
    public int deleteTestByTestId(Long testId) {
        QueryWrapper<StudentTest> studentTestQueryWrapper = new QueryWrapper<>();
        studentTestQueryWrapper.eq("test_id", testId);
        int i = studentTestMapper.delete(studentTestQueryWrapper);
        return i;
    }


    //根据test_id返回这套test的题数
    @Override
    public int getTestNumber(Long testId, Integer testType) {
        int questionNumber;

        if (testType == 1) {//普通试题
            questionNumber = 10;
        } else {//专项试题
            QueryWrapper<SpecialQuestion> wrapper = new QueryWrapper<>();
            wrapper.eq("special_id", testId);
            questionNumber = specialQuestionMapper.selectCount(wrapper);

        }

        return questionNumber;
    }


    //找出某一套套题学生训练计划里的所有普通试卷
    @Override
    public List<StudentTest> getStudentTestBySetId(Long setId) {
        List<StudentTest> studentTestBySetId = studentTestMapper.getStudentTestBySetId(setId);
        return studentTestBySetId;
    }


    //更换模式时，重新整理该学生的student_test表
    @Override
    public void modifyModel(Long userId) {
        //清楚该学生student_test原有的记录
        QueryWrapper<StudentTest> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        studentTestMapper.delete(wrapper);
    }


    //取得学生下一次要训练的情况
    @Override
    public Map<String, Object> getNextTrainingInfo(Long userId, Integer userLevel) {
        Map<String, Object> map = new HashMap<>();

        if (userLevel == 2 || userLevel == 3) {
            //返回知识点即可
            StudentTest nextTest = getNextTest(userId);
            if (nextTest.getTestType() == 1) {
                //找出对应的normalTest
                NormalTest normalTest = normalTestMapper.selectById(nextTest.getTestId());
                map.put("knowledgePointName", normalTest.getTestName());
                map.put("flag", 100);
            }

            if (nextTest.getTestType() == 0) {
                //找出对应的specialTest
                SpecialTest specialTest = specialTestMapper.selectById(nextTest.getTestId());
                map.put("testName", specialTest.getTestName());
                map.put("flag", 200);
            }

        }

        if (userLevel == 4) {
            //找出要做的试卷的名字，找出这张试卷里面包含的知识点
            StudentTest nextTest = getNextTest(userId);

            if (nextTest.getTestType() == 1) {
                map.put("flag", 300);
                NormalTest normalTest = normalTestMapper.selectById(nextTest.getTestId());
                map.put("testName", normalTest.getTestName());

                //获得知识点
                List<String> knowledgePointListInNormalTest = normalTestService.getKnowledgePointListInNormalTest(normalTest.getNormalId());
                map.put("knowlegePointListInNormalTest", knowledgePointListInNormalTest);

            }

            if (nextTest.getTestType() == 0) {
                map.put("flag", 400);
                SpecialTest specialTest = specialTestMapper.selectById(nextTest.getTestId());
                map.put("testName", specialTest.getTestName());
            }
        }

        if (userLevel == 5) {
            StudentTest nextTest = getNextTest(userId);

            if (nextTest != null) {
                SpecialTest specialTest = specialTestMapper.selectById(nextTest.getTestId());
                map.put("flag", 500);
                map.put("testName", specialTest.getTestName());
            }


            if (nextTest == null) {
                //此时表明专项试卷已经写完了，应该回到模式二，并且提高一个等级的难度
                map.put("flag", 600);
                //转换为模式2
                User user = new User();
                user.setUserId(userId);
                user.setUserLevel(4);
                userMapper.updateById(user);

                //此时模式3已经训练完成了，要从模式3跳到模式2了。看这个学生上一次的套题，做再高一个难度的
                //得到这个学生上一次练习的套题
                TestSet recentTestSetByUserId = testSetService.getRecentTestSetByUserId(userId);


                Integer lastSetLevel = recentTestSetByUserId.getSetLevel();
                Integer thisSetLevel = 0;
                if (lastSetLevel < 3) {
                    thisSetLevel = lastSetLevel + 1;
                    map.put("msg", "恭喜您又巩固了一遍薄弱的知识点，接下来，我们再做提升一个难度的套题把");
                    //放入套题
                    TestSet testSetBySetLevel = testSetService.getTestSetBySetLevel(thisSetLevel);
                    insertNormalTestSet(userId, testSetBySetLevel);

                }

                if (lastSetLevel == 3) {
                    thisSetLevel = lastSetLevel;
                    map.put("msg", "恭喜您又巩固了一遍薄弱的知识点，接下来，我们继续进行全面的训练吧");
                    //放入套题
                    TestSet testSetBySetLevel = testSetService.getTestSetBySetLevel(thisSetLevel);
                    insertNormalTestSet(userId, testSetBySetLevel);
                }


            }
        }

        return map;
    }


    //系统生成这个学生最弱的5个知识点专项
    @Override
    public Integer systemGenerate(Long userId) {
        //找出这个学生错误最多的3个知识点
        int i = 0;
        List<String> wrongKnowlegePointList = questionRecordService.getWrongKnowlegePointList(userId);
        Iterator<String> iterator = wrongKnowlegePointList.iterator();

        while (iterator.hasNext()) {
            List<SpecialTest> specialTestByKnowledgePointAndLevel = specialTestService.getSpecialTestByKnowledgePointAndLevel(iterator.next(), 2);
            SpecialTest randomSpecialTest = specialTestService.getRandomSpecialTest(specialTestByKnowledgePointAndLevel);
            i = insertTest(userId, randomSpecialTest, 0);
        }
        return i;
    }


    //得到一个用户所有的训练计划（只有专项）
    @Override
    public List<SpecialTest> getAllStudentTestByUserId(Long userId) {
        List<SpecialTest> allStudentTestByUserId = studentTestMapper.getAllStudentTestByUserId(userId);
        return allStudentTestByUserId;
    }


}
