package com.zlion.util;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Created by zzs on 10/20/16.
 */
public class HttpClientCommonUtil {

    public static int clientSend(String geohashCode, String mobile) throws Exception{
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://api.sms.cn/sms112/");
        //			post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
        NameValuePair[] data ={ new NameValuePair("ac", "send"),new NameValuePair("uid", "zudaojun"),
                new NameValuePair("pwd", "32c6a0cf76d324515d0a0416f86eab4c"),new NameValuePair("template", "390422"),
                new NameValuePair("mobile", mobile), new NameValuePair("content", "{" + geohashCode + ":\"jd259e\"}")};
        post.setRequestBody(data);

        client.executeMethod(post);
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:"+statusCode);
        for(Header h : headers)
        {
            System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString().getBytes("utf8"));
        System.out.println(result); //打印返回消息状态

        return statusCode;
    }



}
