package com.trainingsystem.trainingSystem.config;


import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@MapperScan("com.trainingsystem.trainingSystem.mapper")
public class MyPojoConfig {


//    @Bean
//    @Profile({"dev", "test"})//设置 dev test 环境开启，保证我们的效率
//    public PerformanceInterceptor performanceInterceptor() {
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//
//        // 设置sql执行的最大时间，如果超过了则不执行，单位为ms，
////        performanceInterceptor.setMaxTime(500);
//
//        //设定sql代码格式化
//        performanceInterceptor.setFormat(true);
//        return performanceInterceptor;
//    }

}
