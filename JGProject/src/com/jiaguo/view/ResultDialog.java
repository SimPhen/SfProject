package com.jiaguo.view;


import com.jiaguo.config.AppConfig;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultDialog extends Dialog implements OnClickListener {

	private View mView;
	private TextView contentText;
	private Context mContext;
	private String mText;
	private Button button,button_e;
	private ResultListener mListener;
	private boolean flag;
	private TimeCount time;
	public ResultDialog(Context context, int theme, String text,
			ResultListener listener) {
		super(context, theme);
		this.mContext = context;
		this.mText = text;
		this.mListener = listener;
		this.flag=flag;
		this.mView = LayoutInflater.from(context).inflate(
				AppConfig.resourceId(context, "jgrecharge_result", "layout"),
				null);
		time = new TimeCount(3000, 1000);//控制弹窗显示时间
		time.start();
	}

	public interface ResultListener {
		public void onClick(String v);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(mView);
		button = (Button) findViewById(AppConfig.resourceId(mContext,
				"jgresultbutton", "id"));
		
		contentText = (TextView) findViewById(AppConfig.resourceId(mContext,
				"jg_result", "id"));
		contentText.setText(mText);
		button.setOnClickListener(this);
	
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//mListener.onClick(v);
	}

	class TimeCount extends CountDownTimer {
		/**
		 * 构造方法
		 * 
		 * @param millisInFuture
		 *            总倒计时长 毫秒
		 * @param countDownInterval
		 *            倒计时间隔
		 */
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			
		}

		@Override
		public void onFinish() {// 计时结束
			mListener.onClick("close");
		}
	}
	
	
}
