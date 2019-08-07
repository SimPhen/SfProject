package com.jiaguo.push;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.jiaguo.common.JGSDK;
import com.jiaguo.config.AppConfig;






import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;

/**
 * 推送服务 使用轮询实现 每隔一段时间发起pull请求，
 * 返回数据后跟本地id匹配，如果是没有接收过的，加载详细数据，显示通知栏消息
 * 
 * @author
 * 
 */

public class PushService extends Service {

	public static final String LOGTAG = "PushService";
	public static boolean DEBUG = true;
	public static long taskTime = 1000 *60*60;// 60分钟请求一次
	public static long taskIcon = 10 * 60 * 1;

	private final static int FLAG_PUSH = 51;
	private final static int ICONVISIBLE = 52;
	private final static int ICONINVISIBLE = 53;
	private final static int GAME_TIME = 54;

	private Timer timer;
	private HttpRequestTask taskThread;
	private NotificationManager mManager;
	
	private Timer iconTimer;
	private Listen listenTherad;

	@Override
	public void onCreate() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		timer = new Timer();
		iconTimer = new Timer();
		taskThread = new HttpRequestTask();
		listenTherad = new Listen();
		// 启动循环任务放在onCreate中，确保只有一个任务在请求
		timer.schedule(taskThread, taskTime, taskTime);
		iconTimer.schedule(listenTherad, taskIcon, taskIcon);
	
	//	BuildConfig.Log.v(LOGTAG, "----------------sijiu_service oncreate-------------");
		super.onCreate();
	}

	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getAction() != null
				&& intent.getAction().equals(AppConfig.GAME_TIME_ACTION)) {
			Bundle bundle = intent.getExtras();
			String isExit = bundle.getString("exit");
			int id = bundle.getInt("id");
			String key = bundle.getString("key");
			String uid = bundle.getString("uid");
			
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public void onDestroy() {

		Log.v(LOGTAG, "onDestroy");

		super.onDestroy();
	}


	/**
	 * 发起http请求连接服务器
	 * 
	 */
	class HttpRequestTask extends TimerTask {

		@Override
		public void run() {
			// 网络可用
			runPushRequest();
		}

	}

	/**
	 * 实时监听浮点
	 */
	class Listen extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			icon();
		}
	}

	/**
	 * 请求push消息
	 */
	private void runPushRequest() {
		// NetManager manager = new NetManager();
		// manager.Push(getApplicationContext(), handler);
		
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

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
		
			switch (msg.what) {
			
			case ICONVISIBLE:
				
				if (JGSDK.icon != null && JGSDK.isShow) {
					JGSDK.icon.setVisibility(View.VISIBLE);
				}
				break;
			case ICONINVISIBLE:
				if (JGSDK.icon != null && JGSDK.isShow) {
					JGSDK.icon.setVisibility(View.GONE);
				}
				break;
			}
		}
	};

	

	public void icon() {
		try {
			// 获取包名
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getApplicationContext().getPackageName(), 0);
			String packageName = packageInfo.packageName;
			// 判断包名是否是顶层
			ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcessInfos = manager
					.getRunningAppProcesses();
			for (RunningAppProcessInfo appProcess : appProcessInfos) {
				if (appProcess.processName.equals(packageName)) {
					if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
						handler.sendEmptyMessage(ICONVISIBLE);
						break;
					} else {
						handler.sendEmptyMessage(ICONINVISIBLE);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
}
