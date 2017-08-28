package com.mieasy.whrt_app_android_4.act.ask;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.R.string;
import com.mieasy.whrt_app_android_4.act.SlideBackActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.Translation;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.util.StringToList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AskInfoActivity extends Activity{
	private LinearLayout linReply;
	private ImageView imgReply;
	private TextView mTextViewTitle;
	private ImageView mImgBtnBack;
	
	private int mTitle;
	private boolean IsNetWork= false;
	private int position;
	
	private TextView mAskTitle;
	private TextView mTxtQuestion,mTxtQuestionPer,mTxtQuestionTime;
	private TextView mTxtAsk,mTxtAskPer,mTxtAskTime;
	
	private Translation mTranslation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ask_show_info);
		initView();
		initAllView();
	}
	
	private void initAllView() {
		mAskTitle.setText(mTranslation.getTitle());
		
		mTxtQuestionPer.setText(getResources().getString(R.string.clientname)+mTranslation.getClientName());
		mTxtQuestionTime.setText(getResources().getString(string.submitDate)+mTranslation.getSubmitDate());
		mTxtQuestion.setText(getResources().getString(string.question)+mTranslation.getQuestion());
		if(mTranslation.getReply()!=null){
			mTxtAskPer.setText(getResources().getString(string.askper));
			mTxtAskTime.setText(getResources().getString(string.asktime)+mTranslation.getReplyDate());
			mTxtAsk.setText(getResources().getString(string.reply)+mTranslation.getReply());
			linReply.setVisibility(View.VISIBLE);
			imgReply.setVisibility(View.VISIBLE);
		}else{
			linReply.setVisibility(View.GONE);
			imgReply.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		IsNetWork = ContentApplication.getInstance().IsNetWorkAvailable;	
		//获取数据
		Intent inten = getIntent();
		Bundle bundle = inten.getBundleExtra(NumUtil.NEWS_URL);
		if(bundle!=null){
			mTitle = bundle.getInt(NumUtil.NEWS_DATEIL_TITLE);
			mTranslation = bundle.getParcelable(NumUtil.JUMP_PARCELABLE);
			position = bundle.getInt(NumUtil.ID);
		}
		
		//设置Title
		mTextViewTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTextViewTitle.setText(mTitle);
		mImgBtnBack = (ImageButton)findViewById(R.id.iv_top_left_back);
		linReply = (LinearLayout) findViewById(R.id.lin_ask_reply_info);
		imgReply = (ImageView) findViewById(R.id.img_ask_reply);
		mImgBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AskInfoActivity.this.finish();
			}
		});
		
		mTxtQuestion = (TextView) findViewById(R.id.tv_question);
		mTxtQuestionPer = (TextView) findViewById(R.id.tv_question_person);
		mTxtQuestionTime = (TextView) findViewById(R.id.tv_question_time);
		mTxtAsk = (TextView) findViewById(R.id.tv_ask);
		mTxtAskPer = (TextView) findViewById(R.id.tv_ask_person);
		mTxtAskTime = (TextView) findViewById(R.id.tv_ask_time);
		mAskTitle = (TextView) findViewById(R.id.tv_ask_title);
	}
}















