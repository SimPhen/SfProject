package com.jiaguo.activity;

import com.jiaguo.common.JGSDK;
import com.jiaguo.config.AppConfig;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class JGUserinfoActivity extends JGBaseActivity implements OnClickListener {
	private WebView mWebview;
	private ImageView mBack;
	private String murl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(AppConfig.resourceId(this, "jguserinfo", "layout"));
		murl = getIntent().getStringExtra("url");
		intView();
	}

	private void intView() {
		// TODO Auto-generated method stub
		mWebview = (WebView) findViewById(AppConfig.resourceId(this,
				"jg_webview", "id"));
		mBack = (ImageView) findViewById(AppConfig.resourceId(this,
				"jg_iphoneback", "id"));
		mBack.setOnClickListener(this);

		mWebview.setVerticalScrollBarEnabled(false);
		mWebview.getSettings().setSupportZoom(false);
		mWebview.getSettings().setSaveFormData(false);
		mWebview.getSettings().setSavePassword(false);
		mWebview.getSettings().setJavaScriptEnabled(true);
		mWebview.getSettings().setBuiltInZoomControls(false);
		mWebview.getSettings().setJavaScriptEnabled(true);
		mWebview.getSettings().setSupportZoom(false);
		mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// 修复一些机型webview无法点击****/
		mWebview.requestFocus(View.FOCUS_DOWN);
		mWebview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}
		});
		// ************************//
		mWebview.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("weixin://wap/pay")) {
					Intent intent = null;

					try {
						intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
						view.getContext().startActivity(intent);
					} catch (Exception e) {
						// Toast.makeText(YeepayWebActivity.this, "请安装微信",
						// Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}

					return true;
				} else {

					return super.shouldOverrideUrlLoading(view, url);
				}
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				// super.onReceivedSslError(view, handler, error);
				handler.proceed();// 接受https所有网站的证书
			}
		});

		mWebview.setWebChromeClient(new WebChromeClient());

		mWebview.loadUrl(murl);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == AppConfig.resourceId(this, "jg_iphoneback", "id")) {
			if (mWebview.canGoBack()) {
				mWebview.goBack();
			} else {
				finish();
				if (JGSDK.icon != null) {
					JGSDK.isShow = true;
					JGSDK.icon.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
			mWebview.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (JGSDK.icon != null) {
			JGSDK.isShow = true;
			JGSDK.icon.setVisibility(View.VISIBLE);
		}
	}
}
