package com.trainingsystem.trainingSystem.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_student_knowledge")
public class StudentKnowledge implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "student_knowledge_id", type = IdType.ID_WORKER)
    private Long studentKnowledgePoint;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private Long knowledgePointId;


    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


    @TableField(exist = false)
    private String url;


    @TableField(exist = false)
    private String knowledgePoint;


    @TableField(exist = false)
    private Integer count;


}
