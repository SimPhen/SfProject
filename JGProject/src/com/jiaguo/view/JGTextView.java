package com.jiaguo.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jiaguo.activity.JGUserinfoActivity;
import com.jiaguo.common.JGSDK;
import com.jiaguo.config.AppConfig;
import com.jiaguo.http.ApiAsyncTask;
import com.jiaguo.model.Msg;
import com.jiaguo.utils.Utils;

public class JGTextView extends ImageView implements OnClickListener {
	private final String TAG = JGTextView.class.getSimpleName();

	public static int TOOL_BAR_HIGH = 0;
	public static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	private float startX;
	private float startY;
	private float x;
	private float y;

	private float x1;
	private float y1;

	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	private LayoutInflater inflater;
	public static PopupWindow popupWindow;
	public ImageView mUserinfo, mOrder, mExit, mUserinfoone, mOrderone,
			mExitone;
	public RelativeLayout mLeftBox;
	public RelativeLayout mRightBox;
	private int screenwidth, screenheight;
	private float left, right;
	private View view, view1;
	private float dpi;
	private Context mcontext;
	private int mState = 0;
	private WindowManager wm;
	private TimerTask task;
	private Handler handler;
	public static Timer timer;
	private boolean booon = true;
	private static ApiAsyncTask loginouttask;

	public JGTextView(Context context, int state, WindowManager wm) {
		super(context);
		this.setBackgroundColor(Color.argb(0, 255, 255, 255));
		this.setImageDrawable(context.getResources().getDrawable(
				context.getResources().getIdentifier("jg_float", "drawable",
						context.getPackageName())));
		this.inflater = LayoutInflater.from(context);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		screenwidth = display.getWidth();
		screenheight = display.getHeight();
		dpi = metrics.density;

		this.mcontext = context;
		this.mState = state;
		this.wm = wm;
		timer = new Timer(true);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {

				case AppConfig.CUT_SUCCESS:

					upView();
					break;
				case AppConfig.LOGINOUT_SUCCESS:

					// 切换账号
					// Msg outmsg =(Msg) msg.obj;
					JGSDK.userlistenerinfo.onLogout("logout");
					// Toast.makeText(mcontext, outmsg.getMsg(),
					// Toast.LENGTH_LONG).show();
					break;
				case AppConfig.FLAG_FAIL:
					try {
						Msg outmsg2 = (Msg) msg.obj;
						Toast.makeText(mcontext, outmsg2.getMsg(),
								Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				}

				super.handleMessage(msg);
			}
		};
		Initwindow();

	}

