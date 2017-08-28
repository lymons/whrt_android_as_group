package com.mieasy.whrt_app_android_4.jpush;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.google.gson.Gson;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.SlideBackActivity;
import com.mieasy.whrt_app_android_4.act.news.ImagePagerActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.Info;
import com.mieasy.whrt_app_android_4.bean.JpushNotification;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends SlideBackActivity {
	private static final String TAG = "NotificationActivity";
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
	private JpushNotification newsInfoDetail;
	int imgItem = 0;										//图片集合中的ID
	private ArrayList<String> imgList = new ArrayList<String>();	//图片Url集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_news_detail);
		perPreferences = getSharedPreferences(NumUtil.SHAREPRE_WHRTONCE, MODE_PRIVATE);
		isTextSize = perPreferences.getInt(NumUtil.isTextSize, 20);
		//初始化接受下来的值
		IsNetWork = ContentApplication.getInstance().IsNetWorkAvailable;//判断网络
		newsInfoDetail = new JpushNotification();
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		currentW = outMetrics.widthPixels;
		currentH = outMetrics.heightPixels;
		newsInfoDetail = new JpushNotification();
		Intent intent = getIntent();
		if (null != intent) {
			String pushcontent = intent.getStringExtra("pushcontent");
			Log.e(TAG,"pushcontent:"+pushcontent);
			newsInfoDetail = gson.fromJson(pushcontent,JpushNotification.class);
		}
		initView();			//初始化控件并设置
		initAllView();
	}

	private void initAllView() {
		if (newsInfoDetail != null) {
			List<Info> infoList = newsInfoDetail.getContent().getInfo();
			View view = LayoutInflater.from(this).inflate(R.layout.newinfo_authoer_date, linearLayout);
			LinearLayout linImgContent = (LinearLayout) view.findViewById(R.id.lin_img_content);
			ImageView imgViewContent = (ImageView) view.findViewById(R.id.img_newinfo_content);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_newinfo_title);
			TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
			tvDate.setTextColor(getResources().getColor(R.color.block_bg_color_black_transparent_90));
			tvDate.setTextSize(16.0f);
			TextView tvPeople = (TextView) view.findViewById(R.id.tv_author);
			tvPeople.setTextColor(getResources().getColor(R.color.block_bg_color_black_transparent_90));
			tvPeople.setTextSize(16.0f);
			tvTitle.setText(newsInfoDetail.getTitle());//标题
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			String Create_at = simpleDateFormat.format(newsInfoDetail.getCreate_at());
			String Create_at = newsInfoDetail.getCreate_at().substring(0,10);
			tvDate.setText(Create_at);//时间
			if (newsInfoDetail.getAdmin() != null) {
				tvPeople.setText(getResources().getString(R.string.source) + newsInfoDetail.getAdmin());//来源
			}

			for (int x = 0; x < infoList.size(); x++) {
				if (infoList.get(x).getType().equals("img")) {
					imgList.add( infoList.get(x).getContent());
				}
			}

			int k = -1;
			for (int i = 0; i < infoList.size(); i++) {
				if (infoList.get(i).getType().equals("img")) {
					k = i;
					LayoutParams imgParams = (LayoutParams) imgViewContent.getLayoutParams();
					imgParams.gravity = Gravity.CENTER_HORIZONTAL;
					imgViewContent.setScaleType(ScaleType.FIT_XY);
					linImgContent.setVisibility(View.VISIBLE);
					linImgContent.setLayoutParams(imgParams);
					ImageLoader.getInstance().displayImage(
							 infoList.get(i).getContent(),//NumUtil.APP_URL_SERVER_2 + "image/" +
							imgViewContent);
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
			for (int j = 0; j < infoList.size(); j++) {
				if (k != j) {
					//Info info = infoList.get(j);
					if (infoList.get(j).getType().equals("text")) {
						TextView txtView = createTextView(R.color.block_bg_color_black, isTextSize, infoList.get(j).getContent());
						txtView.setLineSpacing(10f, 1.1f);
						txtView.setPadding(16, 0, 16, 0);
						linearLayout.addView(txtView);
					}
					if (infoList.get(j).getType().equals("img")) {
						LinearLayout imgLin = new LinearLayout(this);
						ImageView imgView = new ImageView(this);
						LayoutParams params = new LayoutParams(currentW - 40, (int) (currentW / 1.8));
						imgView.setScaleType(ScaleType.FIT_XY);
						imgView.setLayoutParams(params);
						imgView.setPadding(50, 0, 10, 30);
						ImageLoader.getInstance().displayImage(infoList.get(j).getContent(), imgView);//NumUtil.APP_URL_SERVER_2 + "image/" +, ContentApplication.getInstance().options4
								imgView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								imageBrower(imgItem += 1, imgList);
							}
						});
						imgLin.addView(imgView);
						linearLayout.addView(imgLin);
					}
				}
			}
		}
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTitle.setText("我的消息");
		imgBack = (ImageButton) findViewById(R.id.iv_top_left_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NotificationActivity.this.finish();
			}
		});
		linearLayout =(LinearLayout) findViewById(R.id.lin_layout);
	}

	private void initValue() {
		perPreferences = getSharedPreferences(NumUtil.SHAREPRE_WHRTONCE, MODE_PRIVATE);
		isTextSize = perPreferences.getInt(NumUtil.isTextSize, 20);
		
		IsNetWork = ContentApplication.getInstance().IsNetWorkAvailable;
		newsInfoDetail = new JpushNotification();
		
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		currentW = outMetrics.widthPixels;
		currentH = outMetrics.heightPixels;
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