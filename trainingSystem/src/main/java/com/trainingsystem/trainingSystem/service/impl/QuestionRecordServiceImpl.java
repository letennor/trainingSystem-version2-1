package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.complie.Answer;
import com.trainingsystem.trainingSystem.complie.Task;
import com.trainingsystem.trainingSystem.mapper.*;
import com.trainingsystem.trainingSystem.pojo.*;
import com.trainingsystem.trainingSystem.service.QuestionIOService;
import com.trainingsystem.trainingSystem.service.QuestionRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trainingsystem.trainingSystem.service.QuestionService;
import com.trainingsystem.trainingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

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
public class QuestionRecordServiceImpl extends ServiceImpl<QuestionRecordMapper, QuestionRecord> implements QuestionRecordService {

    @Autowired
    QuestionRecordMapper questionRecordMapper;
    @Autowired
    QuestionAnswerMapper questionAnswerMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    NormalTestMapper normalTestMapper;
    @Autowired
    TestSetMapper testSetMapper;
    @Autowired
    UserService userService;
    @Autowired
    QuestionIOMapper questionIOMapper;
    @Autowired
    QuestionIOService questionIOService;
    @Autowired
    QuestionService questionService;


    @Override
    public int checkEnterTest(Long userId, List<QuestionRecord> questionRecordList) {
    /*
    1. 将questionId封装成list，然后用这个list拿到对应题目的答案
    2. 将enterTEstQuestionRecordList里面的myAnswer与拿到的题目答案进行比较，如果对一题，flag+1
    3. 判断flag是否>6
     */
        int flag = 0;
        int userLevel = 0;

        QueryWrapper<QuestionAnswer> questionAnswerQueryWrapper = new QueryWrapper<>();
        List<Long> questionIdList = new ArrayList<>();

        for (QuestionRecord questionRecord : questionRecordList) {
            questionIdList.add(questionRecord.getQuestionId());
        }


        questionAnswerQueryWrapper.in("question_id", questionIdList);
        questionAnswerQueryWrapper.eq("language", 0);

        //找到了所有题目的答案
        List<QuestionAnswer> questionAnswers = questionAnswerMapper.selectList(questionAnswerQueryWrapper);
        Map<Long, String> map = transferQuestionAnswerToMap(questionAnswers);

        for (int i = 0; i < 10; i++) {//数据结构部分
            if (questionRecordList.get(i).getUserAnswer().equals(map.get(questionRecordList.get(i).getQuestionId()))) {
                flag++;
            }
        }
        if (flag >= 6) {//level2以上
            flag = 0;
            for (int i = 10; i < 20; i++) {//算法部分
                if (questionRecordList.get(i).getUserAnswer().equals(map.get(questionRecordList.get(i).getQuestionId()))) {
                    flag++;
                }
            }
            if (flag >= 6) {
                //将用户等级设置为4
                userService.changeUserLevel(userId, 4);
                userLevel = 4;
            } else {
                //将用户等级设置为3
                userService.changeUserLevel(userId, 3);
                userLevel = 3;
            }
        } else {
            //将用户等级设置为2
            userService.changeUserLevel(userId, 2);
            userLevel = 2;
        }

        return userLevel;
    }

    //将一张普通练习的所有题目存入t_question_record里面
    @Override
    @Transactional//事务管理
    public int insertNormalTestQuestion(List<QuestionRecord> questionRecordList) {
        int insert = 0;

        for (QuestionRecord questionRecord : questionRecordList) {
            insert += questionRecordMapper.insert(questionRecord);
        }

        return insert;
    }


    //答案匹配：传入TQuestionAnswer，返回questionId和standardAnswer一一对应的map，方便匹配
    @Override
    public Map<Long, String> transferQuestionAnswerToMap(List<QuestionAnswer> questionAnswerList) {
        Map<Long, String> map = new HashMap<>();

        for (QuestionAnswer questionAnswer : questionAnswerList) {
            map.put(questionAnswer.getQuestionId(), questionAnswer.getStandardAnswer());
        }

        return map;
    }


    //取得学生某一张试卷所有错题的题目id
    @Override
    public List<Long> getWrongQuestionId(List<QuestionRecord> questionRecordList) {
        List<Long> questionIdList = new ArrayList<>();
        QuestionRecord questionRecord;
        Iterator<QuestionRecord> iterator = questionRecordList.iterator();

        while (iterator.hasNext()) {
            questionRecord = iterator.next();
            if (questionRecord.getSituation() == 0) {
                questionIdList.add(questionRecord.getQuestionId());
            }
        }

        return questionIdList;
    }


