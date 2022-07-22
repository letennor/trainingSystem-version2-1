package com.trainingsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan("com.trainingsystem.trainingSystem.mapper")
@EnableTransactionManagement//开始事务管理
public class TrainingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingSystemApplication.class, args);
    }


}



































