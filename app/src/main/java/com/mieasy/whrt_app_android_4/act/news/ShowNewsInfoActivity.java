package com.mieasy.whrt_app_android_4.act.news;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.SlideBackActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.Info;
import com.mieasy.whrt_app_android_4.bean.NewsInfoDetail;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import android.widget.TextView;

public class ShowNewsInfoActivity extends SlideBackActivity{
	private TextView mTitle;
	private int titleTxt;
	private ImageButton imgBack;

	private LinearLayout linearLayout;

	private SharedPreferences perPreferences;
	private int isTextSize;
	
	private String url;
	private int position;
	private Gson gson = new Gson();

	//网络状态
	private boolean IsNetWork;
	private int currentW,currentH;
	private NewsInfoDetail newsInfoDetail;
	int imgItem = 0;										//图片集合中的ID
	ArrayList<String> imgList = new ArrayList<String>();	//图片Url集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_news_detail);
		initValue();		//获取上一个界面传过来的值
		initView();			//初始化控件并设置
		initAllView();
	}

	private void initAllView() {
		List<Info> infoList = newsInfoDetail.getBody().getInfo();
		
		View view = LayoutInflater.from(this).inflate(R.layout.newinfo_authoer_date, linearLayout);
		LinearLayout linImgContent = (LinearLayout) view.findViewById(R.id.lin_img_content);
		ImageView imgViewContent = (ImageView) view.findViewById(R.id.img_newinfo_content);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_newinfo_title);		
		TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
		tvDate.setTextColor(getResources().getColor(R.color.block_bg_color_black_transparent_90));   tvDate.setTextSize(16.0f); 
		TextView tvPeople = (TextView) view.findViewById(R.id.tv_author);   
		tvPeople.setTextColor(getResources().getColor(R.color.block_bg_color_black_transparent_90)); tvPeople.setTextSize(16.0f);
		tvTitle.setText(newsInfoDetail.getTitle());
		tvDate.setText(newsInfoDetail.getPublishTime());
		if(newsInfoDetail.getSource()!=null){
			tvPeople.setText(getResources().getString(R.string.source)+newsInfoDetail.getSource());
		}
		
		for(int x=0;x<infoList.size();x++){
			if(infoList.get(x).getType().equals("img")){
				imgList.add(NumUtil.APP_URL_SERVER_2+"image/"+infoList.get(x).getContent());
			}
		}
		
		int k = -1;
		for(int i = 0;i<infoList.size();i++){
			if(infoList.get(i).getType().equals("img")){
				k = i;
				LinearLayout.LayoutParams imgParams =  (LayoutParams) imgViewContent.getLayoutParams();
				imgParams.gravity = Gravity.CENTER_HORIZONTAL;
				imgViewContent.setScaleType(ScaleType.FIT_XY);
				linImgContent.setVisibility(View.VISIBLE);
				linImgContent.setLayoutParams(imgParams);
				ImageLoader.getInstance().displayImage(
						NumUtil.APP_URL_SERVER_2+"image/"+infoList.get(i).getContent(),
						imgViewContent,
						ContentApplication.getInstance().options4);
				imgViewContent.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						imageBrower(imgItem, imgList);
					}
				});
				break;
			}
		}

		//按照段落添加新闻
		for(int j=0;j<infoList.size();j++){
			if(k!=j){
				Info info = infoList.get(j);
				if(info.getType().equals("text")){
					TextView txtView = createTextView(R.color.block_bg_color_black, isTextSize,info.getContent());
					txtView.setLineSpacing(10f, 1.1f);
					txtView.setPadding(16, 0, 16, 0);
					linearLayout.addView(txtView);
				}
				if(info.getType().equals("img")){
					LinearLayout imgLin = new LinearLayout(this);
					ImageView imgView = new ImageView(this);
					LinearLayout.LayoutParams params =new LayoutParams(currentW-40, (int)(currentW/1.8));
					imgView.setScaleType(ScaleType.FIT_XY);
					imgView.setLayoutParams(params);
					imgView.setPadding(50, 0, 10, 30);
					ImageLoader.getInstance().displayImage(NumUtil.APP_URL_SERVER_2+"image/"+info.getContent(), imgView,ContentApplication.getInstance().options4);
					imgView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							imageBrower(imgItem+=1, imgList);
						}
					});
					imgLin.addView(imgView);
					linearLayout.addView(imgLin);
				}
			}
		}
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTitle.setText(titleTxt);
		imgBack = (ImageButton) findViewById(R.id.iv_top_left_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowNewsInfoActivity.this.finish();
			}
		});
		linearLayout =(LinearLayout) findViewById(R.id.lin_layout);
	}

	private void initValue() {
		perPreferences = getSharedPreferences(NumUtil.SHAREPRE_WHRTONCE, MODE_PRIVATE);
		isTextSize = perPreferences.getInt(NumUtil.isTextSize, 20);
		
		IsNetWork = ContentApplication.getInstance().IsNetWorkAvailable;
		newsInfoDetail = new NewsInfoDetail();
		
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		currentW = outMetrics.widthPixels;
		currentH = outMetrics.heightPixels;

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra(NumUtil.NEWS_URL);
		if(bundle!=null){
			newsInfoDetail = bundle.getParcelable(NumUtil.JUMP_PARCELABLE);
			titleTxt = bundle.getInt(NumUtil.NEWS_DATEIL_TITLE);
			position = bundle.getInt(NumUtil.ID);
		}
	}
	
	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		this.startActivity(intent);
	}
	
	public TextView createTextView(int color,float txtSize,String str){
		TextView txView = new TextView(this);
		txView.setTextColor(getResources().getColor(color));
		txView.setTextSize(txtSize);
		txView.setText(str);
		return txView;
	}
}