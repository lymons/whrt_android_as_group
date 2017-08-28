package com.mieasy.whrt_app_android_4.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.handler.UMWXHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * Created by alan on 16-12-12.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private final String TAG = WXCallbackActivity.class.getSimpleName();
    protected UMWXHandler mWxHandler = null;

    public WXEntryActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("create wx callback activity");
        UMShareAPI api = UMShareAPI.get(getApplicationContext());
        this.mWxHandler = (UMWXHandler)api.getHandler(SHARE_MEDIA.WEIXIN);
        Log.e("xxxx wxhandler=" + this.mWxHandler);
        this.mWxHandler.onCreate(this, PlatformConfig.getPlatform(SHARE_MEDIA.WEIXIN));
        this.mWxHandler.getWXApi().handleIntent(this.getIntent(), this);
    }

    protected final void onNewIntent(Intent paramIntent) {
        Log.d(this.TAG, "### WXCallbackActivity   onNewIntent");
        super.onNewIntent(paramIntent);
        this.setIntent(paramIntent);
        UMShareAPI api = UMShareAPI.get(this.getApplicationContext());
        this.mWxHandler = (UMWXHandler)api.getHandler(SHARE_MEDIA.WEIXIN);
        this.mWxHandler.onCreate(this.getApplicationContext(), PlatformConfig.getPlatform(SHARE_MEDIA.WEIXIN));
        this.mWxHandler.getWXApi().handleIntent(paramIntent, this);
    }

    public void onResp(BaseResp resp) {
        if(this.mWxHandler != null && resp != null) {
            try {
                this.mWxHandler.getWXEventHandler().onResp(resp);
            } catch (Exception var3) {
                ;
            }
        }

        this.finish();
    }

    public void onReq(BaseReq req) {
        if(this.mWxHandler != null) {
            this.mWxHandler.getWXEventHandler().onReq(req);
        }

        this.finish();
    }
}
