package com.jiaguo.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.jiaguo.utils.AESCrypt;
import com.jiaguo.utils.SecurityUtils;

import android.util.Log;

/**
 * 获取API请求内容  的工厂方法
 */
public class ApiRequestFactory {

	public static final boolean DEBUG = true;
	public static final String LOGTAG = "ApiRequestFactory";


	/**
	 * 
	 * 获取Web Api HttpRequest
	 * @param url
	 * @param httpType  
	 * @param entity
	 * @param config
	 * @return
	 */
	public static HttpUriRequest getRequest(String url, String httpType,
			HashMap<String, Object> param, String appKey) {
		// 得到hashmap的key集合，进行排序
		Object[] keyArr = param.keySet().toArray();
		Arrays.sort(keyArr);
		// http get
		if (httpType.equals("get")){
			StringBuilder md5Str = new StringBuilder();
			StringBuilder urlBuilderData = new StringBuilder(url);
		    StringBuilder urlBuilder=new StringBuilder(url);
		    urlBuilderData.append("?");
		    urlBuilder.append("?data=");
			for (Object key : keyArr){
				String value = (String) param.get(key)==null?"":(String) param.get(key);
				// 对特殊字符进行encode
				try {
					urlBuilderData.append(key).append("=")
							.append(URLEncoder.encode(value, "UTF-8"))
							.append("&");
				
						
					
					// md5Str.append(URLEncoder.encode(value, "UTF-8"));
					
				} catch (UnsupportedEncodingException e) {

					e.printStackTrace();
				}
			}

			md5Str.append(appKey);
//			try {
//				urlBuilderData.append("appKey=").append(URLEncoder.encode(appKey, "UTF-8")).append("&");
//			} catch (UnsupportedEncodingException e1) {
//				e1.printStackTrace();
//			}

			String md5ResultString = SecurityUtils.getMD5Str(md5Str.toString());

			urlBuilderData.append("sign=").append(md5ResultString);

			HttpGet httpGet = null;
//            try {
//            	byte[] encryptData=RSAUtils.encryptByPublicKey(urlBuilderData.toString().getBytes("utf-8"), RSAUtils.getPublicKey())  ;
//            	urlBuilder.append(Base64Utils.encode(encryptData));
//            } catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			httpGet = new HttpGet(urlBuilderData.toString());
  //        Log.i("kk", "getqing"+urlBuilderData.toString());
			return httpGet;
		}

		// http post
		if (httpType.equals("post")) {

			final ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
			final ArrayList<NameValuePair> postParams2 = new ArrayList<NameValuePair>();
			StringBuilder md5Str = new StringBuilder();
			StringBuilder urlBuilderData = new StringBuilder();
		    StringBuilder urlBuilder=new StringBuilder();
			for (Object key : keyArr) {
				String value = (String) param.get(key);
					postParams.add(new BasicNameValuePair((String) key,
							value ));
					
				
					md5Str.append(value);

					
			} 
			md5Str.append(appKey);
			String md5ResultString = SecurityUtils.getMD5Str(md5Str.toString());
			postParams.add(new BasicNameValuePair("sign", md5ResultString));
		//	Log.i("kk", md5ResultString);
		    urlBuilderData.append("sign=").append(md5ResultString);
		    
		    //postParams.add(new BasicNameValuePair("debug", "1"));
		    String data = null;
		    try {
		        //    Log.i("ee","12"+ params.toString());
		         //   AESCrypt aesc = new AESCrypt();
		          //  data=  aesc.encrypt(postParams.toString());
		          //  Log.i("ee", "1255" +  aesc.encrypt(params.toString()));
		        //    encrypted = Aes.bytesToHex(aes.encrypt("gggg"));
		        } catch (Exception e) {
		            e.printStackTrace();
		         //   Log.i("ee", "77" + encrypted+e.toString());
		        }
		   // postParams2.add(new BasicNameValuePair("data", data));
			HttpPost httpPost = new HttpPost(url);//new HttpPost("www.baidu.com");//new HttpPost(url);
			Log.i("test===",""+postParams+"============="+httpPost);
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(postParams,
						HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Log.i("kk", "post params:" + postParams.toString());
			return httpPost;
		}

		return null;

	}
}
