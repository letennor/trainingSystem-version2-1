package com.trainingsystem.trainingSystem.mapper;

import com.trainingsystem.trainingSystem.pojo.UserLogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@Repository
public interface UserLoginMapper extends BaseMapper<UserLogin> {

    //验证用户登录信息是否正确
    UserLogin ifInfoMatch(@Param("account") Integer account,
                          @Param("pwd") String pwd,
                          @Param("userType") Integer userType);

}
