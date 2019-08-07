package com.jiaguo.http;

import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.util.Log;

import com.jiaguo.config.WebApi;



/**
 *
 * 
 * 发起请求时，调用{@link #cancel(boolean)} 可以取消请求，不返回结果
 * 
 */
public class JiaGuoApiTask extends ApiAsyncTask {

	private static final String LOGTAG = "SiJiuApiTask";
	// 超时（网络）异常
	public static final int TIMEOUT_ERROR = 0;
	// 业务异常
	public static final int BUSSINESS_ERROR = 1;

	private String webApi;
	private ApiRequestListener listener;
	private HashMap<String, Object> parameter;
	private String appKey;
	private HttpClient client;
	Object result = null;
	HttpResponse response = null;
	HttpUriRequest request = null;

	// private Context mContext;

	public JiaGuoApiTask(String webApi, ApiRequestListener listener,
			HashMap<String, Object> param, String appKey) {

		this.webApi = webApi;
		this.listener = listener;
		this.parameter = param;
		this.appKey = appKey;
		setHttpClient();

	}

	@Override
	public void run() {
		onPostExecute(doInBackground());
	}

	private Object doInBackground() {

		// 不判断网络。
		// if (!Utils.isNetworkAvailable(mContext)) {
		// return TIMEOUT_ERROR;
		// }
		
		try {
			request = ApiRequestFactory.getRequest(webApi, WebApi.HttpTypeMap.get(webApi),
					parameter, appKey);
			response = client.execute(request);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				// 非正常返回
				request.abort();
				return statusCode;
			}
			// 读取服务器返回数据
			result = ApiResponseFactory.handleResponse(webApi, response);

			// if (result == null) {
			// BuildConfig.Log.e(LOGTAG, "parse result error");
			// return BUSSINESS_ERROR;
			// }

			return result;

		} catch (Exception e) {
			Log.i("lsf","time out");
			return TIMEOUT_ERROR;
		} finally {
			// 释放资源
			if (request != null) {
				request.abort();
			}
			if (response != null) {
				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						entity.consumeContent();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 最后关闭
			client.getConnectionManager().shutdown();
		}

	}

	private void onPostExecute(Object result) {
		if (listener == null) {
			return;
		}
		// if (result == null) {
		// listener.onError(BUSSINESS_ERROR);
		// return;
		// }
		if (result instanceof Integer) {
			Integer statusCode = (Integer) result;

			// 错误码
			if (!handleResult(statusCode)) {
				listener.onError(statusCode);
				return;
			}
		}
		listener.onSuccess(result);
	}

	private boolean handleResult(int statusCode) {

		if (statusCode == 200) {
			return true;
		}
		return false;
	}

	/**
	 * 取消任务，中止http请求，{@link ApiRequestListener}不返回任何结果
	 * 
	 * @param boolean true
	 */
	public void cancel(boolean b) {

		// request.abort();
		// client.getConnectionManager().closeExpiredConnections();
		// client.getConnectionManager().shutdown();

		// client.close();// 关闭
		client.getConnectionManager().shutdown();
		listener = null;

	}

	private void setHttpClient() {
		client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略

		// 设置 连接超时时间

	}
}
