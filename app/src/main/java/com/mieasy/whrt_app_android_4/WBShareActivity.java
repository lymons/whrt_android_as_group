package com.mieasy.whrt_app_android_4;

import com.umeng.socialize.media.WBShareCallBackActivity;

/**
 * Created by alan on 16-12-12.
 */
public class WBShareActivity extends WBShareCallBackActivity {
//    protected SinaSsoHandler sinaSsoHandler = null;
//
//    public WBShareActivity() {
//    }
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.i("create wx callback activity");
//        UMShareAPI api = UMShareAPI.get(getApplicationContext());
//        this.sinaSsoHandler = (SinaSsoHandler)api.getHandler(SHARE_MEDIA.SINA);
//        this.sinaSsoHandler.onCreate(this, PlatformConfig.getPlatform(SHARE_MEDIA.SINA));
//        if(this.getIntent() != null) {
//            this.sinaSsoHandler.getmWeiboShareAPI().handleWeiboResponse(this.getIntent(), this);
//        }
//
//    }
//
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        this.setIntent(intent);
//        UMShareAPI api = UMShareAPI.get(getApplicationContext());
//        this.sinaSsoHandler = (SinaSsoHandler)api.getHandler(SHARE_MEDIA.SINA);
//        this.sinaSsoHandler.onCreate(this, PlatformConfig.getPlatform(SHARE_MEDIA.SINA));
//        this.sinaSsoHandler.getmWeiboShareAPI().handleWeiboResponse(intent, this);
//    }
//
//    public void onResponse(BaseResponse baseResponse) {
//        if(this.sinaSsoHandler != null) {
//            this.sinaSsoHandler.onResponse(baseResponse);
//        }
//
//        this.finish();
//    }
}