	public void Initwindow() {
		view = inflater.inflate(
				AppConfig.resourceId(mcontext, "jg_text", "layout"), null);
		view1 = inflater.inflate(
				AppConfig.resourceId(mcontext, "jg_textone", "layout"), null);

		mUserinfo = (ImageView) view.findViewById(AppConfig.resourceId(
				mcontext, "jguserinfo", "id"));
		mOrder = (ImageView) view.findViewById(AppConfig.resourceId(mcontext,
				"jgorder", "id"));
		mExit = (ImageView) view.findViewById(AppConfig.resourceId(mcontext,
				"jgexit", "id"));

		mUserinfo.setOnClickListener(this);
		mOrder.setOnClickListener(this);
		mExit.setOnClickListener(this);

		mUserinfoone = (ImageView) view1.findViewById(AppConfig.resourceId(
				mcontext, "jguserone", "id"));
		mOrderone = (ImageView) view1.findViewById(AppConfig.resourceId(
				mcontext, "jgordeone", "id"));
		mExitone = (ImageView) view1.findViewById(AppConfig.resourceId(
				mcontext, "jgexieone", "id"));
		mUserinfoone.setOnClickListener(this);
		mOrderone.setOnClickListener(this);
		mExitone.setOnClickListener(this);

		popupWindow = new PopupWindow(view, 300, 100);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (JGSDK.timer != null) {
			JGSDK.timer.cancel();
		}
		if (timer != null) {
			timer.cancel();
		}
		// 触摸点相对于屏幕左上角坐标
		x = event.getRawX();
		y = event.getRawY() - TOOL_BAR_HIGH;

		curP.x = event.getX();
		curP.y = event.getY();
		setVisibilityBigfloat();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY();

			downP.x = event.getX();
			downP.y = event.getY();

			x1 = event.getRawX();
			y1 = event.getRawY() - TOOL_BAR_HIGH;

			break;
		case MotionEvent.ACTION_MOVE:
			setVisibilityBigfloat();
			if (!popupWindow.isShowing() && x1 != event.getRawX()
					&& y1 != event.getY()) {
				updatePosition();
			}

			break;
		case MotionEvent.ACTION_UP:

			left = x - curP.x;

			if (Math.abs(x1 - event.getRawX()) <= 5
					&& Math.abs(y1 - (event.getRawY() - TOOL_BAR_HIGH)) <= 5) {
				if (booon) {
					initPopWindow();

				}

			} else {
				if (!popupWindow.isShowing()) {
					if (!AppConfig.ISPORTRAIT) {
						updatePosition();
						startX = startY = 0;
					} else {
						if (AppConfig.ISPORTRAIT) {
							if (0 <= left && left < screenwidth / 2) {
								params.x = 0;

								wm.updateViewLayout(this, params);
							} else {
								params.x = (int) (screenwidth);

								wm.updateViewLayout(this, params);
							}
						}
					}
				}
			}

			if (!popupWindow.isShowing()) {
				upPoll();
				TimerTask task = new TimerTask() {
					public void run() {
						Message message = new Message();
						message.what = AppConfig.CUT_SUCCESS;
						handler.sendMessage(message);
					}
				};
				timer = new Timer(true);
				timer.schedule(task, 3000, 3000);
			}
			break;
		}
		return true;
	}

	private void initPopWindow() {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
			return;
		}
		if (AppConfig.ISPORTRAIT) {
			// 竖屏
			port();
		} else {
			// 横屏
			land();
		}

	}

	/**
	 * 竖屏
	 */
	public void port() {
		if (0 <= left && left < screenwidth / 2) {
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					(int) (55 * dpi + 0.5f));
			if ((screenwidth - left) <= (int) (300 * dpi + 0.5f)) {
				popupWindow.showAsDropDown(this);
			} else {
				popupWindow.showAtLocation(this, Gravity.LEFT, mState, 0);
			}
			// popupWindow.showAtLocation(this, Gravity.LEFT, mState, 0);
		} else {
			popupWindow = new PopupWindow(view1, LayoutParams.WRAP_CONTENT,
					(int) (55 * dpi + 0.5f));
			// popupWindow.showAtLocation(this, Gravity.RIGHT, mState, 0);
			if (left < (int) (300 * dpi + 0.5f)) {
				popupWindow.showAsDropDown(this);
			} else {
				popupWindow.showAtLocation(this, Gravity.RIGHT, mState, 0);
			}
		}
	}

	/**
	 * 横屏
	 */
	public void land() {
		if (left <= screenwidth / 2) {
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					(int) (55 * dpi + 0.5f));
			if ((screenwidth - left) <= (int) (300 * dpi + 0.5f)) {
				popupWindow.showAsDropDown(this);
			} else {
				popupWindow.showAtLocation(this, Gravity.LEFT, mState, 0);
			}
		} else {
			popupWindow = new PopupWindow(view1, LayoutParams.WRAP_CONTENT,
					(int) (55 * dpi + 0.5f));
			if (left < (int) (300 * dpi + 0.5f)) {
				popupWindow.showAsDropDown(this);
			} else {
				popupWindow.showAtLocation(this, Gravity.RIGHT, mState, 0);
			}
		}
	}

	// 更新浮动窗口位置参数
	private void updatePosition() {
		// View的当前位置
		params.x = (int) (x - startX);
		params.y = (int) (y - startY);
		wm.updateViewLayout(this, params);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == AppConfig.resourceId(mcontext, "jguserinfo", "id")) {
			// 用户信息
			if (TextUtils.isEmpty(AppConfig.USERURL)) {
				return;
			}
			turnToIntent(AppConfig.USERURL, JGUserinfoActivity.class);
		} else if (v.getId() == AppConfig.resourceId(mcontext, "jgorder", "id")) {
			// 用户订单
			if (TextUtils.isEmpty(AppConfig.ORDERURL)) {
				return;
			}
			turnToIntent(AppConfig.ORDERURL, JGUserinfoActivity.class);
		} else if (v.getId() == AppConfig.resourceId(mcontext, "jgexit", "id")) {
			// 用户退出
			 Loginout();
			
		} else if (v.getId() == AppConfig.resourceId(mcontext, "jguserone",
				"id")) {

			// 用户信息
			if (TextUtils.isEmpty(AppConfig.USERURL)) {
				return;
			}
			turnToIntent(AppConfig.USERURL, JGUserinfoActivity.class);

		} else if (v.getId() == AppConfig.resourceId(mcontext, "jgordeone",
				"id")) {
			// 用户订单
			if (TextUtils.isEmpty(AppConfig.ORDERURL)) {
				return;
			}
			turnToIntent(AppConfig.ORDERURL, JGUserinfoActivity.class);

		} else if (v.getId() == AppConfig.resourceId(mcontext, "jgexieone",
				"id")) {
			 Loginout();

		//	Toast.makeText(mcontext, "此功能暂未开通", Toast.LENGTH_LONG).show();
		//	return;
			// 用户退出
		}
		JGSDK.isShow = false;
		JGSDK.icon.setVisibility(View.GONE);

	}

	@SuppressLint("NewApi")
	private void turnToIntent(String url, Class<?> cls) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("url", url);
		intent.setClass(mcontext, cls);
		mcontext.startActivity(intent);

	}

	/**
	 * /** 显示大浮点图像
	 */

	public void setVisibilityBigfloat() {

		if (mState < x || x < screenwidth - mState / 2)

		{
			if (dpi > 0 && dpi <= 1) {
				params.width = 60;
				params.height = 60;

			}
			if (dpi > 1 && dpi <= 1.5) {
				params.width = 80;
				params.height = 80;

			}
			// 三星n7000 红米 2s
			if (dpi > 1.5 && dpi <= 2) {
				params.width = 100;
				params.height = 100;

			}

			if (dpi > 2 && dpi <= 2.5) {
				params.width = 110;
				params.height = 110;
			}
			// 华为H60-L01
			if (dpi > 2.5) {
				params.width = 145;
				params.height = 145;

			}
			wm.updateViewLayout(this, params);
			this.setImageDrawable(mcontext.getResources().getDrawable(
					mcontext.getResources().getIdentifier("jg_float",
							"drawable", mcontext.getPackageName())));
		}
	}

	/**
	 * 初始化的时候切换小图标
	 */
	public void upPothot() {
		booon = false;
		if (JGSDK.timer != null) {
			JGSDK.timer.cancel();
		}
		// Log.d("kk", "uupPothot"+ booon);
		// 三星t210
		if (dpi > 0 && dpi <= 1) {
			params.width = 15;
			params.height = 35;

		}
		// 三星gt-i8262
		if (dpi > 1 && dpi <= 1.5) {
			params.width = 20;
			params.height = 45;

		}
		// 三星n7000 红米 2s
		if (dpi > 1.5 && dpi <= 2) {
			params.width = 30;
			params.height = 70;

		}

		if (dpi > 2 && dpi <= 2.5) {
			params.width = 45;
			params.height = 70;
		}
		// 华为H60-L01
		if (dpi > 2.5) {
			params.width = 50;
			params.height = 120;

		}
		//Log.i("kk","params.width"+params.width);
		params.format = PixelFormat.RGBA_8888;
		params.gravity = Gravity.LEFT | Gravity.TOP;

		wm.updateViewLayout(this, params);
		if (x <= screenwidth / 2) {
			this.setImageDrawable(mcontext.getResources().getDrawable(
					mcontext.getResources().getIdentifier("jg_halftuoyuan",
							"drawable", mcontext.getPackageName())));
		} else {
			this.setImageDrawable(mcontext.getResources().getDrawable(
					mcontext.getResources().getIdentifier(
							"jg_halftuoyuanround", "drawable",
							mcontext.getPackageName())));
		}
	}

	/**
	 * 移动切换图片
	 */
	public void upView() {
		booon = false;

		if (timer != null) {
			timer.cancel();

		}
		if (dpi > 0 && dpi <= 1) {
			params.width = 15;
			params.height = 35;

		}
		// 三星gt-i8262
		if (dpi > 1 && dpi <= 1.5) {
			params.width = 20;
			params.height = 45;

		}
		// 三星n7000 红米 2s
		if (dpi > 1.5 && dpi <= 2) {
			params.width = 30;
			params.height = 70;

		}

		if (dpi > 2 && dpi <= 2.5) {
			params.width = 45;
			params.height = 70;
		}
		// 华为H60-L01
		if (dpi > 2.5) {
			params.width = 50;
			params.height = 120;

		}
		
		if (x <= screenwidth / 2) {

			params.x = 0;

			wm.updateViewLayout(this, params);
			this.setImageDrawable(mcontext.getResources().getDrawable(
					mcontext.getResources().getIdentifier("jg_halftuoyuan",
							"drawable", mcontext.getPackageName())));

		}
		if (x > screenwidth / 2) {

			params.x = screenwidth;

			wm.updateViewLayout(this, params);
			this.setImageDrawable(mcontext.getResources().getDrawable(
					mcontext.getResources().getIdentifier(
							"jg_halftuoyuanround", "drawable",
							mcontext.getPackageName())));

		}
	}

	/*
	 * 浮点靠边
	 */
	public void upPoll() {
		booon = true;
		if (x <= screenwidth / 2) {

			params.x = 0;

			wm.updateViewLayout(this, params);
		}
		if (x > screenwidth / 2) {

			params.x = screenwidth;

			wm.updateViewLayout(this, params);
		}
	}

	public void Loginout() {
		String ver_id = "";
		try {
			ver_id = Utils.getAgent(mcontext);
					
		} catch (Exception e) {
			// TODO: handle exception
		}
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

}
