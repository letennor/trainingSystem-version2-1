package com.trainingsystem.trainingSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trainingsystem.trainingSystem.pojo.QuestionIO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface QuestionIOService extends IService<QuestionIO> {


    //插入一条io
    int insertIo(Long questionId, String input, String output);


    //根据题目id返回这道题包含的图片信息，
    List<QuestionIO> getOneQuestionIosById(Long questionId);


    //修改io
    int modifyIo(Long questionId, List<String> inputList, List<String> outputList);


    //取得一条题目所有的io
    List<QuestionIO> getAllQuestionIOByQuestionId(Long questionId);

    //将一条题目的某个input拆分成list
    List<String> getInputCodeList(Long questionId, String inputCode);


    //拿出一个文件的stdout和对应input的output，对比看看是不是true
    boolean isUserOutputPass(String fileName, QuestionIO questionIO) throws IOException;


    /*
        //将用户的output与标准答案的output进行对比
    /*
    答案类型：
    byte short int long float double boolean char
    String
    byte[] short[] int[] long[] float[] double[] boolean[] char[]
    String[]
     */
//    boolean isRight();
     //




}
