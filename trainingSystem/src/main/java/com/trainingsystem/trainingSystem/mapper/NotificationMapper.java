package com.trainingsystem.trainingSystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trainingsystem.trainingSystem.pojo.Notification;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationMapper extends BaseMapper<Notification> {

    //更新通知处理状态
    int updateCheck(@Param("notificationId") Long notificationId,
                    @Param("check") int check);
}
