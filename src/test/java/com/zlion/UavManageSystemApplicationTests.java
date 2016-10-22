package com.zlion;

import com.zlion.util.HttpClientUtils;
import com.zlion.util.MailUtil;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UavManageSystemApplicationTests {

//	@Autowired
//	private MailUtil mailService;

	private static String url = "http://www.etuocloud.com/gatetest.action";

	//应用 app_key
	private final static String APP_KEY = "a6qA3dSTdyYp4UxUn0Z7SNPGPHrDXx5k";
	//应用 app_secret
	private final static String APP_SECRET = "Ff0V5Xrit7rnDfkpxrdIWhUrYhsf7yj6qxMspDEKPSQkRnmr8B5hevf0u3Cv37Vz";
	//接口响应格式 json或xml
	private final static String FORMAT = "json";

	@Test
	public void contextLoads() {
//		try{
//			MailUtil mailUtil = new MailUtil("smtp.163.com", "zengzs1995@163.com", "zzs948926865qaz", 465, "zengzs1995@163.com");
//			mailUtil.sendMail("Warning", "This is the first massage for uavsystem", "244025155@qq.com");
//		}catch (EmailException e){
//			e.printStackTrace();
//		}


		Map<String, String> params = new HashMap<String, String>();
//		params.put("app_key", APP_KEY);
//		params.put("view", FORMAT);
//		//自定义的邮件
//		params.put("method", "cn.etuo.cloud.api.sms.advance");
//		params.put("out_trade_no", "");
//		params.put("content", "无人机消息");
//		params.put("to", "13402875067");

		params.put("Uid", "zudaojun");
		params.put("Key", "d88e2432e0a60e61fa40");
		params.put("smsMob", "13402875067");
		params.put("smsText", "UAVSystem Info");

//		try{
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
//			post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
//			NameValuePair[] data ={ new NameValuePair("Uid", "zudaojun"),new NameValuePair("Key", "d88e2432e0a60e61fa40"),
//					new NameValuePair("smsMob", "13402875067"),new NameValuePair("smsText", "UAVSystem Info")};
//			post.setRequestBody(data);
//
//			client.executeMethod(post);
//			Header[] headers = post.getResponseHeaders();
//			int statusCode = post.getStatusCode();
//			System.out.println("statusCode:"+statusCode);
//			for(Header h : headers)
//			{
//				System.out.println(h.toString());
//			}
//			String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
//			System.out.println(result); //打印返回消息状态
//
//
//			post.releaseConnection();
//		}catch (Exception e){
//			e.printStackTrace();
//		}


		try{
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod("http://api.sms.cn/sms/");
//			post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
			NameValuePair[] data ={ new NameValuePair("ac", "send"),new NameValuePair("uid", "zudaojun"),
					new NameValuePair("pwd", "32c6a0cf76d324515d0a0416f86eab4c"),new NameValuePair("template", "390422"),
					new NameValuePair("mobile", "13402875067"), new NameValuePair("content", "{\"geohashcode\":\"jd259e\"}")};
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


			post.releaseConnection();
		}catch (Exception e){
			e.printStackTrace();
		}
//		接口密码：32c6a0cf76d324515d0a0416f86eab4c
//		http://api.sms.cn/sms/?ac=send&uid=zudaojun&pwd=32c6a0cf76d324515d0a0416f86eab4c
//				&template=390422&mobile=13402875067
//				&content={"geohashcode":"jd259e"}


//		try{
////			params.put("sign", HttpClientUtils.genSign(params, APP_SECRET));
//			String result = HttpClientUtils.httpPost(url, params);
//			System.out.println(result);
//
//		}catch (UnsupportedEncodingException e){
//			e.printStackTrace();
//		}catch (IOException e){
//			e.printStackTrace();
//		}

	}

}
