package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.Question;
import com.trainingsystem.trainingSystem.pojo.QuestionAnswer;
import com.trainingsystem.trainingSystem.pojo.QuestionIO;
import com.trainingsystem.trainingSystem.pojo.QuestionImg;
import com.trainingsystem.trainingSystem.service.QuestionAnswerService;
import com.trainingsystem.trainingSystem.service.QuestionIOService;
import com.trainingsystem.trainingSystem.service.QuestionImgService;
import com.trainingsystem.trainingSystem.service.QuestionService;
import com.trainingsystem.trainingSystem.util.common.SnowFlakeGenerateIdWorker;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@RestController
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionAnswerService questionAnswerService;
    @Autowired
    QuestionIOService questionIOService;
    @Autowired
    QuestionImgService questionImgService;


    //取得20道水平测试题目
    @CrossOrigin
    @GetMapping("/train/initLevel")
    public Result<?> getEnterTest() {
        List<Question> enterTest = questionService.getEnterTest();
        return ResultUtil.success(enterTest);
    }


    //管理员查看所有题目
    @GetMapping("/checkAllQuestions")
    public Result<?> checkAllQuestions() {
        List<Question> questionList = questionService.getQuestion();
        return ResultUtil.success(questionList);
    }


    //根据题目id查看题目详情,返回给前端一个字典，包含，题目详情，题目IO,IMG和answer
    @PostMapping("/questionDetails")
    public Result<?> questionDetails(@RequestBody JSONObject jsonObject) {
        Long questionId = jsonObject.getLong("questionId");
        Map<String, Object> data = new HashMap<>();

        Question question = questionService.getOneQuestionById(questionId);
        data.put("questionBasicInfo", question);//题目的基本信息——一个Question对象

        List<QuestionImg> questionImgList = questionImgService.getOneQuestionImgsById(questionId);
        data.put("questionImgList", questionImgList);//该题目对应的图片

        List<QuestionIO> questionIOList = questionIOService.getOneQuestionIosById(questionId);
        data.put("questionIOList", questionIOList);//此题目的输入输出信息

        List<QuestionAnswer> questionAnswerList = questionAnswerService.getOneQuestionAnswer(questionId);
        data.put("questionAnswerList", questionAnswerList);//此题目对应的答案

        return ResultUtil.success(data);
    }


    //传入题目id，删除一道题目
    @PostMapping("/deleteOneQuestion")
    @MyTransactional
    public Result<?> deleteOneQuestion(@RequestBody JSONObject jsonObject) {
        Long questionId = jsonObject.getLong("questionId");

        int status = questionService.deleteOneQuestionById(questionId);
        if (status == 1) {
            return ResultUtil.success(1);
        } else {
            return ResultUtil.defineFail(0, "删除失败，该题目已被组成套题！");
        }
    }


    //判断一道题目是否可以修改
    @RequestMapping("/checkIsChangable")
    public Result<?> checkIsChangable(@RequestBody JSONObject jsonObject) {
        Long questionId = jsonObject.getLong("questionId");
        boolean isChangable = questionService.isQuestionChangable(questionId);

        if (isChangable) {

            return ResultUtil.success(1);
        } else {
            return ResultUtil.defineFail(0, "无法修改，该题目已被组成套题！");
        }
    }


    //修改某道题
    @PostMapping("/alterQuestion")
    @MyTransactional
    public Result<?> alterQuestion(@RequestBody JSONObject jsonObject) {

        Long questionId = jsonObject.getLong("questionId");

        //判断这道题是否已在试卷中
        int status = questionService.deleteOneQuestionById(questionId);
        if (status == 1) {

            String questionTitle = jsonObject.getString("questionTitle");
            String knowledgePoint = jsonObject.getString("knowledgePoint");
            List<QuestionAnswer> questionAnswerList = (List<QuestionAnswer>) jsonObject.get("standardAnswers");
            List<QuestionIO> questionIOList = (List<QuestionIO>) jsonObject.get("questionIOs");
            List<QuestionImg> questionImgs = (List<QuestionImg>) jsonObject.get("questionImgs");


            return ResultUtil.success(1);
        } else {
            return ResultUtil.defineFail(0, "无法修改，该题目已被组成套题！");
        }

    }


    //返回符合条件的题目
    @RequestMapping("/getAllQuestion")
    public Result<?> getAllQuestion(@RequestBody JSONObject jsonObject) {
        List<String> knowledgePointList = (List<String>) jsonObject.get("knowledgePointList");
        List<Integer> questionLevelList = (List<Integer>) jsonObject.get("questionLevelList");
        List<Integer> questionTypeList = (List<Integer>) jsonObject.get("questionTypeList");


        List<Question> questionList = questionService.getQuestion(knowledgePointList, questionLevelList, questionTypeList);
        return ResultUtil.success(questionList);
    }


    //上传一条题目
    @RequestMapping("/uploadQuestion")
    @MyTransactional
    public Result<?> uploadQuestion(@RequestBody JSONObject jsonObject) {
        String questionTitle = jsonObject.getString("questionTitle");
        String standardAnswer = jsonObject.getString("questionAnswer");
        List<String> questionAnswerList = (List<String>) jsonObject.get("questionAnswerList");
        Integer questionType = jsonObject.getInteger("questionType");
        Integer questionLevel = jsonObject.getInteger("questionLevel");
        String knowledgePoint = jsonObject.getString("kowledgePoint");
        Integer questionForm = jsonObject.getInteger("questionForm");
        Integer ioNumber = jsonObject.getInteger("ioNumber");
        List<String> inputList = (List<String>) jsonObject.get("inputList");
        List<String> outputList = (List<String>) jsonObject.get("outputList");
        String teacherIdString = jsonObject.getString("teacherId");
        Long teacherId = Long.parseLong(teacherIdString);


        //雪花算法生成题目id
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker =
                new SnowFlakeGenerateIdWorker(0L, 0L);
        String idString = snowFlakeGenerateIdWorker.generateNextId();
        Long questionId = Long.parseLong(idString);

        //插入题目
        questionService.insertQuestion(questionId, questionTitle, knowledgePoint, questionLevel,
                questionForm, questionType, teacherId);

        //插入答案
        if (questionForm == 1) {
            questionAnswerService.insertQuestionAnswer(questionId, 0, standardAnswer);
        }

        if (questionForm == 2) {
            Iterator<String> iterator = questionAnswerList.iterator();
            int i = 1;
            while (iterator.hasNext()) {
                String answer = iterator.next();
                if (answer != null) {
                    questionAnswerService.insertQuestionAnswer(questionId, i, answer);

                }
                i++;
            }

            //插入io
            for (int j = 0; j < ioNumber; j++) {
                questionIOService.insertIo(questionId, inputList.get(j), outputList.get(j));

            }

        }

        return ResultUtil.success(1);
    }


    //修改一条题目
    @RequestMapping("/modifyQuestion")
    public Result<?> modifyQuestion(@RequestBody JSONObject jsonObject) {
        Long questionId = jsonObject.getLong("questionId");
        String questionTitle = jsonObject.getString("questionTitle");
        String standardAnswer = jsonObject.getString("standardAnswer");
        List<String> questionAnswerList = (List<String>) jsonObject.get("questionAnswerList");
        Integer questionType = jsonObject.getInteger("questionType");
        Integer questionLevel = jsonObject.getInteger("questionLevel");
        String knowledgePoint = jsonObject.getString("kowledgePoint");
        Integer questionForm = jsonObject.getInteger("questionForm");
        List<String> inputList = (List<String>) jsonObject.get("inputList");
        List<String> outputList = (List<String>) jsonObject.get("outputList");


        //修改题目
        questionService.modifyQuestion(questionId, questionTitle, knowledgePoint, questionLevel, questionType);

        if (questionForm == 1) {
            questionAnswerService.modifyQuestionAnswer(questionId, 0, standardAnswer);
        }

        if (questionForm == 2) {
            //修改答案
            Iterator<String> iterator = questionAnswerList.iterator();
            int i = 1;
            while (iterator.hasNext()) {
                String answer = iterator.next();
                if (answer != null) {
                    questionAnswerService.modifyQuestionAnswer(questionId, i, answer);

                }
                i++;
            }

            //修改IO
            questionIOService.modifyIo(questionId, inputList, outputList);
        }

        return ResultUtil.success(1);
    }


    //取得某一个老师做的所有题目
    @RequestMapping("/getQuestionByTeacherId")
    public Result<?> getQuestionByTeacherId(@RequestBody JSONObject jsonObject) {
        Long teacherId = jsonObject.getLong("teacherId");
        List<Question> questionByTeacherId = questionService.getQuestionByTeacherId(teacherId);

        return ResultUtil.success(questionByTeacherId);
    }


    //取得一张专项试卷的所有题目
    @RequestMapping("/getSpecialTestQuestion")
    public Result<?> getSpecialTestQuestion(@RequestBody JSONObject jsonObject) {
        Long specialId = jsonObject.getLong("specialId");
        List<Question> specialTestQuestion = questionService.getSpecialTestQuestion(specialId);

        return ResultUtil.success(specialTestQuestion);
    }


    //取得一张普通试卷的所有题目
    @RequestMapping("/getNormalTestQuestion")
    public Result<?> getNormalTestQuestion(@RequestBody JSONObject jsonObject) {
        Long normalId = jsonObject.getLong("normalId");
        List<Question> normalTestQuestion = questionService.getNormalTestQuestion(normalId);
        return ResultUtil.success(normalTestQuestion);
    }

}

