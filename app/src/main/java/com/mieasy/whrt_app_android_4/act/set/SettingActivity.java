package com.mieasy.whrt_app_android_4.act.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.R.id;
import com.mieasy.whrt_app_android_4.act.SlideBackActivity;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.util.UpdateManager;
import com.mieasy.whrt_app_android_4.welcome.WelcomeActivity;

import java.util.Calendar;

public class SettingActivity extends SlideBackActivity implements OnClickListener{
	private TextView mTextViewTitle;
	private ImageButton mImgBtnBack;
	private Intent intent;
	
	private TextView mQueryNcf,mTextSize,mTextWelcome,mTextShare,mTextUpdate,mTextCall,mTextAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_main);
		initView();
	}

	private void initView() {
		initTitle();
		mQueryNcf = (TextView) findViewById(id.query_nfc);
		mTextSize = (TextView) findViewById(id.text_size);
		mTextWelcome = (TextView) findViewById(id.page_welcome);
		mTextShare = (TextView) findViewById(id.share_app);
		mTextUpdate = (TextView) findViewById(id.check_update);
		mTextCall = (TextView) findViewById(id.callme);
		mTextAbout = (TextView) findViewById(id.about);
		mTextSize.setOnClickListener(this);
		mTextWelcome.setOnClickListener(new NoDoubleClickListener() {
			@Override
			public void onNoDoubleClick(View v) {
				super.onNoDoubleClick(v);
				startArt(WelcomeActivity.class,2,0);
			}
		});
		mTextShare.setOnClickListener(this);
		mTextUpdate.setOnClickListener(this);
		mTextCall.setOnClickListener(this);
		mTextAbout.setOnClickListener(this);
		mQueryNcf.setOnClickListener(this);
	}

	private void initTitle() {
		mTextViewTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTextViewTitle.setText(R.string.block_setting);
		mImgBtnBack = (ImageButton)findViewById(R.id.iv_top_left_back);
		mImgBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SettingActivity.this.finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case id.text_size:			   //字体大小
			startArt(SettingInfoActivity.class, 0,R.string.set_textsize);
			break;
		case id.share_app:			   //推荐
			startArt(SettingInfoActivity.class,1,R.string.set_share);
			break;
		case id.callme:			   //联系我们
			startArt(SettingInfoActivity.class,2,R.string.set_call);
			break;
		case id.about:					//关于
			startArt(SettingInfoActivity.class,3,R.string.set_about);
			break;
		case id.query_nfc:			   //武汉通查询
//			startArt(SettingInfoActivity.class,4,R.string.set_querynfc);
//			startActivity(new Intent(this,NfcqueryActivity.class));
			break;
		case id.page_welcome:     		//欢迎页
			startArt(WelcomeActivity.class,2,0);
			break;
		case id.check_update:	  		//app更新
			new UpdateManager(this,true).checkUpdate();
			break;
		default:
			break;
		}
	}
	
	public void startArt(Class clazz,int item,int rid){
		intent = new Intent(this,clazz);
		intent.putExtra(NumUtil.JUMP_INTENT, item);
		intent.putExtra(NumUtil.JUMP_TYPE, rid);
		startActivity(intent);
	}
	
	//代码2
	public abstract class NoDoubleClickListener implements OnClickListener {
		public static final int MIN_CLICK_DELAY_TIME = 1000;
		private long lastClickTime = 0;

		@Override
		public void onClick(View v) {
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
				lastClickTime = currentTime;
				onNoDoubleClick(v);
			}
		}

		public void onNoDoubleClick(View v) {

		}
	}
}
