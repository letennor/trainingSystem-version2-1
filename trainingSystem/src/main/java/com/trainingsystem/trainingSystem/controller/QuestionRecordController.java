package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.CountOnlineNumber;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.*;
import com.trainingsystem.trainingSystem.service.*;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@RestController
public class QuestionRecordController {
    @Autowired
    QuestionRecordService questionRecordService;
    @Autowired
    TestResultService testResultService;
    @Autowired
    StudentTestService studentTestService;
    @Autowired
    NormalTestService normalTestService;
    @Autowired
    TestSetService testSetService;
    @Autowired
    SpecialTestService specialTestService;
    @Autowired
    UserService userService;


    //取得某一个学生的某一次试卷的所有题目信息
    @RequestMapping("/student/testresult")
    @CountOnlineNumber
    public Result<?> getQuestionRecord(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        Long testId = jsonObject.getLong("testId");
        List<QuestionRecord> questionRecordByTestId = questionRecordService.getQuestionRecordByTestId(userId, testId);
        return ResultUtil.success(questionRecordByTestId);
    }


    //将水平测试的结果进行处理，将用户进行定级
    @PostMapping("/train/initLevel")
    @CountOnlineNumber
    @Transactional
    public Result<?> checkEnterTest(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        List<String> userAnswerList = (List<String>) jsonObject.get("userAnswer");
        List<Long> questionIdList = JSONArray.parseArray(jsonObject.get("questionId").toString(), Long.class);
        List<QuestionRecord> questionRecordList = questionRecordService.transferToQuestionRecord(userId, null,
                null, questionIdList, null, userAnswerList);

        int userLevel = questionRecordService.checkEnterTest(userId, questionRecordList);

        if (userLevel == 2) {
            TestSet testSet = testSetService.getTestSetBySetId(954811036794355712L);//模式一，level2
            studentTestService.insertNormalTestSet(userId, testSet);
        } else if (userLevel == 3) {
            TestSet testSet = testSetService.getTestSetBySetId(954825154939060224L);//模式一，level3
            studentTestService.insertNormalTestSet(userId, testSet);
        } else if (userLevel == 4) {
            //进入2模式,难度为1的试卷放进t_student_test里面
            //第一次进入，将setlevel为1的试卷放进去
            TestSet testSet = testSetService.getTestSetBySetLevel(1);
            studentTestService.insertNormalTestSet(userId, testSet);
        }
        return ResultUtil.success(userLevel);
    }


