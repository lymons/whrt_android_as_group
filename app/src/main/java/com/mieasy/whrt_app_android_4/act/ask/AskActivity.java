package com.mieasy.whrt_app_android_4.act.ask;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.SlideBackActivity;
import com.mieasy.whrt_app_android_4.act.login.LoginActivity;
import com.mieasy.whrt_app_android_4.util.NumUtil;

public class AskActivity extends SlideBackActivity {
	private ImageView mImgBtnBack;
	private TextView mTextViewTitle;
	private ImageView mImageButton;

	private FrameLayout mFrameLayout;
	private Fragment askFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notice_main);
		initView();
	}

	private void initView() {
		mTextViewTitle = (TextView) findViewById(R.id.tv_top_tiles_tiles);
		mTextViewTitle.setText(R.string.block_ask);
		mImgBtnBack = (ImageButton) findViewById(R.id.iv_top_left_backs);
		mImgBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AskActivity.this.finish();
			}
		});
		mImageButton = (ImageView) findViewById(R.id.btn_image_ask);
		mImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserDatabase", Context.MODE_PRIVATE);
				final String userName = sharedPreferences.getString("Loginame", "");
				final String password = sharedPreferences.getString("PassWord", "");
				if ("".equals(userName)) {
					//请登陆弹框 取消和确定
					//取消在提问列表dialog
					//确定就跳转到登陆界面
					dialog();
				} else {
					Intent intent = new Intent(AskActivity.this, AskQuestionActivity.class);
					startActivity(intent);
				}
			}
		});
		Intent intent = getIntent();

		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

		askFrag = new AskFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(NumUtil.ITEM_LOGO, NumUtil.ITEM_LOGO_ASK);                                //列表前面的图标
		bundle.putString(NumUtil.NEWS_URL, NumUtil.URL_LOCAL_NEWS_ZXWD);                //获取列表的URL
		bundle.putInt(NumUtil.NEWS_DATEIL_TITLE, R.string.block_ask);                            //获取详细页的title
		askFrag.setArguments(bundle);

		fragmentTransaction.add(R.id.notice_view, askFrag);
		fragmentTransaction.commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected  void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(AskActivity.this);
		builder.setTitle("请登录！");
		builder.setMessage("确认是否登录？");
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(AskActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		builder.create().show();
	}

}
