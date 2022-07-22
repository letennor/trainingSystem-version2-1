package com.trainingsystem.trainingSystem.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trainingsystem.trainingSystem.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trainingsystem.trainingSystem.pojo.UserLogin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    //取得密码
    User getUserPassword(@Param("account") int account);

    //将用户得userLevel加1
    int plusOneOnUserLevel(@Param("userId") Long userId);


    //更新用户的等级
    int updateLevel(@Param("userId") Long userId,
                    @Param("userLevel") Integer userLevel);


}
