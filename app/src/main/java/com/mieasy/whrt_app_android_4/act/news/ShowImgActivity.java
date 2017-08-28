package com.mieasy.whrt_app_android_4.act.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.bean.BuildingLine;
import com.mieasy.whrt_app_android_4.util.NumUtil;

public class ShowImgActivity extends FragmentActivity{
	private FrameLayout mFrameLayout;
	private TextView mTextViewTitle;
	private ImageButton mImageButton;
	
	private String picUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pro_showinfo_lineimg);
		initValue();
		initView();
	}

	private void initValue() {
		Intent intent = getIntent();
		BuildingLine mBuildingLine = intent.getParcelableExtra(NumUtil.JUMP_INTENT);
		picUrl = NumUtil.APP_URL_SERVER_2+"image/"+mBuildingLine.getPic();
	}
	
	private void initView() {
		mTextViewTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTextViewTitle.setText(R.string.prolinepic);
		mImageButton = (ImageButton) findViewById(R.id.iv_top_left_back);
		mImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowImgActivity.this.finish();
			}
		});
		mFrameLayout = (FrameLayout) this.findViewById(R.id.pro_showlineimg);
		this.getSupportFragmentManager().beginTransaction().add(R.id.pro_showlineimg,  ImageDetailFragment.newInstance(picUrl)).commit();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
