package com.kerkr.edu.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WebviewActivity extends Activity {

	private WebView contentWebView = null;
	private TextView msgView = null;

	private static final String APP_CACAHE_DIRNAME = "/webcache";
	private static final String TAG = WebviewActivity.class.getSimpleName();

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.webview_activity);
//		contentWebView = (WebView) findViewById(R.id.webview);
//		msgView = (TextView) findViewById(R.id.msg);
		// 启用javascript
		contentWebView.getSettings().setJavaScriptEnabled(true);
		// 从assets目录下面的加载html
		contentWebView.loadUrl("http://top.baidu.com/detail?b=3&w=angelababy");

		contentWebView.addJavascriptInterface(this, "hello");
		initWebView();
	}

	OnClickListener btnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// 无参数调用
			contentWebView.loadUrl("javascript:javacalljs()");
			// 传递参数调用
			contentWebView.loadUrl("javascript:javacalljswithargs("
					+ "'hello world'" + ")");
		}
	};

	public void startFunction() {
		Toast.makeText(this, "js调用了java函数", Toast.LENGTH_SHORT).show();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
//				msgView.setText(msgView.getText() + "\njs调用了java函数");

			}
		});
	}

	@SuppressLint("NewApi")
	private void initWebView() {
		
		if (isNetworkConnected(this)) {
			contentWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//			Toast.makeText(this, "当前网络正常", Toast.LENGTH_SHORT).show();
		}else {
			contentWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			Toast.makeText(this, "Current network is unavailable, please check your network!", Toast.LENGTH_SHORT).show();
		}
		contentWebView.getSettings().setJavaScriptEnabled(true);
		contentWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
//		contentWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); //设置 缓存模式 
		// 开启 DOM storage API 功能
		contentWebView.getSettings().setDomStorageEnabled(true);
		//开启 database storage API 功能
		contentWebView.getSettings().setDatabaseEnabled(true);
		String cacheDirPath = getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
		Log.i(TAG, "cacheDirPath="+cacheDirPath);
		//设置数据库缓存路径 
		contentWebView.getSettings().setDatabasePath(cacheDirPath);
		//设置  Application Caches 缓存目录 
		contentWebView.getSettings().setAppCachePath(cacheDirPath);
		//开启 Application Caches 功能
		contentWebView.getSettings().setAppCacheEnabled(true);
		contentWebView.setWebViewClient(new MyWebClient());
		contentWebView.setWebChromeClient(new MyWebChromeClient());
		}

	final class MyWebChromeClient extends WebChromeClient {
		public MyWebChromeClient() {
			
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public void onProgressChanged(WebView view, final int newProgress) {
			super.onProgressChanged(view, newProgress);
			// mHandler.post(new Runnable() {
			// @Override
			// public void run() {
			// Toast.makeText(MyWebView.this,
			// "加载进度 "+newProgress,Toast.LENGTH_SHORT).show();
			// }
			// });
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			Log.e(TAG, "标题Title " + title);
		}

		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			super.onReceivedIcon(view, icon);
		}
	}
	final class MyWebClient extends WebViewClient {

		public MyWebClient() {
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// 这个方法是必须要重写的，如果不重写就是会调用系统浏览器自己处理了
			Log.v(TAG, "shouldOverrideUrlLoading " + url);
			view.loadUrl(url);
			return true;
		}

		/**
		 * 这个事件就是开始载入页面调用的，通常我们可以在这设定一个loading的页面，告诉用户程序在等待网络响应。
		 */
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i(TAG, "onPageStarted " + url);
			// 这个主要是先不让图片加载，似乎可以达到快速的目的。
			// 但是在有的网页里面不可以详见：http://hi.baidu.com/goldchocobo/item/9f7b0639f3cd2efe96f88dfb
			// view.getSettings().setBlockNetworkImage(true);
			super.onPageStarted(view, url, favicon);
		}

		/**
		 * 同样道理，我们知道一个页面载入完成，于是我们可以关闭loading条，切换程序动作。
		 */
		@Override
		public void onPageFinished(WebView view, String url) {
			Log.i(TAG, "onPageFinished " + url);
			view.getSettings().setBlockNetworkImage(false);
			super.onPageFinished(view, url);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			Log.i(TAG, "onLoadResource " + url);
			super.onLoadResource(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			Log.e(TAG, "onReceivedError errorCode:" + errorCode
					+ " description" + description + " failingUrl" + failingUrl);
			// finish();
		}

		/**
		 * 接收到Http请求的事件
		 */
		@Override
		public void onReceivedHttpAuthRequest(WebView view,
				HttpAuthHandler handler, String host, String realm) {
			super.onReceivedHttpAuthRequest(view, handler, host, realm);
		}

	}
	public  boolean isNetworkConnected(Activity context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isConnected();
            }
        }
        return false;
    }
	public void startFunction(final String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
//				msgView.setText(msgView.getText() + "\njs调用了java函数传递参数：" + str);

			}
		});
	}
}