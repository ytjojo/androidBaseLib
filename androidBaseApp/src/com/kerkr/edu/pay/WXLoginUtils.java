package com.kerkr.edu.pay;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * Created by ybk on 2015/4/15.
 */
public class WXLoginUtils {
    private static IWXAPI api;
    
    
    public static void loginWX(Context context) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(context, Appkey.WX_APP_ID, false);
        }
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, "没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        api.registerApp(Appkey.WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "loginActivity";
        api.sendReq(req);
    }

    public static boolean isWXAppInstalledAndSupportedNoToast(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, "101041593", false);//Constants.APP_ID是你应用注册的AppID
        return api.isWXAppInstalled() && api.isWXAppSupportAPI();
    }
}
