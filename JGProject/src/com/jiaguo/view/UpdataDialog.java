package com.jiaguo.view;


import com.jiaguo.config.AppConfig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class UpdataDialog extends Dialog implements OnClickListener {

	private View mView;
	private WebView webview;
	private Context mContext;
	private String mText;
	private Button button,button_e;
	private UpdataListener mListener;
	private boolean flag;
	public UpdataDialog(Context context, int theme, String text,boolean flag,
			UpdataListener listener) {
		super(context, theme);
		
		this.mContext = context;
		this.mText = text;
		this.mListener = listener;
		this.flag=flag;
		this.mView = LayoutInflater.from(context).inflate(
				AppConfig.resourceId(context, "jgupdate_dialog", "layout"),
				null);
		// TODO Auto-generated constructor stub
	}
	public interface UpdataListener {
		public void onClick(View v);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mListener.onClick(v);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(mView);
		button = (Button) findViewById(AppConfig.resourceId(mContext,
				"next_button_updata", "id"));
		button_e = (Button) findViewById(AppConfig.resourceId(mContext,
				"button_updata", "id"));
		webview = (WebView) findViewById(AppConfig.resourceId(mContext,
				"result_updata", "id"));
		//contentText.setText(Html.fromHtml(mText));
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webview.getSettings().setUseWideViewPort(true);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true); 
		//webview.loadDataWithBaseURL (null,mText,"text/html", "UTF-8",null);
		webview.loadUrl(mText);
		button.setOnClickListener(this);
		button_e.setOnClickListener(this);
		if(flag){
			button.setVisibility(View.GONE);
		}
	}

}
