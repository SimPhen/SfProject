package com.jiaguo.view;




import com.jiaguo.config.AppConfig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class Exitdialog extends Dialog implements android.view.View.OnClickListener {
	private Context mContext;
	private View mView;
	private  Exitdialoglistener  mExitdialoglistener;
	private Button mExitebt;
	private Button mCancelbt;

	public Exitdialog(Context context, int theme,  Exitdialoglistener  exitListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mExitdialoglistener = exitListener;
		this.mView = LayoutInflater.from(context).inflate(
				AppConfig.resourceId(context, "jgexit_dialog", "layout"),
				null);
	}
	public interface Exitdialoglistener {
		public void onClick(View view);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(mView);
		mExitebt = (Button) findViewById(AppConfig.resourceId(mContext,"dialog_exit", "id"));
		mCancelbt = (Button)findViewById(AppConfig.resourceId(mContext,"dialog_cancel", "id"));
		mExitebt.setOnClickListener(this);
		mCancelbt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	mExitdialoglistener.onClick(v);
	}

}
