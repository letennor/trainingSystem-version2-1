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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "user_id", type = IdType.ID_WORKER)
    private Long userId;

    private String name;

    private String college;

    private String major;

    private Integer account;

    private String email;

    private Long pwdId;

    @TableField(exist = false)
    private String pwd;

    private Integer userLevel;

    private Integer userType;


    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    //题目记录
    @TableField(exist = false)
    private List<QuestionRecord> questionRecords;

    //试题结果
    @TableField(exist = false)
    private List<TestResult> testResults;

    //学生试题
    @TableField(exist = false)
    private List<StudentTest> studentTests;


}