    //获取学生某一次普通练习错题的所有知识点返回
    @Override
    public List<String> getWrongQuestionKnowledgePoint(List<Long> questionIdList) {
        /*
        步骤：
        1、将questionIdList中各个题目的知识点拿出来
         */

        List<String> knowledgePointList = new ArrayList<>();
        List<Question> questions = questionMapper.selectBatchIds(questionIdList);
        String knowledgePoint;


        for (Question question : questions) {
            knowledgePoint = question.getKnowledgePoint();
            knowledgePointList.add(knowledgePoint);
        }

        return knowledgePointList;
    }


    //模式2中获得某一次练习的套题的set_level
    @Override
    public Integer getNormalTestSetLevel(Long testId) {
        //questionRecord，从testId找到setId，从setId找到setLevel
        NormalTest normalTest = normalTestMapper.selectById(testId);

        Long setId = normalTest.getSetId();
        TestSet testSet = testSetMapper.selectById(setId);

        Integer setLevel = testSet.getSetLevel();

        return setLevel;
    }


    //取得某一个用户某一张试卷的所有题目信息
    @Override
    public List<QuestionRecord> getQuestionRecordByTestId(Long userId, Long testId) {
        List<QuestionRecord> questionRecordByTestId = questionRecordMapper.getQuestionRecordByTestId(userId, testId);
        return questionRecordByTestId;
    }


    //将传过来的测试答案封装成QuestionRecord
    @Override
    public List<QuestionRecord> transferToQuestionRecord(Long userId, Long testId, Integer testType,
                                                         List<Long> questionIdList, List<Integer> situationList,
                                                         List<String> userAnswerList) {
        List<QuestionRecord> questionRecordList = new ArrayList<>();
        QuestionRecord questionRecord;

        for (int i = 0; i < questionIdList.size(); i++) {
            questionRecord = new QuestionRecord();
            questionRecord.setUserId(userId);
            questionRecord.setTestId(testId);
            questionRecord.setTestType(testType);
            questionRecord.setQuestionId(questionIdList.get(i));
            if (situationList != null) {
                questionRecord.setSituation(situationList.get(i));
            }
            questionRecord.setUserAnswer(userAnswerList.get(i));
            questionRecordList.add(questionRecord);
        }

        return questionRecordList;
    }


    //统计某一用户question_record的所有信息，找出错得最多的三个知识点
    @Override
    public List<String> getWrongKnowlegePointList(Long userId) {
        List<String> wrongKnowlegePointList = questionRecordMapper.getWrongKnowlegePointList(userId);
        return wrongKnowlegePointList;
    }