    //接收模式2的试卷批改情况，进行如下处理
    /*
        返回：
            status:
            0:普通/专项试卷通过
            1:普通/专项试卷通过
            3:普通试卷通过，且为最后一张试卷，下次的套题难度依然为本难度
            4:普通试卷未通过，且为最后一张试卷，下次套题的难度为本难度
            5:普通试卷通过，且为最后一张试卷，下次的套题难度为下一个难度
            6:普通试卷未通过，且为最后一张试卷，下次的套题难度为下一个难度
            7:普通试卷通过，且为最后一张试卷，下一次转到模式3
            8:普通试卷未通过，且为最后一张试卷，下一次转到模式3
     */
    @PostMapping("/model2/check")
    @CountOnlineNumber
    @MyTransactional
    public Result<?> checkNormalTest(@RequestBody JSONObject jsonObject) {

        Map<String, Object> map = new HashMap<>();
        Long userId = jsonObject.getLong("userId");
        Long testId = jsonObject.getLong("testId");
        Integer testType = jsonObject.getInteger("testType");
        List<Integer> situationList = (List<Integer>) jsonObject.get("situation");
        List<String> userAnswerList = (List<String>) jsonObject.get("userAnswer");
        List<Long> questionIdList = JSONArray.parseArray(jsonObject.get("questionId").toString(), Long.class);

        List<QuestionRecord> questionRecordList = questionRecordService.transferToQuestionRecord(userId, testId,
                testType, questionIdList, situationList, userAnswerList);


        //将题目放入t_question_record
        questionRecordService.insertNormalTestQuestion(questionRecordList);

        //计算正确数量
        int score = testResultService.getScore(questionRecordList);

        //计算试卷情况
        int status = testResultService.getTestStatus(testId, score, testType);

        //将试卷情况放入t_test_result里面
        testResultService.insertTestResult(userId, testId, testType, score, status);

        //把拿出了的试卷在t_student_test里面移除
        int i = studentTestService.deleteTestByTestId(testId);

        //如果是普通试卷
        if (testType == 1) {
            //若status为0，则要放入专项试卷
            if (status == 0) {
                List<Long> wrongQuestionId = questionRecordService.getWrongQuestionId(questionRecordList);
                List<String> wrongQuestionKnowledgePoint = questionRecordService.getWrongQuestionKnowledgePoint(wrongQuestionId);
                //取得这张试卷所属的套题的难度
                Integer setLevel = questionRecordService.getNormalTestSetLevel(testId);
                studentTestService.insertSpecialTest(userId, wrongQuestionKnowledgePoint, setLevel);
            }

            //若这张试卷是这套试题的最后一张试卷
            NormalTest normalTest = normalTestService.getNormalTestById(testId);
            if (normalTest.getNormalTestNumber() == 10) {
                //判断下一次的套题
                TestSet nextTestSet;

                //取得最后一张普通试卷所对应的套题id
                Long setId = testSetService.getSetIdByNormalId(testId);

                //取得刚刚所做的套题的平均分和错题情况
                Map<String, Object> normalTestResultInfo = testResultService.getNormalTestAverage(setId, userId);

                //取得那套套题，的到期及格线
                TestSet testSet = testSetService.getTestSetBySetId(setId);
                Integer threshold = testSet.getThreshold();

                if ((Double) normalTestResultInfo.get("avg") < threshold) {
                    //继续做这个难度的套题
                    nextTestSet = testSetService.getTestSetBySetLevel(testSet.getSetLevel());
                    studentTestService.insertNormalTestSet(userId, nextTestSet);

                    if (status == 1) {
                        status = 3;
                    }
                    if (status == 0) {
                        status = 4;
                    }

                }

                if ((Double) normalTestResultInfo.get("avg") >= threshold) {
                    //判断刚刚做的套题时，有没有知识点错误超过3次
                    //超过3次错误的知识点
                    List<String> wrongNumberOver3 = (List<String>) normalTestResultInfo.get("wrongNumberOver3");
                    if (wrongNumberOver3.size() >= 3) {
                        //模式3
                        //讲学生的等级设置为5
                        userService.changeUserLevel(userId, 5);
                        //放入系统生成的知识点（前端知道status是7/8后，弹出一个dialog，放入系统给的专项，然后也可以让学生再选择新的专项）
                        studentTestService.systemGenerate(userId);
                        if (status == 1) {
                            status = 7;
                        }
                        if (status == 0) {
                            status = 8;
                        }

                    } else {
                        //做下一个难度的套题
                        int nextLevel = testSet.getSetLevel();

                        if (nextLevel < 3) {
                            //如果还没有到最高难度的套题
                            nextLevel += 1;
                        }

                        nextTestSet = testSetService.getTestSetBySetLevel(nextLevel);
                        studentTestService.insertNormalTestSet(userId, nextTestSet);
                        if (status == 1) {
                            status = 5;
                        }
                        if (status == 0) {
                            status = 6;
                        }

                    }


                }

            }

        }
        map.put("status", status);
        return ResultUtil.success(map);
    }


