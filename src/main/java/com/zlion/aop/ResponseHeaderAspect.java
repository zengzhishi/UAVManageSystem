package com.zlion.aop;

import com.zlion.service.DateCompareException;
import com.zlion.service.UavService;
import com.zlion.util.GeohashUtil;
import com.zlion.util.HttpClientCommonUtil;
import com.zlion.util.MailUtil;
import org.apache.commons.mail.EmailException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 * Created by zzs on 10/7/16.
 */

@Aspect
@Component
public class ResponseHeaderAspect {

    @Autowired
    private UavService uavService;


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

    @Pointcut("execution(public * com.zlion.controller.UavController.addUavLocation(..))")
    public void checkLocation(){

    }

    @After("checkLocation()")
    public void reportError(JoinPoint joinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String uuid = request.getParameter("uuid");
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        double height = Double.parseDouble(request.getParameter("height"));

        String geohashCode = GeohashUtil.encode(latitude, longitude, 6);

        //发送邮件和短信
        System.out.println("检测 geohashCode:" + geohashCode);
        if (uavService.isApplied(geohashCode, uuid)) {
            System.out.println(">>>>>>>越界了");
            //发送提醒消息
            String userEmail = uavService.findUserEmail(uuid);
            String userPhone = uavService.findUserPhone(uuid);
            try{
                MailUtil mailUtil = new MailUtil("smtp.163.com", "zengzs1995@163.com", "zzs948926865qaz", 465, "zengzs1995@163.com");
                mailUtil.sendMail("Warning", "你的无人机uuid为 " + uuid + " 于" + (new Date()).toString() + "进入禁飞空域，请立即处理", userEmail);
                HttpClientCommonUtil.clientSend(geohashCode, userPhone);
            }catch (EmailException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

        }




    }



}
