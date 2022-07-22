package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.SpecialTest;
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
public interface SpecialTestService extends IService<SpecialTest> {

    //取得一张专项试卷的所有信息
    SpecialTest getSpecialTest(Long specialId);


    //根据老师选题库里的题的id组成的list和知识点，组成专向测试的套题
    SpecialTest makeSpecialTest(List<Long> questionIds, String knowledge);


    //找到某一个知识点和某一个specialLevel的所有专项试卷
    List<SpecialTest> getSpecialTestByKnowledgePointAndLevel(String knowledgePoint,
                                                             Integer specialLevel);


    //从专项list中随机拿出一套
    SpecialTest getRandomSpecialTest(List<SpecialTest> specialTestList);


    //生成一张专项试卷，插入t_special_test
    int insertSpecialTest(Long specialId, String testName, String knowledgePoint, Integer specialTestLevel,
                          Integer specialThreshold, Long teacherId);


    //返回该专项的知识点 的 url ———— 模式一用到
    Map getSpecialKnowledgeUrl(Long specialId);


    //取出所有的专项试题
    List<SpecialTest> getAllSpecialTest();

    //取得系统中所有专项的数量
    Integer getSpecialTestNumber();


    //删除一套专项试卷，前提是这套专项没有被放到student_test中
    int deleteOneSpecialTest(Long specialId);


    //查找某一个老师制作的所有专项试卷
    List<SpecialTest> getSpecialTestByTeacherId(Long teacherId);

    //取得一个老师制作的所有专项的试卷
    Integer getSpecialTestNumberByTeacherId(Long teacherId);


    //判断一张专项能否被删除
    boolean isSpecialTestChangable(Long specialId);


    //修改专项试卷
    int modifySpecialTest(Long specialId, String testName, Integer SpecialTestLevel);

}
