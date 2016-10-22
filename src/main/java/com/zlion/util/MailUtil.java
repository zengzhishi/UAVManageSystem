package com.zlion.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;

/**
 * Created by zzs on 10/20/16.
 */
public class MailUtil {


    private String mailHost;
    private String username;
    private String password;
    private int port = 465;
    private String emailFrom;

    private Email email;

    public MailUtil(String mailHost, String username, String password, int port, String emailFrom) throws EmailException{
        this.mailHost = mailHost;
        this.username = username;
        this.password = password;
        this.port = port;
        this.emailFrom = emailFrom;
        this.getEmailConnect();
    }


    private void getEmailConnect() throws EmailException{
        this.email = new SimpleEmail();
        this.email.setHostName(this.mailHost);
        this.email.setSmtpPort(this.port);
        this.email.setAuthenticator(new DefaultAuthenticator(username, password));
        this.email.setSSLOnConnect(true);
        this.email.setFrom(this.emailFrom);
    }


    public void sendMail(String subject, String msg, String sendToEmail) throws EmailException{

        this.email.setSubject(subject);
        this.email.setMsg(msg);
        this.email.addTo(sendToEmail);
        this.email.send();

    }


}
