package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.mapper.NormalTestMapper;
import com.trainingsystem.trainingSystem.pojo.NormalTest;
import com.trainingsystem.trainingSystem.pojo.StudentTest;
import com.trainingsystem.trainingSystem.pojo.TestSet;
import com.trainingsystem.trainingSystem.mapper.TestSetMapper;
import com.trainingsystem.trainingSystem.service.StudentTestService;
import com.trainingsystem.trainingSystem.service.TestSetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
public class TestSetServiceImpl extends ServiceImpl<TestSetMapper, TestSet> implements TestSetService {

    @Autowired
    NormalTestMapper normalTestMapper;
    @Autowired
    TestSetMapper testSetMapper;
    @Autowired
    StudentTestService studentTestService;

    //得到某一张试卷所在的套题
    @Override
    public Long getSetIdByNormalId(Long normalId) {
        NormalTest normalTest = normalTestMapper.selectById(normalId);
        return normalTest.getSetId();
    }


    //通过setId取得某一套套题
    @Override
    public TestSet getTestSetBySetId(Long setId) {
        TestSet testSet = testSetMapper.selectById(setId);
        return testSet;
    }


    //随机取出某一难度的套题
    @Override
    public TestSet getTestSetBySetLevel(Integer setLevel) {
        QueryWrapper<TestSet> testSetQueryWrapper = new QueryWrapper<>();
        testSetQueryWrapper.eq("set_level", setLevel);


        List<TestSet> testSets = testSetMapper.selectList(testSetQueryWrapper);

        int random = (int) (Math.random() * testSets.size() + 1);

        return testSets.get(random - 1);
    }


    //创建一套套题
    @Override
    public int insertTestSet(Long setId, Integer setLevel, Integer threshold, Long teacherId) {
        TestSet testSet = new TestSet();

        testSet.setSetId(setId);
        testSet.setSetLevel(setLevel);
        testSet.setThreshold(threshold);
        testSet.setTeacherId(teacherId);
        return testSetMapper.insert(testSet);
    }


    //查询一个老师最近制作的套题的情况
    /*
    1. 没有制作过试卷，即setId是null
    2. 正在制作试卷，但是还没有完成，即setId有值，normalTestNumber有值，但是小于10
    3. 已经制作完成至少一张试卷，即setId有值，normalTestNumber有值，且为10
     */
    @Override
    public Map<String, Object> queryTeacherSetInfo(Long teacherId) {
        Map<String, Object> map = null;
        Long setId = null;
        int normalTestNumber = 0;
        //需要返回什么东西：setId，code
        //找出setId
        QueryWrapper<TestSet> testSetQueryWrapper = new QueryWrapper<>();
        testSetQueryWrapper.eq("teacher_id", teacherId);
        testSetQueryWrapper.orderByDesc("gmt_create");
        testSetQueryWrapper.last("limit 1");
        TestSet testSet = testSetMapper.selectOne(testSetQueryWrapper);

        if (testSet != null) {
            setId = testSet.getSetId();
        }

        if (setId != null) {
            //找出所有normalId
            QueryWrapper<NormalTest> normalTestQueryWrapper = new QueryWrapper<>();
            normalTestQueryWrapper.eq("set_id", setId);
            List<NormalTest> normalTestList = normalTestMapper.selectList(normalTestQueryWrapper);
            normalTestNumber = normalTestList.size();
        }


        map = new HashMap<>();
        map.put("setId", setId);
        map.put("normalTestNumber", normalTestNumber);

        return map;
    }


    //取得一个老师制定的所有套题
    @Override
    public List<TestSet> getTeacherTestSet(Long teacherId) {
        QueryWrapper<TestSet> testSetQueryWrapper = new QueryWrapper<>();
        testSetQueryWrapper.eq("teacher_id", teacherId);

        List<TestSet> testSetList = testSetMapper.selectList(testSetQueryWrapper);

        return testSetList;
    }


    //取得一个老师制作的套题的数量
    @Override
    public Integer getTeacherTestSetNumber(Long teacherId) {
        QueryWrapper<TestSet> testSetQueryWrapper = new QueryWrapper<>();
        testSetQueryWrapper.eq("teacher_id", teacherId);
        Integer integer = testSetMapper.selectCount(testSetQueryWrapper);
        return integer;
    }


    //判断套题是否可修改
    @Override
    public boolean isChangable(Long setId) {
        List<StudentTest> studentTestBySetId = studentTestService.getStudentTestBySetId(setId);

        if (studentTestBySetId.size() == 0) {
            return true;
        } else {
            return false;
        }
    }


    //修改套题及格线
    @Override
    public int modifyThreshold(Long setId, Integer threshold) {
        TestSet testSet = new TestSet();
        testSet.setSetId(setId);
        testSet.setThreshold(threshold);

        return testSetMapper.updateById(testSet);

    }


    //删除某一套套题
    @Override
    public int deleteTestSet(Long setId) {
        int i = testSetMapper.deleteById(setId);
        return i;
    }


    //取得所有套题
    @Override
    public List<TestSet> getAllTestSet() {
        List<TestSet> testSets = testSetMapper.selectList(null);

        return testSets;
    }


    //取得系统中所有套题的数量
    @Override
    public Integer getAllTestSetNumber() {
        Integer integer = testSetMapper.selectCount(null);
        return integer;
    }


    //取得一个学生最近做的一套套题
    @Override
    public TestSet getRecentTestSetByUserId(Long userId) {
        TestSet recentTestSetByUserId = testSetMapper.getRecentTestSetByUserId(userId);
        return recentTestSetByUserId;
    }
}
