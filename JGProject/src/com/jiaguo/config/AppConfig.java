package com.jiaguo.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.R.fraction;
import android.content.Context;
import android.view.LayoutInflater;

public class AppConfig {

	public final static int FLAG_SUCCESS = 0;
	
	public final static int FLAG_REQUEST_ERROR = 2;
	public final static int CODE_SUCCESS = 3;
	public final static int REGISTER_SUCCESS = 4;
	public final static int LOGIN_SUCCESS=5;
	public final static int FLAG_INIT_SUCCESS = 6;
	public final static int FLAG_INIT_FAIL = 7;
	public final static int FLAG_PAY_SUCCESS = 8;
	public final static int FLAG_PAY_FAIL =9;
	public final static int WEB_PAY_SUCCESS =10;
	public final static int LOGINOUT_SUCCESS =11;
	public final static int CUT_SUCCESS =12;
	public final static int RESET_SUCCESS = 13;
	public final static int FLAG_SHOW_POPWINDOW = 14;
	public final static int FLAG_PLATFORM_SUCCESS =18;
	public final static int FLAG_FAIL = 20;
	public final static String APP_VER = "1.0";
	public final static String GAME_TIME_ACTION = "com.game.time";
	public static int appId = 0;
	public static String appKey = "";
	public static String ver_id = "";

	public static int userType = 0;
	public static boolean isAppExit = false;
	public static boolean isApkCacheExit = false;
	public static boolean isFirst = true;
	public static boolean isDownload = true;
	public static String GAME_TIME = "GAME_TIME";

	public static boolean ISPORTRAIT = false; // 是否竖屏
	public static String uid = ""; // 用户uid
	public static String userName = "";// 用户名
	public static String gametoken = "";
	public static String time="";
	public static String  ORDERURL = "";//订单地址
	public static String USERURL = "";//用户信息地址
	public static String FPWD ="";//找回密码
	
	public static String Sessid = "";
	public static String Token = "";
	public static Map<String, String> tempMap = new HashMap<String, String>();// 临时保存未激活的注册账号和修改密码页面的用户信息
	public static Map<String, String> loginMap = new HashMap<String, String>();// 临时保存文件系统中的user0信息
	public static Map<String, String> initMap = new HashMap<String, String>();// 临时保存初始化信�?

	public static void saveMap(String user, String pwd, String uid) {
		loginMap.put("user", user);
		loginMap.put("pwd", pwd);
		loginMap.put("uid", uid);
	}

	/**
	 * 清除数据
	 */
	public static void clear() {
		tempMap.clear();
	}

	public static void clearCache() {
		tempMap.clear();
		loginMap.clear();
		initMap.clear();
		isFirst = true;
		isApkCacheExit = false;
	}

	public static List<String> intersect(String[] a, String[] b) {
		List<String> list = new ArrayList<String>(Arrays.asList(b));
		list.retainAll(Arrays.asList(a));
		return list;
	}

	public static int resourceId(Context context, String name, String type) {
		return context.getResources().getIdentifier(name, type,
				context.getPackageName());
	}

	/**
	 * 计算时间
	 */
	public static String GameTime(String begin, String end) {
		String time = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault());
			Date beginDate = fmt.parse(begin);
			Date endDate = fmt.parse(end);
			long between = (endDate.getTime() - beginDate.getTime()) / 1000;
			long minute = between / 60;
			time = minute + "";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return time;
	}
}
