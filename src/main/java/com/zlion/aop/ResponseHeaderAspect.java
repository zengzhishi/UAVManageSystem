package com.zlion.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletResponse;

/**
 * Created by zzs on 10/7/16.
 */

@Aspect
@Component
public class ResponseHeaderAspect {


    @Pointcut("execution(public * com.zlion.controller..*.*(..))")
    public void excudeService(){

    }

    @Before("excudeService()")
    public void addRespondHeader(JoinPoint joinPoint) throws Throwable{

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletResponse response=servletRequestAttributes.getResponse();
        response.setHeader("Access-Control-Allow-Origin", "*");

        System.out.println(">>>>>>Add header complete!!!!");

    }



}
