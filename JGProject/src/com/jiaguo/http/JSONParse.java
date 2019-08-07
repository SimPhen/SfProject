package com.jiaguo.http;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParse {

	private static final String LOGTAG = "JSONParse";

	
	public static Object parseSrc(String data) throws JSONException {
		JSONObject jsonObject = new JSONObject(data);
		Log.i("lsf parseSrc====","" + data);
		String src = jsonObject.optString("src");
		if (src == null) {
			src = "";
		}
		return src;
	}
}
