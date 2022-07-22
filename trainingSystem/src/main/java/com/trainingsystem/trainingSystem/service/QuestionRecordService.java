package com.trainingsystem.trainingSystem.service;

import com.trainingsystem.trainingSystem.pojo.QuestionAnswer;
import com.trainingsystem.trainingSystem.pojo.QuestionRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
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
public interface QuestionRecordService extends IService<QuestionRecord> {

    //判断注册小测试
    //前端传过来的user_id, question_id, my_answer，后端用TQuestionRecord对象进行接收，传入这个方法
    //然后方法里面进行判断，返回0/1，如果是1，则将该用户的userLevel+1
    int checkEnterTest(Long userId, List<QuestionRecord> questionRecordList);


    //模式2，写完一张普通练习
    //将一张普通/专项练习的所有题目存入t_question_record里面
    int insertNormalTestQuestion(List<QuestionRecord> questionRecordList);


    //答案匹配：传入TQuestionAnswer，返回questionId和standardAnswer一一对应的map，方便匹配
    Map<Long, String> transferQuestionAnswerToMap(List<QuestionAnswer> questionAnswerList);


    //取得学生某一张试卷所有错题的题目id
    List<Long> getWrongQuestionId(List<QuestionRecord> questionRecordList);


    //获取学生某一次普通练习错题的所有知识点返回
    List<String> getWrongQuestionKnowledgePoint(List<Long> questionIdList);


    //模式2中获得某一次练习的套题的set_level
    Integer getNormalTestSetLevel(Long testId);


    //取得某一个用户的某一张试卷的所有题目
    List<QuestionRecord> getQuestionRecordByTestId(Long userId, Long testId);


    //将传过来的测试答案封装成QuestionRecord
    List<QuestionRecord> transferToQuestionRecord(Long userId, Long testId, Integer testType,
                                                  List<Long> questionIdList, List<Integer> situationList,
                                                  List<String> userAnswerList);


    //统计某一用户question_record的所有信息，找出错得最多的三个知识点
    List<String> getWrongKnowlegePointList(Long userId);


    //判断学生某一道题是否通过
    boolean isQuestionPass(Long questionId, String userCode) throws IOException;


}
