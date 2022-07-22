package com.trainingsystem.trainingSystem.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.mapper.*;
import com.trainingsystem.trainingSystem.pojo.*;
import com.trainingsystem.trainingSystem.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    SpecialQuestionMapper specialQuestionMapper;
    @Autowired
    NormalQuestionMapper normalQuestionMapper;
    @Autowired
    QuestionIOMapper questionIOMapper;
    @Autowired
    QuestionImgMapper questionImgMapper;
    @Autowired
    QuestionAnswerMapper questionAnswerMapper;
    @Autowired
    CommentMapper commentMapper;

    //从题库里找所有题目
    @Override
    public List<Question> getQuestion() {
        List<Question> questions = questionMapper.selectList(null);
        return questions;
    }


    //得到题库中所有题目的数量
    @Override
    public Integer getQuestionNumber() {
        Integer integer = questionMapper.selectCount(null);
        return integer;
    }


    //从题库中找符合条件的题目
    @Override
    public List<Question> getQuestion(List<String> knowlegePoint, List<Integer> questionLevel,
                                      List<Integer> questionType) {


        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.in("knowledge_point", knowlegePoint);
        questionQueryWrapper.in("question_level", questionLevel);
        questionQueryWrapper.in("question_type", questionType);
        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);

        return questionList;
    }


    //为注册测试，随机取30道填空题，难度为1，题目类型为c和数据结构和算法
    @Override
    public List<Question> getEnterTest() {
        QueryWrapper<Question> questionQueryWrapper;

        //放入前端展示的题目的list
        List<Question> enterTestQuestions = new ArrayList<>();
        int random = 0;

        for (int questionType = 2; questionType <= 3; questionType++) {
            questionQueryWrapper = new QueryWrapper<>();
            questionQueryWrapper.eq("question_level", 1);//拿难度为1星的难度
            questionQueryWrapper.eq("question_type", questionType);//数据结构/算法
            questionQueryWrapper.eq("question_form", 1);//全部拿填空题

            //把所有符合条件的题目都筛选出来了，需要从中拿出10道题
            List<Question> questions = questionMapper.selectList(questionQueryWrapper);

            for (int i = 0; i < 10; i++) {
                random = (int) (Math.random() * questions.size() + 1);
                Question enterQuestion = questions.get(random - 1);
                enterTestQuestions.add(enterQuestion);
            }

        }

        return enterTestQuestions;
    }


    //插入1条题目
    @Override
    public int insertQuestion(Long questionId, String questionTitle, String knowledgePoint, Integer questionLevel,
                              Integer questionForm, Integer questionType, Long teacherId) {
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setQuestionTitle(questionTitle);
        question.setKnowledgePoint(knowledgePoint);
        question.setQuestionLevel(questionLevel);
        question.setQuestionForm(questionForm);
        question.setQuestionType(questionType);
        question.setTeacherId(teacherId);
        question.setMethodName("");
        question.setReturnType(0);
        question.setArgumentType("");

        return questionMapper.insert(question);
    }


    @Override
    public Question getOneQuestionById(Long questionId) {
        Question question = questionMapper.selectById(questionId);
        return question;
    }


    //判断某一条题目是否能够修改
    @Override
    public boolean isQuestionChangable(Long questionId) {
        //查看该题目是否被组成了专项试题
        QueryWrapper<SpecialQuestion> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("question_id", questionId);
        List<SpecialQuestion> specialQuestionList = specialQuestionMapper.selectList(wrapper1);
        //查看改题目是否被组成了普通试题
        QueryWrapper<NormalQuestion> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("question_id", questionId);
        List<NormalQuestion> normalQuestionList = normalQuestionMapper.selectList(wrapper2);

        if (specialQuestionList.size() == 0 && normalQuestionList.size() == 0) {
            return true;
        }

        return false;
    }


    /*    根据题目id删除一道题，若能删除，
    *    前提：此题目前没有被组成套题，否则不能删，
      要将其标答，io，图片，四个表内涉及到的数据全删了
    */
    @Override
    public int deleteOneQuestionById(Long questionId) {

        if (isQuestionChangable(questionId)) {//没有被组成套题
            //question表中删除记录
            int status = questionMapper.deleteById(questionId);
            //删除io
            QueryWrapper<QuestionIO> ioQueryWrapper = new QueryWrapper<>();
            ioQueryWrapper.eq("question_id", questionId);
            questionIOMapper.delete(ioQueryWrapper);
            //删除img
            QueryWrapper<QuestionImg> imgQueryWrapper = new QueryWrapper<>();
            imgQueryWrapper.eq("question_id", questionId);
            questionImgMapper.delete(imgQueryWrapper);
            //删除answer
            QueryWrapper<QuestionAnswer> answerQueryWrapper = new QueryWrapper<>();
            answerQueryWrapper.eq("question_id", questionId);
            questionAnswerMapper.delete(answerQueryWrapper);
            //删除comment
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("question_id", questionId);
            commentMapper.delete(commentQueryWrapper);

            return status;
        } else {
            return 0;
        }

    }


    //取得某一个老师上传的的所有题目
    @Override
    public List<Question> getQuestionByTeacherId(Long teacherId) {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("teacher_id", teacherId);

        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);

        return questionList;
    }


    //取得一个老师制作的所有题目的数量
    @Override
    public Integer getQuestionNumberByTeacherId(Long teacherId) {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("teacher_id", teacherId);

        Integer integer = questionMapper.selectCount(questionQueryWrapper);
        return integer;
    }


    //修改一条题目
    @Override
    public int modifyQuestion(Long questionId, String questionTitle, String knowledgePoint,
                              Integer questionLevel, Integer questionType) {
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setQuestionTitle(questionTitle);
        question.setKnowledgePoint(knowledgePoint);
        question.setQuestionType(questionType);
        question.setQuestionLevel(questionLevel);

        return questionMapper.updateById(question);
    }


    //取得某一张专项试卷的基本信息
    @Override
    public List<Question> getSpecialTestQuestion(Long specialId) {
        List<Question> specialTestQuestion = questionMapper.getSpecialTestQuestion(specialId);
        return specialTestQuestion;
    }


    //取得一套普通试卷的基本题目信息
    @Override
    public List<Question> getNormalTestQuestion(Long normalId) {
        List<Question> normalTestQuestion = questionMapper.getNormalTestQuestion(normalId);
        return normalTestQuestion;
    }


    //取得一条题目的参数类型
    @Override
    public List<Integer> getArgumentType(Long qustionId) {
        Question question = questionMapper.selectById(qustionId);
        String argumentTypeString = question.getArgumentType();
        List<Integer> argumentTypeList = new ArrayList<>();
        String[] split = argumentTypeString.split("=");

        for (int i = 0; i < split.length; i++) {
            Integer type = Integer.parseInt(split[i]);
            argumentTypeList.add(type);
        }

        return argumentTypeList;
    }

}
