package com.digital.attendance.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Configuration
public class GeneralConfig {

    private static final Logger logger = LoggerFactory.getLogger(GeneralConfig.class);

    @Before("execution(* com.digital.attendance.service.MainServiceImpl.*(..))")
    public void beforeMethodExecution(JoinPoint joinPoint){
        logger.info("METHOD NAME : " + joinPoint.getSignature().getName());
        logger.info("ARGS PASSED IN METHOD : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* com.digital.attendance.service.MainServiceImpl.*(..))", returning = "result")
    public void getReturnResultForAllMethods(JoinPoint joinPoint, Object result){
        logger.info("METHOD RESULT RETURNS = " + result);
        logger.info("------------------------------------------------------------------");
    }


    @Around("@annotation(com.digital.attendance.aop.TrackTime))")
    public Object getTimeToExecuteMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();
        logger.info("METHOD EXECUTION TIME : " + stopWatch.getTotalTimeSeconds() + " SECONDS");
        return result;

    }


    @Before("execution(* com.digital.attendance.service.SupportingServiceImpl.*(..))")
    public void beforeMethodExecutionForSupportingService(JoinPoint joinPoint){
        logger.info("METHOD NAME : " + joinPoint.getSignature().getName());
        logger.info("ARGS PASSED IN METHOD : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* com.digital.attendance.service.SupportingServiceImpl.*(..))", returning = "result")
    public void getReturnResultForAllMethodsForSupportingService(JoinPoint joinPoint, Object result){
        logger.info("METHOD RESULT RETURNS = " + result);
        logger.info("------------------------------------------------------------------");
    }

}
