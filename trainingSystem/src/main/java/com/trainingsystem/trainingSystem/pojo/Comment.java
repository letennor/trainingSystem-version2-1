package com.trainingsystem.trainingSystem.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor  
@AllArgsConstructor
@TableName("t_comment")
public class Comment {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "comment_id", type = IdType.ID_WORKER)
    private Long commentId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @TableField(exist = false)
    public String userName;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long fatherCommentId;

    @JsonSerialize(using = ToStringSerializer.class)
    public Long replytoUserId;

    @TableField(exist = false)
    public String upUserName;

    private String comment;

    //该条评论的子评论
    @TableField(exist = false)
    public List<Comment> sonComments;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
