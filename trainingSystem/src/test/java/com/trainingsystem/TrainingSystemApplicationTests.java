package com.trainingsystem;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingsystem.trainingSystem.mapper.*;
import com.trainingsystem.trainingSystem.pojo.*;
import com.trainingsystem.trainingSystem.service.*;
import com.trainingsystem.trainingSystem.util.common.SnowFlakeGenerateIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
class TrainingSystemApplicationTests {
    @Autowired
    StudentTestService studentTestService;
    @Autowired
    TestSetService testSetService;
    @Autowired
    UserService userService;
    @Autowired
    TestResultService testResultService;

    @Autowired
    QuestionIOService questionIOService;
    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionRecordService questionRecordService;

    @Test
    void test01() {
        List<QuestionIO> allQuestionIOByQuestionId = questionIOService.getAllQuestionIOByQuestionId(1500428165165797377L);
        Iterator<QuestionIO> iterator = allQuestionIOByQuestionId.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }


    }


    @Test
    void test02() {
        questionService.getArgumentType(1500428165165797377L);
    }


    //int[]转list
    @Test
    void test03() {
        int result[] = new int[]{1, 2, 3, 4, 5, 6, 7};
        List<Integer> list = Arrays.stream(result).boxed().collect(Collectors.toList());
        System.out.println(list.toString());
    }

    @Test
    void test003() {
        List<String> inputCodeList = questionIOService.getInputCodeList(1500428165165797377L, "[2,7,11,15] 9");
        System.out.println(inputCodeList);
    }


    @Autowired
    QuestionIOMapper questionIOMapper;

    @Test
    void test04() throws IOException {
        /*
            参数获取：
            userOutput：读取文件
            output：根据questionId和input从数据库中读取output
         */

        //一个答案就是一个文件，一个文件只有一行，而且全是String
        File file = new File("C:\\Users\\HTinnocenceIYH\\IdeaProjects\\trainingSystem\\tmp\\userOutput\\stdout.txt");// Text文件
        BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
        String userOutput = null;

        //取出对应题目的output
        QueryWrapper<QuestionIO> questionIOQueryWrapper = new QueryWrapper<>();
        questionIOQueryWrapper.eq("question_id", "1500428165165797377");
        questionIOQueryWrapper.eq("input_code", "[2,7,11,15] 9");
        QuestionIO questionIO = questionIOMapper.selectOne(questionIOQueryWrapper);
        String output = questionIO.getOutput();
        System.out.println();

        while ((userOutput = br.readLine()) != null) {// 使用readLine方法，一次读一行
            System.out.println(userOutput.replaceAll(" ", "").equals(output.replaceAll(" ", "")));
        }

        br.close();
    }

    /*
    判断一道题是否所有测试用例都通过了，需要的参数：
    1. 这道题所有的intput
    2. 这道题intput对应的output（两个可以用List<QuestionIO>拿出来）
    3. 将所有的input放入用户的代码中
    4. 编译、运行用户代码
    5. 如果编译、运行通过，返回值不是void，则生成stdout.txt文件，一个input生成一个stdout.txt文件
    6. 将txt文件中的内容取出来，形成String userOutput，然后将userOutput与output进行对比
    7. 如果对于所有的input都为true，则accept，否则不通过
     */
    @Test
    void checkAnswer() throws IOException {
        String userCode = "class Solution {\n" +
                "    public int wiggleMaxLength(int[] nums) {\n" +
                "        int n = nums.length;\n" +
                "        if (n < 2) {\n" +
                "            return n;\n" +
                "        }\n" +
                "        int[] up = new int[n];\n" +
                "        int[] down = new int[n];\n" +
                "        up[0] = down[0] = 1;\n" +
                "        for (int i = 1; i < n; i++) {\n" +
                "            if (nums[i] > nums[i - 1]) {\n" +
                "                up[i] = Math.max(up[i - 1], down[i - 1] + 1);\n" +
                "                down[i] = down[i - 1];\n" +
                "            } else if (nums[i] < nums[i - 1]) {\n" +
                "                up[i] = up[i - 1];\n" +
                "                down[i] = Math.max(up[i - 1] + 1, down[i - 1]);\n" +
                "            } else {\n" +
                "                up[i] = up[i - 1];\n" +
                "                down[i] = down[i - 1];\n" +
                "            }\n" +
                "        }\n" +
                "        return Math.max(up[n - 1], down[n - 1]);\n" +
                "    }\n" +
                "}\n";

        Long questionId = 1500428175085326338L;
        boolean questionPass = questionRecordService.isQuestionPass(questionId, userCode);
        if (questionPass) {
            System.out.println("通过啦");
        } else {
            System.out.println("没有通过");
        }

    }

    /*
    一条题目的一个io的code
    code: 是用户在输入框中输入的代码
    methodName: 是该题目方法的名字，如twoSum()
     */
    String mergeCode(String code, String methodName, QuestionIO questionIO) {
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


        //在数据库中的存储形式：”12 3“，然后用分隔符取出

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
        while (argumentNameIterator.hasNext()) {
            methodName = methodName + argumentNameIterator.next();
            if (argumentNameIterator.hasNext()) {
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


        return "import java.util.*;\n" +
                "import java.util.stream.Collectors;" + code;


    }


    //测试将inputCode组装成list
    @Test
    void test100() {
//        List<String> inputCodeList = questionIOService.getInputCodeList(1500428165165797377L, "{2,7,11,15}-9");
        String test = "{-1,0}=-1";
        QuestionIO questionIO = questionIOMapper.selectById(1500432413991587841L);
        System.out.println("test: " + test);
        System.out.println("input_code: " + questionIO.getInputCode());

        String testList[] = test.split("=");
        String inputCodeList[] = questionIO.getInputCode().split("=");

        List<String> newInputCodeList = new ArrayList<>();
        //将String[]中的内容放入list里面
        for (int i = 0; i < inputCodeList.length; i++) {
            newInputCodeList.add(inputCodeList[i]);
        }

        for (int i = 0; i < testList.length; i++) {
            System.out.println(testList[i]);
        }

        System.out.println("=================");

        for (int i = 0; i < newInputCodeList.size(); i++) {
            System.out.println(newInputCodeList.get(i));
        }

    }

    @Autowired
    QuestionImgMapper questionImgMapper;
    @Autowired
    QuestionAnswerMapper questionAnswerMapper;

    //删除题目，以及对应的io，img，answer
    @Test
    void deleteQuestion() {

        List<Long> questionIdList = new ArrayList<>();
        questionIdList.add(960558191958032384L);
        questionIdList.add(960559180672925696L);
        questionIdList.add(960564157667606528L);
        questionIdList.add(960564541920378880L);
        questionIdList.add(960564875921195008L);
        questionIdList.add(960565478172917760L);
        questionIdList.add(960565769341501440L);
        questionIdList.add(960567375420194816L);
        questionIdList.add(960567737866780672L);
        questionIdList.add(960568005064916992L);
        questionIdList.add(963907571549208576L);


        Iterator<Long> iterator = questionIdList.iterator();

        while (iterator.hasNext()) {
            Long questionId = iterator.next();
            //删除题目
            questionMapper.deleteById(questionId);

            //删除io
            QueryWrapper<QuestionIO> questionIOQueryWrapper = new QueryWrapper<>();
            questionIOQueryWrapper.eq("question_id", questionId);
            questionIOMapper.delete(questionIOQueryWrapper);

            //删除img
            QueryWrapper<QuestionImg> questionImgQueryWrapper = new QueryWrapper<>();
            questionImgQueryWrapper.eq("question_id", questionId);
            questionImgMapper.delete(questionImgQueryWrapper);

            //删除answer
            QueryWrapper<QuestionAnswer> questionAnswerQueryWrapper = new QueryWrapper<>();
            questionAnswerQueryWrapper.eq("question_id", questionId);
            questionAnswerMapper.delete(questionAnswerQueryWrapper);


        }


    }


    //增加t_question的空记录
    @Test
    void addQuestion() {

        for (int i = 0; i < 73; i++) {
            Question question = new Question();
            question.setQuestionTitle("");
            question.setArgumentType("");
            question.setMethodName("");
            question.setReturnType(0);
            question.setKnowledgePoint("");
            question.setQuestionLevel(0);
            question.setQuestionForm(0);
            question.setQuestionType(0);
            question.setTeacherId(0L);

            questionMapper.insert(question);
        }
    }


    //增加t_question_io的空记录
    @Test
    void addQuestionIO() {

        for (int i = 0; i < 185; i++) {
            QuestionIO questionIO = new QuestionIO();
            questionIO.setQuestionId(0L);
            questionIO.setInput("");
            questionIO.setInputCode("");
            questionIO.setOutput("");

            questionIOMapper.insert(questionIO);
        }

    }


    //增加t_question_answer的空记录
    @Test
    void addQuestionAnswer() {
        for (int i = 0; i < 166; i++) {

            QuestionAnswer questionAnswer = new QuestionAnswer();
            questionAnswer.setQuestionId(0L);
            questionAnswer.setLanguage(0);
            questionAnswer.setStandardAnswer("");

            questionAnswerMapper.insert(questionAnswer);
        }

    }

}
