package com.trainingsystem.trainingSystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.complie.Answer;
import com.trainingsystem.trainingSystem.complie.Question;
import com.trainingsystem.trainingSystem.complie.Task;
import com.trainingsystem.trainingSystem.mapper.QuestionIOMapper;
import com.trainingsystem.trainingSystem.pojo.QuestionIO;
import com.trainingsystem.trainingSystem.service.QuestionIOService;
import com.trainingsystem.trainingSystem.service.QuestionService;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.*;

@RestController
public class ComplieController {
    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionIOMapper questionIOMapper;
    @Autowired
    QuestionIOService questionIOService;

    @PostMapping("/complie/test")
    public Result<?> complieTest(@RequestBody JSONObject jsonObject) throws IOException, InterruptedException {
        String code = jsonObject.getString("code");
        Long questionId = jsonObject.getLong("questionId");
        questionId =1500428165165797377L;

        QueryWrapper<QuestionIO> questionIOQueryWrapper = new QueryWrapper<>();
        questionIOQueryWrapper.eq("question_id", questionId)
                .eq("input_code", "{2,7,11,15} 9");

        QuestionIO questionIO = questionIOMapper.selectOne(questionIOQueryWrapper);


        code = mergeCode(code, "twoSum()", questionIO);
        System.out.println("新的code：" + code);

        Task task = new Task();
        Question question = new Question();
        question.setCode(code);
        Answer answer = task.compileAndRun(question);

        if (answer.getErrno() == 0) {
            //编译成功，执行成功
            return ResultUtil.success(0);
        }

        if (answer.getErrno() == 1) {
            //编译失败
            return ResultUtil.success(1);
        }

        if (answer.getErrno() == 2) {
            //编译成功，运行失败
            return ResultUtil.success(2);
        }
        return null;
    }


    String mergeCode(String code, String methodName , QuestionIO questionIO){
        //将main补上去
        String mainCode =
                "     public static void main(String[] args){\n" +
                        "        Solution solution = new Solution();\n" +
                        "    } ";


        //参数类型list
        List<Integer> argumentTypeList = questionService.getArgumentType(questionIO.getQuestionId());

        //参数代码list
        List<String> inputCodeList = questionIOService.getInputCodeList(questionIO.getQuestionId(), questionIO.getInputCode());

        //参数名称list
        List<String> argumentNameList = new ArrayList<>();


        Iterator<Integer> augumentTypeIterator = argumentTypeList.iterator();
        Iterator<String> inputCodeIterator = inputCodeList.iterator();

        //为mainCode加上参数
        int argumentCount = 0;
        String argumentName =  "";
        String tempType = "";
        String inputCode = "";
        String newArgumentCode = "";
        int type = 0;
        int pos = 0;
        while (augumentTypeIterator.hasNext() && inputCodeIterator.hasNext()){
            type = augumentTypeIterator.next();
            inputCode = inputCodeIterator.next();
            switch (type){
                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    tempType = "int";
                    argumentName = "augument" + argumentCount;

                    //inputCode存于数据库中
                    argumentCount++;

                    newArgumentCode = tempType + " " + argumentName + " = " + inputCode + ";";
                    //将创建参数的名字放入mainCode里面
                    pos = mainCode.lastIndexOf("}");
                    mainCode = mainCode.substring(0, pos);
                    mainCode = mainCode + newArgumentCode + "}";
                    argumentNameList.add(argumentName);

                    break;

                case 4:

                    break;

                case 5:

                    break;

                case 6:

                    break;

                case 7:

                    break;
                case 8:

                    break;

                case 9:

                    break;

                case 10:

                    break;

                case 11:

                    break;

                case 12:
                    tempType = "int[]";
                    argumentName = "augument" + argumentCount;
                    argumentCount++;

                    newArgumentCode = tempType + " " + argumentName + " = " + inputCode + ";";
                    //将创建参数的名字放入mainCode里面
                    pos = mainCode.lastIndexOf("}");
                    mainCode = mainCode.substring(0, pos);
                    mainCode = mainCode + newArgumentCode + "}";

                    argumentNameList.add(argumentName);

                    break;

                case 13:

                    break;

                case 14:

                    break;

                case 15:

                    break;

                case 16:

                    break;

                case 17:

                    break;

                case 18:

                    break;



            }
        }



        //加入调用方法的代码
        int methodPos = methodName.lastIndexOf(")");
        methodName = methodName.substring(0, methodPos);

        //把参数加进去
        Iterator<String> argumentNameIterator = argumentNameList.iterator();
        while (argumentNameIterator.hasNext()){
            methodName = methodName + argumentNameIterator.next();
            if (argumentNameIterator.hasNext()){
                //加上逗号
                methodName = methodName + ",";
            }
        }
        String methodCall = "int[] result = solution." + methodName + ");";

        int mainCodePos = mainCode.lastIndexOf("}");
        mainCode = mainCode.substring(0, mainCodePos);
        mainCode = mainCode + methodCall +
                "        List<Integer> list = Arrays.stream(result).boxed().collect(Collectors.toList());\n" +
                "        System.out.println(list.toString());" + "}";




        //对mainCode进行操作，如果有输入，则先把输入写上
        //将最后一个"}"截掉，然后加上mainCode，然后再补回"}"
        int position = code.lastIndexOf("}");

        //去掉"}"
        code = code.substring(0, position);

        //加上main之后的code
        code = code + mainCode + "}";

        System.out.println("==============================");
        System.out.println(code);
        System.out.println("==============================");

        return "import java.util.*;\n" +
                "import java.util.stream.Collectors;" + code;

    }


}
