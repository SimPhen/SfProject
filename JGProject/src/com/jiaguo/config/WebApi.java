package com.jiaguo.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import com.jiaguo.http.ApiAsyncTask;
import com.jiaguo.http.ApiRequestListener;
import com.jiaguo.http.JiaGuoApiTask;

public class WebApi {

	private static final String LOGTAG = "WebApi";

	public static final String HOST = "http://39.107.14.77:80/gamesdk/index.php";//"http://apisdk.szkuyou.cn";
	// public static final String HOST = "http://139.196.255.139";//测试用

	public static Map<String, String> HttpTypeMap = new HashMap<String, String>();
	/**
	 * 接口名称配置信息
	 */

	//获取src接口
	public static final String ACTION_SRC = HOST + "/Api/Index/GetSrcII";
	
	/**
	 * 接口请求方式配置
	 */
	static {
		HttpTypeMap.put(ACTION_SRC, "post");
	}

	/**
	 * 后台启动http连接，使用Thread实现
	 * 
	 * @param context
	 * @param action
	 * @param listener
	 * @param params
	 */
	public static ApiAsyncTask startThreadRequest(String webApi,
			ApiRequestListener listener, HashMap<String, Object> params,
			String appKey) {

		ApiAsyncTask task = new JiaGuoApiTask(webApi, listener, params, appKey);
		task.start();

		return task;
	}

}
