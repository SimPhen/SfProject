package com.jiaguo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import com.jiaguo.config.WebApi;


import android.util.Log;

/**
 * API 响应结果解析工厂类，所有的API响应结果解析需要在此完成。
 * 
 */
public class ApiResponseFactory {

	public static final boolean DEBUG = false;
	public static final String LOGTAG = "ApiResponseFactory";

	/**
	 * 处理response
	 * 
	 * 
	 * @param action
	 * @param response
	 * @return
	 */
	public static Object handleResponse(String webApi, HttpResponse response) {
		String data = inputStreamToString(HttpUtils
				.getInputStreamResponse(response));
		data = clearBom(data);
		Object result = null;
		
		try {
			if(webApi.equals(WebApi.ACTION_SRC)) {
				result = JSONParse.parseSrc(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	private static String inputStreamToString(InputStream in) {

		try {
			StringBuilder builder = new StringBuilder();
			BufferedReader bufferedReader2 = new BufferedReader(
					new InputStreamReader(in));
			
			for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
					.readLine()) {
				builder.append(s);
			}
			return builder.toString();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return "";
	}
	
	private static String clearBom(String data){
		if(data.startsWith("\ufeff")){
			return data.substring(1);
		}
		return data;
	}

}