    /**
     * satus:
     * 0，未通过
     * 1，通过
     * 2，通过，且模式1完成
     * 3，未通过，且专项错误达到2次
     * 4，未通过，且专项错误达到3次即以上
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/model1/check")
    @CountOnlineNumber
    @MyTransactional
    public Result<?> checkModel1(@RequestBody JSONObject jsonObject) {
        Long userId = jsonObject.getLong("userId");
        Long testId = jsonObject.getLong("testId");
        Integer testType = jsonObject.getInteger("testType");
        List<Integer> situationList = (List<Integer>) jsonObject.get("situation");
        List<String> userAnswerList = (List<String>) jsonObject.get("userAnswer");
        List<Long> questionIdList = JSONArray.parseArray(jsonObject.get("questionId").toString(), Long.class);
        Integer userLevel = jsonObject.getInteger("userLevel");

        List<QuestionRecord> questionRecordList = questionRecordService.transferToQuestionRecord(userId, testId,
                testType, questionIdList, situationList, userAnswerList);

        //将题目放入t_question_record
        questionRecordService.insertNormalTestQuestion(questionRecordList);

        //计算正确数量
        int score = testResultService.getScore(questionRecordList);

        //计算试卷情况 通过与否 0：未通过  1：通过
        int status = testResultService.getTestStatus(testId, score, testType);

        //将试卷情况放入t_test_result里面
        testResultService.insertTestResult(userId, testId, testType, score, status);

        //把拿出了的试卷在t_student_test里面移除
        int i = studentTestService.deleteTestByTestId(testId);

        //若status为0，则要放入专项试卷，专项/普通 未通过
        if (status == 0) {
            //这份题对应知识点
            List<String> wrongQuestionKnowledgePoint = questionRecordService.getWrongQuestionKnowledgePoint(questionIdList);
            String knowledge = wrongQuestionKnowledgePoint.get(0);//取一个就好
            //找到知识点为xxx的专项试题
            List<SpecialTest> specialTests = specialTestService.getSpecialTestByKnowledgePointAndLevel(knowledge, 1);

            SpecialTest specialTest = specialTestService.getRandomSpecialTest(specialTests);

            //加入一套到Student tset中
            studentTestService.insertTest(userId, specialTest, 0);//放入一份难度为1的专项试题
        } else {

            //专项/普通通过了,查看是否还有下一套题
            StudentTest studentTest = studentTestService.getNextTest(userId);
            if (studentTest == null) {
                //设置用户等级为4
                userService.changeUserLevel(userId, 4);
                //进入2模式,难度为1的试卷放进t_student_test里面
                //第一次进入，将setlevel为1的试卷放进去
                TestSet testSet = testSetService.getTestSetBySetLevel(1);
                studentTestService.insertNormalTestSet(userId, testSet);
                status = 1;
            } else {
                status = 2;
            }


        }


        //看看这个人是不是一直没通过模式1
        /*

        思路：
            -- 先得到这个人最近通过的一次，即最近status为1的一次
            -- 然后得到result_id
            -- 然后着testType = 0的，且resultId大于刚刚得到的resultId的，进行count，如果大于等于2，表明需要提示了
         */
        if (userLevel == 2 || userLevel == 3) {
            int wrongTimesInModel1 = testResultService.getWrongTimesInModel1(userId);
            if (wrongTimesInModel1 == 2) {
                status = 3;
            }

            if (wrongTimesInModel1 > 2) {
                status = 4;
            }

        }


        return ResultUtil.success(status);
    }


    //对用户的答案进行自动判题，并返回正确与否的结果
    @PostMapping("/complieAndCheck")
    public Result<?> complieAndCheck(@RequestBody JSONObject jsonObject) throws IOException {
        List<Integer> situationList = new ArrayList<>();
        Map<String, List<Integer>> complieSituation = new HashMap<>();
        Long userId = jsonObject.getLong("userId");
        List<String> userAnswerList = (List<String>) jsonObject.get("userAnswer");
        List<Long> questionIdList = JSONArray.parseArray(jsonObject.get("questionId").toString(), Long.class);
        for (int i = 0; i < userAnswerList.size(); i++) {
            boolean questionPass = questionRecordService.isQuestionPass(questionIdList.get(i), userAnswerList.get(i));
            if (questionPass) {
                System.out.println("通过啦");
                situationList.add(1);
            } else {
                System.out.println("没有通过");
                situationList.add(0);
            }

        }


        int i = 0;
        complieSituation.put("complieSituation", situationList);
        return ResultUtil.success(complieSituation);
    }

}

