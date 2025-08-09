package com.ujwal.TrackYourExpense.Aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    // Call before every method in Service package
    @Before("execution(* com.ujwal.TrackYourExpense.Service.*(..))")
    public void logMethodCall() {
        LOGGER.info("Method called...");
    }

    // Call when an exception occurs in Service package
    @AfterThrowing(pointcut = "execution(* com.ujwal.TrackYourExpense.Service.*(..))", throwing = "ex")
    public void logMethodCrash(JoinPoint jp, Throwable ex) {
        LOGGER.error("Method {} threw an error: {}", jp.getSignature().getName(), ex.getMessage());
    }
    @After("execution(* com.ujwal.TrackYourExpense.Service.*(..))")
    public void logMethod() {
        LOGGER.info("Method run successfully...");
    }
}
