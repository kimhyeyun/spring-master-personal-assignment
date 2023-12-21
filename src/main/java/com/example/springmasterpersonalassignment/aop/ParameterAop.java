package com.example.springmasterpersonalassignment.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j(topic = "[AOP]")
@Aspect
@Component
public class ParameterAop {


    @Pointcut("execution(* com.example.springmasterpersonalassignment.controller..*.*(..))")
    private void cut() {
    }

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info(method.getName() + " 실행");

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("type : " + arg.getClass().getSimpleName());
            log.info("value : " + arg);
        }
        log.info("요청 시간: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss")));
    }

    @AfterReturning(value = "cut()", returning = "obj")
    public void afterReturning(JoinPoint joinPoint, Object obj) {
        log.info("return obj");
        log.info(obj.toString());
        log.info("응답 시간: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss")));
    }
}
