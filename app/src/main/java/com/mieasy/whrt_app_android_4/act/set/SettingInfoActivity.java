package com.mieasy.whrt_app_android_4.act.set;

import java.util.ArrayList;
import java.util.List;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.SlideBackActivity;
import com.mieasy.whrt_app_android_4.act.set.fragment.AboutAppFragment;
import com.mieasy.whrt_app_android_4.act.set.fragment.ContactNumFragment;
import com.mieasy.whrt_app_android_4.act.set.fragment.ShareAppFragment;
import com.mieasy.whrt_app_android_4.act.set.fragment.TextSizeFragment;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingInfoActivity extends SlideBackActivity {
	private TextView mTextViewTitle;
	private ImageButton mImgBtnBack;
	private List<Fragment> listFragment;
	private int item;
	private int rid;
	private String data;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_main_info);
		initValue();
		initView();
	}
	
	private void initValue() {
		Intent intent = getIntent();
		item = intent.getIntExtra(NumUtil.JUMP_INTENT, 0);
		rid = intent.getIntExtra(NumUtil.JUMP_TYPE, R.string.set_textsize);

		listFragment = new ArrayList<Fragment>();
		listFragment.add(new TextSizeFragment());
		listFragment.add(new ShareAppFragment());
		listFragment.add(new ContactNumFragment());
		listFragment.add(new AboutAppFragment());
	}

	private void initView() {
		initTitle();
		getFragmentManager().beginTransaction().add(R.id.main_frame,  listFragment.get(item)).commit();
	}

	private void initTitle() {
		mTextViewTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTextViewTitle.setText(rid);
		mImgBtnBack = (ImageButton)findViewById(R.id.iv_top_left_back);
		mImgBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SettingInfoActivity.this.finish();
			}
		});
	}
}
