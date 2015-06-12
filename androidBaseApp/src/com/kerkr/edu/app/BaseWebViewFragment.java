package com.kerkr.edu.app;

import com.kerkr.edu.log.Logs;
import com.kerkr.edu.utill.NetChecker;
import com.ytjojo.androidapplib.R;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.webkit.WebSettings.RenderPriority;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-2-4]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class BaseWebViewFragment extends BaseFragment {
    public WebView mWebView;
    
    private boolean mIsWebViewAvailable;
    
    private boolean isBlockNetworkImage;
    
    public String loadUrl;
    
    
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }
    
    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }
    
    /**
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable
    Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        showProgressBar("正在加载页面...");
    }
    
    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    @Override
    public void onDestroyView() {
        mIsWebViewAvailable = false;
        super.onDestroyView();
    }
    
    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
    
    public void setWebView(WebView webView) {
        if (mWebView != null && mWebView != webView) {
            mWebView.destroy();
        }
        this.loadUrl = getUrl();
        this.mWebView = webView;
        buildeWebView();
        mIsWebViewAvailable = true;
    }
    
    private void buildeWebView() {
        String userAgentString = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(userAgentString + "/AppServerUXian_ANDROID");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);//增加渲染优先级
        
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setWebViewClient(new MyWebViewClient());
        
        mWebView.setOnKeyListener(new OnKeyListener() {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) { //表示按返回键 时的操作  
                        if (mWebView.canGoBack()) {
                            
                            mWebView.goBack(); //后退 
                            return true;
                        }
                    }
                }
                return false;
                
            }
        });
        if (!TextUtils.isEmpty(loadUrl)) {
            mWebView.loadUrl(loadUrl);
        }
    }
    
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
        showProgressBar("正在加载页面...");
    }
    
    /**
     * Gets the WebView.
     */
    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }
    private View mVideoProgressView;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!NetChecker.isConnected(mActivity)) {
                return true;
            }
            loadUrl = url;
            view.loadUrl(url);
            mWebView.getSettings().setBlockNetworkImage(true);//把图片加载放在最后来加载渲染
            isBlockNetworkImage = true;
            //            if (Build.VERSION.SDK_INT >= 19) {
            //                myWebView.getSettings().setLoadsImagesAutomatically(true);
            //            } else {
            //                myWebView.getSettings().setLoadsImagesAutomatically(false);
            //            }
            
            return true;
        }
        
        /**
         * @param view
         * @param handler
         * @param error
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // TODO Auto-generated method stub
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
            
            super.onPageFinished(view, url);
            //            if(!view.getSettings().getLoadsImagesAutomatically()) {
            //                view.getSettings().setLoadsImagesAutomatically(true);
            //            }
            hideProgressBar();
        }
        
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
        
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            if (isBlockNetworkImage) {
                view.getSettings().setBlockNetworkImage(false);
                isBlockNetworkImage = false;
            }
        }
    }
    
    WebChromeClient mChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            onGetPageTitle(view, title);
        }
        
        public boolean onJsAlert(WebView view, String url, String message, android.webkit.JsResult result) {
            BaseWebViewFragment.this.onJsAlert(url, message, result);
            result.cancel();
            return true;
        };

        @Override
        public View getVideoLoadingProgressView() {
            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(mActivity);
                mVideoProgressView = inflater.inflate(R.layout.loading_dialog, null);
            }
            return mVideoProgressView;
        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mActivity.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }
        
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            //Log.i(LOGTAG, "here in on ShowCustomView");
            mWebView.setVisibility(View.GONE);

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mCustomView = view;
            mCustomViewCallback = callback;
        }

        @Override
        public void onHideCustomView() {
            System.out.println("customview hideeeeeeeeeeeeeeeeeeeeeeeeeee");
            if (mCustomView == null)
                return;

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            mCustomView = null;
            mCustomViewCallback.onCustomViewHidden();

            mWebView.setVisibility(View.VISIBLE);
            mWebView.goBack();
        }

        
    };
    
//    注:这里一定要注意一点，在调用设置Cookie之后不能再设置
//    Java代码  收藏代码
//    webView.getSettings().setBuiltInZoomControls(true);  
//    webView.getSettings().setJavaScriptEnabled(true); 
    
    /**
     * before webView.loadUrl(url);
     * <功能详细描述>
     * @param domainNameUrl
     * @param strings [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void syncCookie( String domainNameUrl, String... strings) {
        CookieSyncManager.createInstance(mActivity);
        //CookieSyncManager.getInstance().startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        
        for (String s : strings) {
//            cookieManager.setCookie(getString(getResources().getIdentifier(domainNameUrl,"string", mActivity.getPackageName())), s);  
            cookieManager.setCookie(domainNameUrl, s);

        }
        Logs.d(cookieManager.getCookie(domainNameUrl));
        CookieSyncManager.getInstance().sync();
        //    CookieSyncManager.getInstance().stopSync();
    }
    /**
     * <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void removeCookie() {
        CookieSyncManager.createInstance(mActivity);  
        CookieManager cookieManager = CookieManager.getInstance(); 
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();  
    }


    public WebBackForwardList getHistoricalList() {
        return mWebView.copyBackForwardList();
    }

    public  String getHistoricalUrl(WebView webView) {
        WebBackForwardList webBackForwardList = getHistoricalList();
        return webBackForwardList.getItemAtIndex(webBackForwardList.getCurrentIndex() - 1).getUrl();

    }
    
    abstract String getUrl();
    
    abstract void onGetPageTitle(WebView view, String title);
    
    public void onJsAlert(String url, String message, android.webkit.JsResult result) {
        
    }
}