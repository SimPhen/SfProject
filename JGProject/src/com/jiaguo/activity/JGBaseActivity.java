package com.jiaguo.activity;


import java.io.IOException;
import java.util.Properties;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class JGBaseActivity extends Activity {

	public String appKey = "", serverId = "", verId = "";
	public int appId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
	
	}

	private void initData() {
	
	}
	/**
	 * 接口返回数据处理
	 */
	public void sendData(int num, Object data, Handler callback) {
		Message msg = callback.obtainMessage();
		msg.what = num;
		msg.obj = data;
		msg.sendToTarget();
	}

	/**
	 * toast 提示信息
	 * 
	 * @param msg
	 */
	public void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Intent页面跳转
	 */
	public void changePage(Activity activity, Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		Bundle bundle = new Bundle();
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

	/**
	 * 显示登录成功
	 * 
	 * @param userName
	 * @param xOffset
	 * @param yOffset
	 * @param duration
	 */
	public void showLoginFinish(String userName, int xOffset, int yOffset,
			int duration) {
	}

	/**
	 * 回调信息
	 */
	public void callBack(String result, String msg, String timestamp,
			String uid, String username, String sign,String token) {
	}
	


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 判断输入框中的数据是否符合格式
	 */
	public boolean verfy(EditText user, EditText pwd) {

		if (user != null && pwd != null) {
			if (user.getText() == null || "".equals(user.getText().toString())) {
				showMsg("请输入账号!");
				return true;
			} else if (pwd.getText() == null
					|| "".equals(pwd.getText().toString())) {
				showMsg("请输入密码!");
				return true;
			}
		}
		if (user == null) {
			if (pwd.getText() == null || "".equals(pwd.getText().toString())) {
				showMsg("请输入密码!");
				return true;
			}
		}

		if (pwd == null) {
			if (user.getText() == null || "".equals(user.getText().toString())) {
				showMsg("请输入账号!");
				return true;
			}
		}
		return false;
	}
}
