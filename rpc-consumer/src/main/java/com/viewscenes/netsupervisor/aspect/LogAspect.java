package com.viewscenes.netsupervisor.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class LogAspect {

        @Pointcut("execution(public * com.viewscenes.netsupervisor.controller.*.*(..))")
        public void LogAspect(){}

        @Before("LogAspect()")
        public void doBefore(JoinPoint joinPoint){
            System.out.println("AOPdoBefore");
        }

        @After("LogAspect()")
        public void doAfter(JoinPoint joinPoint){
            System.out.println("AOP测试doAfter");
        }

        @AfterReturning("LogAspect()")
        public void doAfterReturning(JoinPoint joinPoint){
            System.out.println("AOP测试doAfterReturning");
        }

        @AfterThrowing("LogAspect()")
        public void deAfterThrowing(JoinPoint joinPoint){
            System.out.println("AOP测试deAfterThrowing");
        }

        @Around("LogAspect()")
        public Object deAround(ProceedingJoinPoint pjp) throws Throwable{
            System.out.println("AOP测试deAround");
            Object[] args = pjp.getArgs();
            // 调用的方法名
            String method = pjp.getSignature().getName();
            // 获取目标对象
            Object target = pjp.getTarget();
            // 执行完方法的返回值
            // 调用proceed()方法，就会触发切入点方法执行
            Object result=pjp.proceed();

            System.out.println("输出,方法名：" + method + ";目标对象：" + target + ";返回值：" + result);
            System.out.println("======执行环绕通知结束=========");
            return result;
        }

}
