package com.example.jiaguosdk;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.testsdk.R;
import com.jiaguo.common.ExitListener;
import com.jiaguo.common.JGSDK;
import com.jiaguo.http.ApiRequestListener;
import com.jiaguo.sdk.JGAPISDK;

public class MainActivity extends Activity implements OnClickListener {
	public Button sfBtn;
	public Button mBtninit;
	public Button mBtnlogin;
	public Button mBtninfo;
	public Button mBtnpay;
	public Button mBtnexit;
	private int appid = 100000;
	private String appkey = "123123";
	private boolean isPlay = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		JGSDK.onCreate(this,appkey);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		sfBtn = (Button) findViewById(R.id.sf_test);
		mBtninit = (Button) findViewById(R.id.jg_initbt);
		mBtnlogin = (Button) findViewById(R.id.jg_loginbt);
		mBtninfo = (Button) findViewById(R.id.jg_info);
		mBtnpay = (Button) findViewById(R.id.jg_paybt);
		mBtnexit = (Button) findViewById(R.id.jg_exitbt);

		sfBtn.setOnClickListener(this);
		mBtninit.setOnClickListener(this);
		mBtnlogin.setOnClickListener(this);
		mBtninfo.setOnClickListener(this);
		mBtnpay.setOnClickListener(this);
		mBtnexit.setOnClickListener(this);
	}

	public void SetBtnText(String txt){
		this.sfBtn.setText(txt);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.sf_test){
			if(isPlay){
				Toast.makeText(getApplicationContext(),"已经在播放中啦！！！",Toast.LENGTH_SHORT).show();
				return;
			}
			this.sfBtn.setText("播放中");
			isPlay = true;
			JGAPISDK.get().startGetSrc(MainActivity.this,appkey, new ApiRequestListener() {
				@Override
				public void onSuccess(Object obj) {
					// //("kk",obj+"");
					if(obj == null){
						return;
					}
					JGSDK.onSetResult(MainActivity.this,obj.toString());
					
				}

				@Override
				public void onError(int statusCode) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		else if (v.getId() == R.id.jg_initbt) {
			
		} else if (v.getId() == R.id.jg_loginbt) {
			
		} else if (v.getId() == R.id.jg_info) {
			/**
			 * 额外信息
			 * 
			 * @param context
			 *            上下文
			 * @param scene_Id
			 *            场景 分别为进入服务器(enterServer)、玩家创建用户角色(createRole)、玩家升级(levelUp)
			 * @param roleId
			 *            角色id
			 * @param roleName
			 *            角色名
			 * @param roleLevel
			 *            角色等级
			 * @param zoneId
			 *            当前登录的游戏区服id
			 * @param zoneName
			 *            当前游戏区服名称
			 * @param balance
			 *            游戏币余额
			 * @param Vip
			 *            当前用户vip等级
			 * @param partyName
			 *            当前用户所属帮派
			 * @param roleCTime
			 *            单位为秒，创建角色的时间
			 * @param roleLevelMTime
			 *            单位为秒，角色等级变化时间
			 */
			JGSDK.setExtData(this, "enterServer", "11", "嘉果", "99", "1", "1区",
					"80", "8", "逍遥", "21322222", "54456588");

		} else if (v.getId() == R.id.jg_paybt) {
		

		} else if (v.getId() == R.id.jg_exitbt) {
			JGSDK.exit(this, new ExitListener() {

				@Override
				public void fail(String msg) {
					// TODO Auto-generated method stub

				}

				@Override
				public void ExitSuccess(String msg) {
					// TODO Auto-generated method stub
					System.exit(0);
				}
			});
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		JGSDK.onActivityResult(MainActivity.this, requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		JGSDK.onDestroy(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JGSDK.onPause(this);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		JGSDK.onRestart(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JGSDK.onResume(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		JGSDK.onstop(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		JGSDK.onNewIntent(intent);
	}
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			JGSDK.exit(MainActivity.this, new ExitListener() {
//				
//				@Override
//				public void fail(String arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void ExitSuccess(String arg0) {
//					// TODO Auto-generated method stub
//					
//					
//					System.exit(0);
//				}
//			});
//			
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}
