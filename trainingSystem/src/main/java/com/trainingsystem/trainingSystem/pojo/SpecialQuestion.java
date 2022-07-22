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
@TableName("t_special_question")
public class SpecialQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "special_question_id", type = IdType.ID_WORKER)
    private Long specialQuestionId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long specialId;

    private Integer specialQuestionNumber;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;


    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableField(exist = false)
    private String testName;

    @TableField(exist = false)
    private Integer questionLevel;

    @TableField(exist = false)
    private Integer questionForm;

    @TableField(exist = false)
    private Integer questionType;

    @TableField(exist = false)
    private String questionTitle;

    //图片
    @TableField(exist = false)
    private List<QuestionImg> questionImgList;

    //输入输出
    @TableField(exist = false)
    List<QuestionIO> questionIOList;

    //答案
    @TableField(exist = false)
    List<QuestionAnswer> questionAnswerList;

}
