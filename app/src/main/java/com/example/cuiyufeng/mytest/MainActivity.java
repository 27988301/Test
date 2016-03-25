package com.example.cuiyufeng.mytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class MainActivity extends BaseActivity implements View.OnClickListener,IUiListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btn_qq=(Button)findViewById(R.id.btn_qq);
        Button btn_weixin=(Button)findViewById(R.id.btn_weixin);
        Button btn_sina=(Button)findViewById(R.id.btn_sina);
        btn_qq.setOnClickListener(this);
        btn_weixin.setOnClickListener(this);
        btn_sina.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_qq:
                qqLogin();
                break;
            case R.id.btn_weixin:
                wxLogin();
                break;
            case R.id.btn_sina:
                break;
        }
    }

    private void showDialog(){
        //v7中，加入了对话框和新主题theme Theme.AppCompat.Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dialog");
        builder.setMessage("少数派客户端");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    /**
     * QQ登陆
     */
    private void qqLogin() {
        Tencent mTencent = Tencent.createInstance("APP_QQ_KEY",this);
        mTencent.login(this, "all", this);
    }


    @Override
    public void onCancel() {
        Log.e("onCancel_qq", "onCancel");
    }

    @Override
    public void onError(UiError uiError) {
        Log.e("onError_qq","onError");
    }

    @Override
    public void onComplete(Object o) {
        Log.e("onComplete_qq",o.toString());
    }

    BroadcastReceiver receiver;
    /**
     * 微信登陆
     */
    private void wxLogin() {
        IWXAPI api = WXAPIFactory.createWXAPI(this, "WEICHAT_APPID", false);
        api.registerApp("WEICHAT_APPID");

        if (!api.isWXAppInstalled()) {
            Log.e("weichat","手机中没有安装微信客户端");
            return;
        }
        // 唤起微信登录授权
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_login";
        api.sendReq(req);
        // 注册一个广播，监听微信的获取openid返回（类：WXEntryActivity中）
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("wechat");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String openid_info = intent.getStringExtra("bundle_key_openid_info");
                    //openIdLogin(OpenIdCatalog.WECHAT, openid_info);
                    Log.e("wechat",""+openid_info);
                    // 注销这个监听广播
                    if (receiver != null) {
                        unregisterReceiver(receiver);
                    }
                }
            }
        };

        registerReceiver(receiver, intentFilter);
    }


    /**
     * 新浪登录
     */
    private void sinaLogin() {
//        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    }

}
