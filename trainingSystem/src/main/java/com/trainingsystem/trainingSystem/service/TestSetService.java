package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.TestSet;
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
public interface TestSetService extends IService<TestSet> {

    //得到某一张试卷所在的套题的id
    Long getSetIdByNormalId(Long normalId);


    //通过setId取得某一套套题
    TestSet getTestSetBySetId(Long setId);


    //寻找某一难度的套题
    TestSet getTestSetBySetLevel(Integer setLevel);


    //创建一套套题
    int insertTestSet(Long setId, Integer setLevel, Integer threshold, Long teacherIs);


    //查询一个老师最近制作的套题的情况
    Map<String, Object> queryTeacherSetInfo(Long teacherId);


    //取得一个老师制定的所有套题
    List<TestSet> getTeacherTestSet(Long teacherId);

    //取得一个老师制作的套题的数量
    Integer getTeacherTestSetNumber(Long teacherId);


    //判断套题是否可修改
    boolean isChangable(Long setId);


    //修改套题及格线
    int modifyThreshold(Long setId, Integer threshold);


    //删除某一套套题
    int deleteTestSet(Long setId);


    //取得所有套题
    List<TestSet> getAllTestSet();

    //取得系统中所有套题的数量
    Integer getAllTestSetNumber();


    //取得一个学生最近做的一套套题
    TestSet getRecentTestSetByUserId(Long userId);

}