    //判断学生某一道题是否通过
    /*
    userCode是用户传过来的code
     */
    @Override
    public boolean isQuestionPass(Long questionId, String userCode) throws IOException {
        boolean flag = false;

        //通过questionId获取Question
        Question question = questionMapper.selectById(questionId);

        //获取这条题目所有的io
        QueryWrapper<QuestionIO> questionIOQueryWrapper = new QueryWrapper<>();
        questionIOQueryWrapper.eq("question_id", questionId);
        List<QuestionIO> questionIOList = questionIOMapper.selectList(questionIOQueryWrapper);
        Iterator<QuestionIO> questionIOIterator = questionIOList.iterator();

        //获取问题的methodName
        String methodName = question.getMethodName();

        while (questionIOIterator.hasNext()) {
            QuestionIO questionIO = questionIOIterator.next();
            //循环的目的是拿出input_code和output和questionIO

            String code = mergeCode(userCode, methodName, questionIO, question.getReturnType());

            //编译、执行代码，生成stdout文件
            Task task = new Task();
            com.trainingsystem.trainingSystem.complie.Question questionTemp = new com.trainingsystem.trainingSystem.complie.Question();
            questionTemp.setCode(code);
            Answer answer = null;
            try {
                answer = task.compileAndRun(questionTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //如果answer == 1或者==2，则不生成文件
            if (answer.getErrno() == 0) {
                //生成一个文件，就与output对比一下
                String fileName = task.getSTDOUT();
                boolean userOutputPass = questionIOService.isUserOutputPass(fileName, questionIO);
                if (userOutputPass) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            } else {
                flag = false;
                break;
            }

        }

        return flag;
    }


    //返回的是可以直接执行的用户的代码，之后会用这个代码来进行判断，生成stdout，然后用stdout进行判断
    String mergeCode(String code, String methodName, QuestionIO questionIO, Integer returnType) {
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
        String argumentName = "";
        String tempType = "";
        String inputCode = "";
        String newArgumentCode = "";
        int type = 0;
        int pos = 0;
        while (augumentTypeIterator.hasNext() && inputCodeIterator.hasNext()) {
            type = augumentTypeIterator.next();
            inputCode = inputCodeIterator.next();
            switch (type) {
                case 1:
                    tempType = "byte";
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

                case 2:
                    tempType = "short";
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
                    tempType = "long";
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

                case 5:
                    tempType = "float";
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

                case 6:
                    tempType = "double";
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

                case 7:
                    tempType = "char";
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

                case 8:
                    tempType = "boolean";
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

                case 9:
                    tempType = "String";
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

                case 10:
                    tempType = "byte[]";
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

                case 11:
                    tempType = "short[]";
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
                    tempType = "long[]";
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

                case 14:
                    tempType = "float[]";
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

                case 15:
                    tempType = "double[]";
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

                case 16:
                    tempType = "char[]";
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

                case 17:
                    tempType = "boolean[]";
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

                case 18:
                    tempType = "String[]";
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

                case 19:
                    tempType = "List<Byte>";
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

                case 20:
                    tempType = "List<Short>";
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

                case 21:
                    tempType = "List<Integer>";
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

                case 22:
                    tempType = "List<Long>";
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

                case 23:
                    tempType = "List<Float>";
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

                case 24:
                    tempType = "List<Double>";
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

                case 25:
                    tempType = "List<Character>";
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

                case 26:
                    tempType = "List<Boolean>";
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


                case 27:
                    tempType = "List<String>";
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


            }
        }



        /*
        加入调用方法的代码
         */
        int methodPos = methodName.lastIndexOf(")");
        methodName = methodName.substring(0, methodPos);

        //把参数加进去
        Iterator<String> argumentNameIterator = argumentNameList.iterator();
        while (argumentNameIterator.hasNext()) {
            methodName = methodName + argumentNameIterator.next();
            if (argumentNameIterator.hasNext()) {
                //加上逗号
                methodName = methodName + ",";
            }
        }

        //将调用和打印组装进去
        mainCode = contactMainCode(methodName, returnType, mainCode);


        /*
        对mainCode进行最后的组装
         */
        //将最后一个"}"截掉，然后加上mainCode，然后再补回"}"
        int position = code.lastIndexOf("}");

        //去掉"}"
        code = code.substring(0, position);

        //加上main之后的code
        code = code + mainCode + "}";

        return "import java.util.*;\n" +
                "import java.util.stream.Collectors;" + code;

    }


    /*
    为main函数加上调用函数部分和打印部分（因为不同的返回类型，定义的result不同，打印的方式也不同）
     */
    String contactMainCode(String methodName, Integer returnType, String mainCode) {
        String methodCall = "";
        int mainCodePos = mainCode.lastIndexOf("}");
        mainCode = mainCode.substring(0, mainCodePos);
        switch (returnType){
            case 1://byte
                methodCall = "byte result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall + "System.out.println(result);" + "}";//此时mainCode组装完毕
                break;

            case 2://short
                methodCall = "short result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall + "System.out.println(result);" + "}";//此时mainCode组装完毕
                break;

            case 3://int
                methodCall = "int result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall + "System.out.println(result);" + "}";//此时mainCode组装完毕
                break;

            case 4://long
                methodCall = "long result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall + "System.out.println(result);" + "}";//此时mainCode组装完毕
                break;

