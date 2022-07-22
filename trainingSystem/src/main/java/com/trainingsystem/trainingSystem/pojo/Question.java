package com.trainingsystem.trainingSystem.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "question_id", type = IdType.ID_WORKER)
    private Long questionId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long teacherId;


    private String questionTitle;

    private String knowledgePoint;

    private Integer questionLevel;


    private Integer questionForm;

    private Integer questionType;

    private String argumentType;

    private String methodName;

    private Integer returnType;


    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    //答案
    @TableField(exist = false)
    private List<QuestionAnswer> questionAnswers;


    //图片
    @TableField(exist = false)
    private List<QuestionImg> questionImgList;


    //输入输出
    @TableField(exist = false)
    List<QuestionIO> questionIOList;


}
