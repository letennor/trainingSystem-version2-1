package com.trainingsystem.trainingSystem.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.mapper.QuestionAnswerMapper;
import com.trainingsystem.trainingSystem.mapper.QuestionIOMapper;
import com.trainingsystem.trainingSystem.pojo.QuestionAnswer;
import com.trainingsystem.trainingSystem.pojo.QuestionIO;
import com.trainingsystem.trainingSystem.service.QuestionAnswerService;
import com.trainingsystem.trainingSystem.service.QuestionIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@DS("db1")
public class QuestionIOServiceImpl extends ServiceImpl<QuestionIOMapper, QuestionIO> implements QuestionIOService {

    @Autowired
    QuestionIOMapper questionIOMapper;
    @Autowired
    QuestionIOService questionIOService;

    //插入一条io
    @Override
    public int insertIo(Long questionId, String input, String output) {
        QuestionIO questionIO = new QuestionIO();
        questionIO.setQuestionId(questionId);
        questionIO.setInput(input);
        questionIO.setOutput(output);
        questionIO.setInputCode("");

        return questionIOMapper.insert(questionIO);
    }


    @Override
    public List<QuestionIO> getOneQuestionIosById(Long questionId) {

        QueryWrapper<QuestionIO> wrapper = new QueryWrapper<>();
        wrapper.eq("question_id", questionId);
        List<QuestionIO> questionIOList = questionIOMapper.selectList(wrapper);
        return questionIOList;
    }


    //修改io
    @Override
    public int modifyIo(Long questionId, List<String> inputList, List<String> outputList) {
        QueryWrapper<QuestionIO> questionIOQueryWrapper = new QueryWrapper<>();
        QuestionIO questionIO;
        questionIOQueryWrapper.eq("question_id", questionId);

        List<QuestionIO> questionIOList = questionIOMapper.selectList(questionIOQueryWrapper);

        int i;
        for (i = 0; i < questionIOList.size(); i++) {
            questionIO = new QuestionIO();
            questionIO.setQuestionIoId(questionIOList.get(i).getQuestionIoId());
            questionIO.setInput(inputList.get(i));
            questionIO.setOutput(outputList.get(i));
            questionIOMapper.updateById(questionIO);
        }

        if (inputList.size() > questionIOList.size()) {
            for (; i < inputList.size(); i++) {
                questionIOService.insertIo(questionId, inputList.get(i), outputList.get(i));
            }
        }

        return 1;
    }


    //取得一条题目所有的io
    @Override
    public List<QuestionIO> getAllQuestionIOByQuestionId(Long questionId) {
        QueryWrapper<QuestionIO> questionIOQueryWrapper = new QueryWrapper<>();
        questionIOQueryWrapper.eq("question_id", questionId);

        List<QuestionIO> questionIOList = questionIOMapper.selectList(questionIOQueryWrapper);
        return questionIOList;
    }


    //将一条题目的某个input拆分成list
    @Override
    public List<String> getInputCodeList(Long questionId, String inputCode) {
        List<String> inputCodeList = new ArrayList<>();
        QueryWrapper<QuestionIO> questionIOQueryWrapper = new QueryWrapper<>();
        questionIOQueryWrapper.eq("question_id", questionId)
                .eq("input_code", inputCode);

//        QuestionIO questionIO = questionIOMapper.selectOne(questionIOQueryWrapper);
        List<QuestionIO> questionIOList = questionIOMapper.selectList(questionIOQueryWrapper);
        QuestionIO questionIO = questionIOList.get(0);
        String[] s = questionIO.getInputCode().split("=");

        for (int i = 0; i < s.length; i++) {
            inputCodeList.add(s[i]);
        }

        return inputCodeList;

    }

    //拿出一个文件的stdout和对应input的output，对比看看是不是true
    @Override
    public boolean isUserOutputPass(String fileName, QuestionIO questionIO) throws IOException {
        //一个答案就是一个文件，一个文件只有一行，而且全是String
        File file = new File(fileName);// Text文件
        BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
        String userOutput = null;

        boolean result = false;

        //取出对应题目的output
        String output = questionIO.getOutput();

        while ((userOutput = br.readLine()) != null) {// 使用readLine方法，一次读一行
            if(userOutput.replaceAll(" ", "").equals(output.replaceAll(" ", ""))){
                result = true;
            }else{
                result = false;
                break;
            }
        }

        br.close();

        return result;
    }


}
