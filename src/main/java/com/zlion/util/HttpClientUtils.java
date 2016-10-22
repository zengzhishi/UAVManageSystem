package com.zlion.util;

/**
 * 引用自cn.etuo.cloud.demo;
 * http://www.etuocloud.com/
 */

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientUtils {

    public static String httpPost(String url, Map<String, String> parameter) throws IOException {
        return httpPost(url, parameter, "UTF-8");
    }

    public static String httpPost(String url, Map<String, String> parameter, String charset) throws IOException {
        NameValuePair[] data = new NameValuePair[parameter.size()];
        Iterator<String> keys = parameter.keySet().iterator();
        for (int i = 0; keys.hasNext(); i++) {
            String key = (String) keys.next();
            String value = (String) parameter.get(key);
            data[i] = new NameValuePair(key, value);
        }
        PostMethod postMethod = new PostMethod(url);
        HttpMethodParams params = postMethod.getParams();
        params.setContentCharset(charset);
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        if (data.length > 0) {
            postMethod.setRequestBody(data);
        }
        HttpClient httpClient = new HttpClient();
        String responseMsg = null;
        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(postMethod);
            if (statusCode == 200) {
                responseMsg = getResponseBodyAsString(postMethod);
                return responseMsg;
            } else {
                throw new IOException("服务端内部错误: " + statusCode + " from " + postMethod.getURI());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getResponseBodyAsString(HttpMethod method) throws IOException {
        String charset = "utf-8";
        InputStream resStream = method.getResponseBodyAsStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(resStream, charset));
        StringBuffer resBuffer = new StringBuffer();
        String resTemp = null;
        while ((resTemp = br.readLine()) != null) {
            resBuffer.append(resTemp);
        }
        return resBuffer.toString();
    }

    /**
     * 生成签名
     *
     * 根据参数名称的ASCII码表的顺序排序。如：foo=1, bar=2, foo_bar=3,   foobar=4排序后的顺序是bar=2, foo=1, foo_bar=3, foobar=4
     * 将排序好的参数名和参数值(以k1=v1&k2=v2...)方式拼装在一起，根据上面的示例得到的结果为：bar=2&foo=1&foo_bar=3&foobar=4
     * 把拼装好的字符串采用utf-8编码，在拼装的字符串后面加上应用的app_secret后，再进行摘要，如：md5(bar=2&foo=1&foo_bar=3&foobar=4+app_secret)
     * 将摘要得到的字节流结果使用十六进制表示，如：hex("helloworld".getBytes("utf-8")) = "68656c6c6f776f726c64"
     */
    public static String genSign(Map<String, String> params, String APP_SECRET)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //TreeMap 默认按key 升序
        Map<String,String> sortMap = new TreeMap<String,String>();
        sortMap.putAll(params);
        //以k1=v1&k2=v2...方式拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> s : sortMap.entrySet()) {
            String k = s.getKey();
            String v = s.getValue();
            if(v.equals("") || v == null){//过滤空值
                continue;
            }
            builder.append(k).append("=").append(v).append("&");
        }
        if (!sortMap.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        //拼接应用的app_secret
        builder.append(APP_SECRET);
        //摘要
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] digest = instance.digest(builder.toString().getBytes("UTF-8"));
        //十六进制表示
        return new String(encodeHex(digest));
    }

    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }

}