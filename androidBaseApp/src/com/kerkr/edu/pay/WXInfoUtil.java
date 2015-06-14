package com.kerkr.edu.pay;

/**
 * Created by ybk on 2015/4/7.
 *  获得code之后：

 调用接口：
 https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
 返回：
 {
 "access_token":"ACCESS_TOKEN", 
 "expires_in":7200, 
 "refresh_token":"REFRESH_TOKEN",
 "openid":"OPENID", 
 "scope":"SCOPE" 
 }

 调用：
 https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
 返回：
 {
 "access_token":"ACCESS_TOKEN", 
 "expires_in":7200, 
 "refresh_token":"REFRESH_TOKEN", 
 "openid":"OPENID", 
 "scope":"SCOPE" 
 }


 检验授权凭证（access_token）是否有效：
 https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID
 成功：
 {
 "errcode":0,"errmsg":"ok"
 }
 失败：
 {
 "errcode":40003,"errmsg":"invalid openid"
 }

 用户信息接口调用：
 https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
 返回：
 {
 openid: "oEIm8jm74sKuyOZ6WpfNfc7s2mQc",
 nickname: "one is all",
 sex: 1,
 language: "zh_CN",
 city: "Hangzhou",
 province: "Zhejiang",
 country: "CN",
 headimgurl: "http://wx.qlogo.cn/mmopen/PiajxSqBRaEJWtLTfOs5wfibPs6VYjUHVnLd47icuaV5mxF7MfRRagorbSenqiblI0AqwoNtkvMUFvJNnSlkvh3PUg/0",
 privilege: [ ],
 unionid: "o7obot1QaYwT4LXlqDacqF65VfB0"
 }
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Message;
import android.text.TextUtils;

import com.kerkr.edu.String.JsonParser;
import com.kerkr.edu.log.VALog;


public class WXInfoUtil {
    
    private String code;
    
    private String refresh_token;
    
    private String access_token;
    
    private String openid;
    private String mWXappSecret;
    private String mWXAppId;
    
    private int num = 0;
    
    WXUserInfoCallBack wxUserInfoCallBack = null;
    
    public interface WXUserInfoCallBack {
        public void wxBackSucceed(WXUserInfoBean wxUserInfoBean);
    }
    
    public void setOnWXUserInfoCallBack(WXUserInfoCallBack wxUserInfoCallBack) {
        this.wxUserInfoCallBack = wxUserInfoCallBack;
    }
    
    public WXInfoUtil(String code) {
        this.code = code;
        this.mWXAppId = Appkey.WX_APP_ID;
        this.mWXappSecret = Appkey.WX_APP_SECRET;
        invokCode();
    }
    
    //通过code get接口
    private void invokCode() {
        new CodeThread().start();
    }
    
    class CodeThread extends Thread implements Runnable {
        @Override
        public void run() {
            super.run();
            num = 0;
            String codeStr = loadTextFromURL("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + mWXAppId+ "&secret="
                    + mWXappSecret + "&code=" + code + "&grant_type=authorization_code");
            VALog.i("===WXInfoUtil==codeStr==" + codeStr);
            WXCodeResponseBean wxCodeRequestBean = JsonParser.getInstance().getObjectFromJson(codeStr, WXCodeResponseBean.class);
            refresh_token = wxCodeRequestBean.getRefresh_token();
            VALog.i("===WXInfoUtil==refresh_token==" + refresh_token);
            if (TextUtils.isEmpty(refresh_token)) {
                if (num < 3) {
                    num++;
                    invokCode();
                }
                else {
                    //接口回调  3次失败返回null
                    wxUserInfoCallBack.wxBackSucceed(null);
                }
            }
            else {
                new TakenThread().start();
            }
        }
    }
    
    class TakenThread extends Thread implements Runnable {
        @Override
        public void run() {
            super.run();
            num = 0;
            String takenStr = loadTextFromURL("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + mWXappSecret
                    + "&grant_type=refresh_token&refresh_token=" + refresh_token);
            VALog.i("===WXInfoUtil==takenStr==" + takenStr);
            WXRefreshTokenResponseBean wxRefreshTokenResponseBean = JsonParser.getInstance().getObjectFromJson(takenStr,
                    WXRefreshTokenResponseBean.class);
            access_token = wxRefreshTokenResponseBean.getAccess_token();
            openid = wxRefreshTokenResponseBean.getOpenid();
            VALog.i("===WXInfoUtil==access_token==" + access_token + "===openid==" + openid);
            if (TextUtils.isEmpty(access_token) || TextUtils.isEmpty(openid)) {
                if (num < 3) {
                    num++;
                    new TakenThread().start();
                }
                else {
                    //接口回调  3次失败返回null
                    wxUserInfoCallBack.wxBackSucceed(null);
                }
            }
            else {
                new UserInfoThread().start();
            }
        }
    }
    
    class UserInfoThread extends Thread implements Runnable {
        @Override
        public void run() {
            super.run();
            num = 0;
            String userInfo = loadTextFromURL("https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid);
            VALog.i("===WXInfoUtil==userInfo==" + userInfo);
            WXUserInfoBean wxUserInfoBean = JsonParser.getInstance().getObjectFromJson(userInfo, WXUserInfoBean.class);
            if (wxUserInfoBean == null) {
                if (num < 3) {
                    num++;
                    new UserInfoThread().start();
                }
                else {
                    //接口回调  3次失败返回null
                    wxUserInfoCallBack.wxBackSucceed(null);
                }
            }
            else {
                VALog.i("===WXInfoUtil==nickname==" + wxUserInfoBean.getNickname());
                //接口回调微信用户信息bean
                wxUserInfoCallBack.wxBackSucceed(wxUserInfoBean);
                num = 0;
            }
        }
    }
    
    public static String loadTextFromURL(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet requestGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(requestGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity, "utf-8");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
