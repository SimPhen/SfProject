package com.jiaguo.utils;

import com.jiaguo.activity.JGUserinfoActivity;
import com.jiaguo.common.JGSDK;
import com.jiaguo.config.AppConfig;
import com.jiaguo.sdk.JGAPISDK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;



public class JsInterface {
	private Context mContext;
	
	private Seference mSeference;

	public JsInterface(Context context) {
		mContext = context;
		mSeference = new Seference(context);
	}

	@JavascriptInterface
	public void JavaScriptToJumppassword() {
		Intent intent = new Intent();
		intent.putExtra("url", AppConfig.USERURL);
		intent.setClass(mContext,  JGUserinfoActivity.class);
		mContext.startActivity(intent);
	}
	@JavascriptInterface
	public void JavaScriptSavepassword(String user, String pwd, String uid) {
		mSeference.saveAccount(user, pwd, uid);
		AppConfig.saveMap(user, pwd, uid);
		JGSDK.saveUserToSd(mContext);
	}
	@JavascriptInterface
    public void JavaScriptToJumpLogin(){
    	if(mContext!=null){
    		((Activity)mContext).finish();
    	}
    }
	
}
