package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.CountOnlineNumber;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.*;
import com.trainingsystem.trainingSystem.service.*;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@RestController
public class StudentTestController {

    @Autowired
    StudentTestService studentTestService;
    @Autowired
    NormalTestService normalTestService;
    @Autowired
    SpecialTestService specialTestService;
    @Autowired
    TestSetService testSetService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionRecordService questionRecordService;


    //模式3，添加勾选的知识点的专项
    /*
        参数：
        {
        "userId":1,
        "knowledgePoint":["知识点7","知识点10","知识点11"],
        "specialLevel":2
        }

        返回：
        code
        200：插入成功
        0：插入失败
     */
    @PostMapping(value = "/model3/choose")//check
    @CountOnlineNumber
    @MyTransactional
    public Result<?> setSpecialTest(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        List<String> knowledgePointList = (List<String>) jsonObject.get("knowledgePoint");
        Integer specialLevel = jsonObject.getInteger("specialLevel");

        //将专项放入t_student_test里
        int i = studentTestService.insertSpecialTest(userId, knowledgePointList, specialLevel);

        if (i <= 0) {
            return ResultUtil.defineFail(0, "存储失败");
        }

        return ResultUtil.success(null);
    }


    //把模式2、3要做的试卷的题目返回
    /*
        参数：
        userId

        返回：
        List<NormalQuestion>或List<SpecialQuestion>
     */
    @PostMapping("/model/trainingstart")
    @CountOnlineNumber
    public Result<?> getStudentTest(@RequestBody JSONObject jsonObject) {

        Long userId = jsonObject.getLong("userId");

        //取得了该用户下次要做的试卷
        StudentTest nextTest = studentTestService.getNextTest(userId);

        //试卷已经没有了
        if (nextTest == null) {
            return ResultUtil.defineFail(0, "试卷已经没有了");
        }

        int testType = nextTest.getTestType();
        Long testId = nextTest.getTestId();

        Object test = new Object();

        //取出用户下次要做的试卷的所有信息
        if (testType == 1) {
            NormalTest normalTest = normalTestService.getNormalTest(testId);
            test = normalTest;
        }

        if (testType == 0) {
            SpecialTest specialTest = specialTestService.getSpecialTest(testId);
            test = specialTest;
        }

        return ResultUtil.success(test);
    }


    /*     模式一，根据UserId取到StudentTest中该学生下一套要做的题，对应的知识点，对应的url
     *
     * */
    @PostMapping("/model1/study")
    @MyTransactional
    public Result<?> getKnowledgeUrl(@RequestBody JSONObject jsonObject) {
        String url = null;
        Long userId = jsonObject.getLong("userId");

        //取到该用户下次要做的试卷
        StudentTest nextTest = studentTestService.getNextTest(userId);

        //试卷已经没有了,即将进入模式二，更改level和模式
        if (nextTest == null) {
            //将用户等级设置为4
            userService.changeUserLevel(userId, 4);
            //进入2模式,难度为1的试卷放进t_student_test里面，将setlevel为1的试卷放进去
            studentTestService.insertNormalTestSet(userId, testSetService.getTestSetBySetLevel(1));
            return ResultUtil.defineFail(0, "你已经完成了了模式一的任务！可以进入模式二啦！");
        }

        Object test = new Object();//下一次测试
        int testType = nextTest.getTestType();//下一次测试类型
        Long testId = nextTest.getTestId();//testid
        Map<String, Object> data = new HashMap<>();//Map向前端返回知识点url和知识点名称

        //取出用户下次要做的试卷的所有信息
        if (testType == 1) {//普通
            NormalTest normalTest = normalTestService.getNormalTest(testId);
            test = normalTest;
            //找出这套题对应第一道题的知识点的url
            data = normalTestService.getNormalKnowledgeUrl(normalTest.getNormalId());
        } else if (testType == 0) {//专项
            SpecialTest specialTest = specialTestService.getSpecialTest(testId);
            test = specialTest;
            //根据SpecialTest，返回该专项的知识点的url
            data = specialTestService.getSpecialKnowledgeUrl(specialTest.getSpecialId());
        }
        return ResultUtil.success(data);
    }


    //再做一套专项
    @RequestMapping("/getAnotherSpecialTest")
    @MyTransactional
    public Result<?> getAnotherSpecialTest(@RequestBody JSONObject jsonObject) {
        String knowledgePoint = jsonObject.getString("knowledgePoint");
        Integer specialTestLevel = jsonObject.getInteger("specialTestLevel");
        Long userId = jsonObject.getLong("userId");

        List<SpecialTest> specialTestByKnowledgePointAndLevel = specialTestService.getSpecialTestByKnowledgePointAndLevel(knowledgePoint, specialTestLevel);
        SpecialTest randomSpecialTest = specialTestService.getRandomSpecialTest(specialTestByKnowledgePointAndLevel);
        int i = studentTestService.insertTest(userId, randomSpecialTest, 0);

        return ResultUtil.success(i);
    }


    //模式三中系统生成3套这个学生最薄弱知识点的专项
    @RequestMapping("/model3/systemGenerate")
    public Result<?> systemGenerate(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        studentTestService.systemGenerate(userId);

        return ResultUtil.success(1);
    }


    //判断这个用户下一次应该做的事情是什么
    @RequestMapping("/getNextTrainingInfo")
    public Result<?> getNextTrainingInfo(@RequestBody JSONObject jsonObject) {
        Integer userLevel = jsonObject.getInteger("userLevel");
        Long userId = jsonObject.getLong("userId");

        Map<String, Object> nextTrainingInfo = studentTestService.getNextTrainingInfo(userId, userLevel);

        return ResultUtil.success(nextTrainingInfo);
    }


    //得到一个用户所有的训练计划（只有专项）
    @RequestMapping("/getStudentTestByUserId")
    public Result<?> getStudentTestByUserId(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        List<SpecialTest> allStudentTestByUserId = studentTestService.getAllStudentTestByUserId(userId);
        return ResultUtil.success(allStudentTestByUserId);
    }

}

