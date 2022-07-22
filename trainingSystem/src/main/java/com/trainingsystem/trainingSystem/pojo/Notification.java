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
@TableName("t_notification")
public class Notification implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "notification_id", type = IdType.ID_WORKER)
    private Long notificationId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private Integer userType;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long receiveUserId;

    private Integer isCheck;

    private String notificationInfo;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}
