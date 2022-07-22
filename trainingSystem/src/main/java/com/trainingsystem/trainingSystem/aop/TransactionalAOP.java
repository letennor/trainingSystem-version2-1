package com.trainingsystem.trainingSystem.aop;


import com.trainingsystem.trainingSystem.util.common.TransactionalUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

@Component
@Slf4j
@Aspect
public class TransactionalAOP {

    @Autowired
    private TransactionalUtils transactionalUtils;


    /*
    拦截MyTransactional注解
     */
    @Around(value = "@annotation(com.trainingsystem.trainingSystem.annotation.MyTransactional)")
    public Object around(ProceedingJoinPoint joinPoint) {
        TransactionStatus begin = null;
        //目标方法
        try {
            log.info("开启事务");
            begin = transactionalUtils.begin();
            Object result = joinPoint.proceed();
            log.info("提交事务");
            transactionalUtils.commit(begin);
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            log.info("回滚事务");
            if (begin != null) {
                transactionalUtils.rollback(begin);
            }
            return "系统错误";
        }
    }


}
