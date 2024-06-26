package com.yan.demo.common.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: sixcolor
 * @Date: 2024-04-29 19:31
 * @Description:
 */
@Aspect
@Component
public class ScheduleAspect {

    private static final Logger log = LoggerFactory.getLogger(ScheduleAspect.class);


    @Pointcut("execution(* com.yan.demo.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        log.info("---------------------------------------------------");
        log.info("--------------------定时任务已开启--------------------");
        log.info("---------------------------------------------------");
        String methodName = joinPoint.getSignature().toShortString();
        log.info("Method " + methodName + " execution started.");
    }

    @After("pointcut()")
    public void afterMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        System.out.println("Method " + methodName + " execution finished.");
        log.info("---------------------------------------------------");
        log.info("--------------------定时任务已结束--------------------");
        log.info("---------------------------------------------------");
    }

}
