package com.viewscenes.netsupervisor.aspect;

import com.viewscenes.netsupervisor.annotation.TimeLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
@Order(2)
public class TimeLogAspect {

    @Pointcut("@annotation(com.viewscenes.netsupervisor.annotation.TimeLog)")
    public void aspect() {
    }

    @Around("aspect()&&@annotation(timeLog))")
    public Object around(ProceedingJoinPoint pjp,TimeLog timeLog) throws Throwable {
        System.out.println("AOP测试方法开始时间是:"+new Date());
        System.out.println("deAround");
        //获取签名
        Signature signature = pjp.getSignature();
        Object[] args = pjp.getArgs();
        // 调用的方法名
        String methodName = signature.getName();
        //获取参数类型
        Class[]  classes = ((MethodSignature)signature).getParameterTypes();
        // 获取目标对象
        Object target = pjp.getTarget();
        //获得当前访问的class
        Class<?> className = pjp.getTarget().getClass();
//        TimeLog timeLog = className.getAnnotation(TimeLog.class);
//        String str = timeLog.value();
//        System.out.println(str);
        //获取目标方法
        Method method = className.getMethod(methodName,classes);
        //获取方法上注解
//        timeLog = method.getAnnotation(TimeLog.class);
        String str = timeLog.value();
        System.out.println(str);

        Object result = pjp.proceed();

        System.out.println("输出,方法名：" + methodName + ";目标对象：" + target + ";返回值：" + result);
        System.out.println("方法结束时间是:"+new Date()) ;
        return result;

    }
}