            case 5://float
                methodCall = "float result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall + "System.out.println(result);" + "}";//此时mainCode组装完毕
                break;

            case 6://double
                methodCall = "double result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall + "System.out.println(result);" + "}";//此时mainCode组装完毕
                break;

            case 7://char
                methodCall = "char result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall + "System.out.println(result);" + "}";//此时mainCode组装完毕
                break;

            case 8://boolean
                methodCall = "boolean result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall + "System.out.println(result);" + "}";//此时mainCode组装完毕
                break;

            case 9://String
                methodCall = "String result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall + "System.out.println(\"\\\"\" + result + \"\\\"\");" + "}";//此时mainCode组装完毕



                break;

            case 10://byte[]
                methodCall = "byte[] result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "List<Byte> resultList = new ArrayList<>();\n" +
                        "                for (int i = 0; i < result.length; i++) {\n" +
                        "                    resultList.add(result[i]);\n" +
                        "                }\n" +
                        "                System.out.println(resultList.toString());" +
                        "}";
                break;

            case 11://short[]
                methodCall = "short[] result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "List<Short> resultList = new ArrayList<>();\n" +
                        "                for (int i = 0; i < result.length; i++) {\n" +
                        "                    resultList.add(result[i]);\n" +
                        "                }\n" +
                        "                System.out.println(resultList.toString());" +
                        "}";

                break;

            case 12://int[]
                methodCall = "int[] result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "List<Integer> resultList = new ArrayList<>();\n" +
                        "                for (int i = 0; i < result.length; i++) {\n" +
                        "                    resultList.add(result[i]);\n" +
                        "                }\n" +
                        "                System.out.println(resultList.toString());" +
                        "}";
                break;

            case 13://long[]
                methodCall = "long[] result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "List<Long> resultList = new ArrayList<>();\n" +
                        "                for (int i = 0; i < result.length; i++) {\n" +
                        "                    resultList.add(result[i]);\n" +
                        "                }\n" +
                        "                System.out.println(resultList.toString());" +
                        "}";
                break;

            case 14://float[]
                methodCall = "float[] result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "List<Float> resultList = new ArrayList<>();\n" +
                        "                for (int i = 0; i < result.length; i++) {\n" +
                        "                    resultList.add(result[i]);\n" +
                        "                }\n" +
                        "                System.out.println(resultList.toString());" +
                        "}";
                break;

            case 15://double[]
                methodCall = "double[] result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "List<Double> resultList = new ArrayList<>();\n" +
                        "                for (int i = 0; i < result.length; i++) {\n" +
                        "                    resultList.add(result[i]);\n" +
                        "                }\n" +
                        "                System.out.println(resultList.toString());" +
                        "}";
                break;

            case 16://char[]
                methodCall = "char[] result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "List<Character> resultList = new ArrayList<>();\n" +
                        "                for (int i = 0; i < result.length; i++) {\n" +
                        "                    resultList.add(result[i]);\n" +
                        "                }\n" +
                        "                System.out.println(resultList.toString());" +
                        "}";
                break;

            case 17://boolean[]
                methodCall = "boolean[] result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "List<Boolean> resultList = new ArrayList<>();\n" +
                        "                for (int i = 0; i < result.length; i++) {\n" +
                        "                    resultList.add(result[i]);\n" +
                        "                }\n" +
                        "                System.out.println(resultList.toString());" +
                        "}";
                break;

            case 18://String[]
                methodCall = "String[] result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "List<String> resultList = new ArrayList<>();\n" +
                        "                for (int i = 0; i < result.length; i++) {\n" +
                        "                    resultList.add(result[i]);\n" +
                        "                }\n" +
                        "for (int i = 0; i < resultList.size(); i++) {\n" +
                        "                    resultList.set(i, \"\\\"\" + resultList.get(i) + \"\\\"\");\n" +
                        "                }" +
                        "                System.out.println(resultList.toString());" +
                        "}";



                break;

            case 19://List<Byte>
                methodCall = "List<Byte> result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "System.out.println(result.toString());}";
                break;

            case 20://List<Short>
                methodCall = "List<Short> result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "System.out.println(result.toString());}";
                break;

            case 21://List<Integer>
                methodCall = "List<Integer> result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "System.out.println(result.toString());}";
                break;

            case 22://List<Long>
                methodCall = "List<Long> result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "System.out.println(result.toString());}";
                break;

            case 23://List<Float>
                methodCall = "List<Float> result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "System.out.println(result.toString());}";
                break;

            case 24://List<Double>
                methodCall = "List<Double> result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "System.out.println(result.toString());}";
                break;

            case 25://List<Character>
                methodCall = "List<Character> result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "System.out.println(result.toString());}";
                break;

            case 26://List<Boolean>
                methodCall = "List<Boolean> result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "System.out.println(result.toString());}";
                break;

            case 27://List<String>
                methodCall = "List<String> result = solution.";
                methodCall = methodCall + methodName + ");";
                mainCode = mainCode + methodCall +
                        "for (int i = 0; i < result.size(); i++) {\n" +
                        "     result.set(i, \"\\\"\" + result.get(i) + \"\\\"\");\n" +
                        "}\n" +
                        " System.out.println(result);" +
                        "}";

                break;

        }

        return mainCode;

    }

}
