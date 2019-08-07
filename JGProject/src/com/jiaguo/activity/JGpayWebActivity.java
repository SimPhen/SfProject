package com.jiaguo.activity;

import java.net.URISyntaxException;

import com.jiaguo.config.AppConfig;
import com.jiaguo.utils.JsInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class JGpayWebActivity extends Activity implements OnClickListener {
	private String url = "";
	private int type;
	private LinearLayout progress;
	private WebView webView;
	private LinearLayout backBtn;
	private final static int FLAG_WEB_SUCCESS = 22;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		Bundle bundle = this.getIntent().getExtras();
	//	oritation = bundle.getString("oritation");
		setContentView(getResources().getIdentifier("jgrecharge_web_land",
				"layout", getPackageName()));
		url = bundle.getString("url");
		type = bundle.getInt("type");
		initPage();
		initWebView();
		//Log.i("kk","url"+url);
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		webView.setVerticalScrollBarEnabled(false);
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(false);
	     webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// 修复一些机型webview无法点击****/
		webView.requestFocus(View.FOCUS_DOWN);
		webView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
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
		webView.setWebViewClient(new WebViewClient() {
		
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				// super.onReceivedSslError(view, handler, error);
				handler.proceed();// 接受https所有网站的证书
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			if (url.startsWith("weixin://wap/pay")) {
                    Intent intent = null;

                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        view.getContext().startActivity(intent);
                    } catch (Exception e) {
                    	Toast.makeText(JGpayWebActivity.this, "请安装微信", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                 
                   return true;
                } else if(!url.startsWith("weixin://wap/pay")&&!url.startsWith("http:")&&!url.startsWith("https:")){
                	   Intent intenturl = new Intent(Intent.ACTION_VIEW,
          						Uri.parse(url));
          				
          				startActivity(intenturl);
          			  return true;
                }
                else {

                    return super.shouldOverrideUrlLoading(view, url);
                }

				
			}

		}
		
				);

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					Message msg = new Message();
					msg.what = FLAG_WEB_SUCCESS;
					handler.sendMessage(msg);
				}
			}
		});

		webhtml(url);
	}

	private void webhtml(String url) {
		try {
			
			webView.loadUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FLAG_WEB_SUCCESS:
				
				break;

			default:
				break;
			}
		}

	};

	private void initPage() {
		
		webView = (WebView) findViewById(getResources().getIdentifier(
				"webview", "id", getPackageName()));
		backBtn = (LinearLayout) findViewById(getResources().getIdentifier(
				"web_backbtn", "id", getPackageName()));

		backBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == getResources().getIdentifier("web_backbtn", "id",
				getPackageName())) {
			sendData(type);
			this.finish();
		}
	}

	private void sendData(int type) {
		Intent intent = new Intent();
		intent.putExtra("result", "支付页面关闭，支付结果请查看订单记录!");
		setResult(type, intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			sendData(type);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
