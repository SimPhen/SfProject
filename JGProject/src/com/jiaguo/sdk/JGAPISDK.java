package com.jiaguo.sdk;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.jiaguo.config.AppConfig;
import com.jiaguo.config.WebApi;
import com.jiaguo.http.ApiAsyncTask;
import com.jiaguo.http.ApiRequestListener;
import com.jiaguo.utils.DeviceInfo;

public class JGAPISDK {
	private static JGAPISDK instance;
	
	private static int DEVICE = 1;// 安卓设备

	DeviceInfo deviceInfo;

	private JGAPISDK() {

	}

	public static JGAPISDK get() {

		if (instance == null) {
			instance = new JGAPISDK();
		}
		return instance;
	}
	
	public ApiAsyncTask startGetSrc(Context context,ApiRequestListener listener) {

		if (deviceInfo == null) {
			deviceInfo = new DeviceInfo(context);
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("imei", deviceInfo.getImei() + "");
		return WebApi.startThreadRequest(WebApi.ACTION_SRC, listener,params,
				"1");
	}
}
