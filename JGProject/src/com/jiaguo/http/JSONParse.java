package com.jiaguo.http;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParse {

	private static final String LOGTAG = "JSONParse";

	
	public static Object parseSrc(String data) throws JSONException {
		JSONObject jsonObject = new JSONObject(data);
		Log.i("lsf parseSrc====","" + data);
		boolean r = jsonObject.optBoolean("result");
		String msg = jsonObject.optString("msg");
		Log.i("parse msg====","" + msg);
		String src = "";
		if (r) {
			src = jsonObject.optString("data");
		}
		return src;
	}
}
