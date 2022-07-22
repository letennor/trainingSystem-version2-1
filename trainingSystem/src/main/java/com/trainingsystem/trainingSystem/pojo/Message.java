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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_message")
public class Message implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "message_id", type = IdType.ID_WORKER)
    private Long messageId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long teacherId;

    private String messageTitle;

    private String messageContent;

    private Integer isReplied;

    private String messageReply;

    private Date replyTime;

    private Integer isCheckReply;

    @TableField(exist = false)
    private String teacherName;

    //留言人的姓名
    @TableField(exist = false)
    private String name;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}
