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
@TableName("t_special_test")
public class SpecialTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "special_id", type = IdType.ID_WORKER)
    private Long specialId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long teacherId;

    private String testName;

    private String knowledgePoint;

    private Integer specialTestLevel;

    private Integer specialThreshold;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


    //学生试题
    @TableField(exist = false)
    private List<StudentTest> studentTests;

    //专项试题题目
    @TableField(exist = false)
    private List<SpecialQuestion> specialQuestions;


    //试题里面的题目数量
    @TableField(exist = false)
    private Integer questionNumber;


}
