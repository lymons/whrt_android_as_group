package com.mieasy.whrt_app_android_4.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.pro.NoBarActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by alan on 16-12-30.
 */
public class ShareAppActivity extends NoBarActivity {

    private ImageView mImageView;
    private Button button;
    private TextView mTextViewTitle;
    private ImageButton mImgBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_main_info_shareapp);
        //可以将一下代码加到你的MainActivity中，或者在任意一个需要调用分享功能的activity当中
        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
        ActivityCompat.requestPermissions(ShareAppActivity.this, mPermissionList, 100);
        initView();
        button = (Button) findViewById(R.id.share_app_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareApp();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    private void initView() {
        mTextViewTitle = (TextView) findViewById(R.id.tv_top_left_title);
        mTextViewTitle.setText("推荐此应用");
        mImgBtnBack = (ImageButton) findViewById(R.id.iv_top_left_back);
        mImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareAppActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mImageView = (ImageView) findViewById(R.id.qrcode);
        DisplayMetrics outMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int currentW = outMetrics.widthPixels;
        LinearLayout.LayoutParams mParam = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
        mParam.width = (int)(currentW-currentW*0.2);
        mParam.height = mParam.width;
        mParam.setMargins((int)(currentW*0.1), (int)(currentW*0.1), (int)(currentW*0.1), 0);
        mImageView.setLayoutParams(mParam);
    }

    //分享到QQ,QQ空间,微信,微信朋友圈
    private void ShareApp() {
        ShareAction shareAction = new ShareAction(ShareAppActivity.this).setDisplayList( SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
        shareAction.withTitle("出行改变生活，每一公里都舒适无比");
        shareAction.withText("点击链接，立即下载武汉地铁");
        shareAction.withMedia(new UMImage(this, R.drawable.share_app_app));
        shareAction.withTargetUrl("http://app1.whrt.gov.cn");
        shareAction.setCallback(umShareListener);
        shareAction.open();
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(ShareAppActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShareAppActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ShareAppActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ShareAppActivity.this, platform + " 已分享,返回！", Toast.LENGTH_SHORT).show();
        }
    };

    //	分享后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(ShareAppActivity.this).onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult");
    }
}
