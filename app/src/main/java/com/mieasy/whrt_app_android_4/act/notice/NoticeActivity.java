package com.mieasy.whrt_app_android_4.act.notice;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.news.TenderFragment;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeActivity extends FragmentActivity{
	private ImageView mImgBtnBack;
	private TextView mTextViewTitle;
	
	private FrameLayout mFrameLayout;
	private Fragment tenderFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notice_main);
		initView();
	}

	private void initView() {
		mTextViewTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTextViewTitle.setText(R.string.block_notice);
		mImgBtnBack = (ImageButton)findViewById(R.id.iv_top_left_back);
		mImgBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NoticeActivity.this.finish();
			}
		});
		
		Intent intent = getIntent();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		tenderFrag = new TenderFragment();
		Bundle bundle = new Bundle();
		bundle.putString(NumUtil.NEWS_URL, NumUtil.URL_LOCAL_NEWS_TZGG);
		bundle.putInt(NumUtil.NEWS_DATEIL_TITLE,R.string.block_notice);
		tenderFrag.setArguments(bundle);
		
		fragmentTransaction.add(R.id.notice_view, tenderFrag);
		fragmentTransaction.commit();
	}
}
