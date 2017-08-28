package com.mieasy.whrt_app_android_4.welcome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.adapter.ViewPagerAdapter;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity implements OnClickListener, OnPageChangeListener{
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private List<View> views;
	private View view1,view2,view3,view4;

	private boolean isFirstUse;
	private int times = 1;				//第一次访问时为1，其他位置跳转不为1
	private ImageView checkImgView;

	private View[] dots;				//引导界面下面的小点，用于显示当前View是第几个
	private int[] ids = { R.id.imageView1, R.id.imageView2,R.id.imageView3,R.id.imageView4};
	private int[] imgRid = {R.drawable.lead_one,R.drawable.lead_two,R.drawable.lead_three,R.drawable.lead_frou};
	private SharedPreferences perPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		setContentView(R.layout.activity_welcome);
		getinitInfo();
	}

	/**
	 * 获取SharedPreferences数据
	 */
	private void getinitInfo() {
		Intent intent = getIntent();
		times = intent.getIntExtra(NumUtil.JUMP_INTENT, 1);//2

		//用SharedPreferences来存储用户是否是安装后第一次使用的值
		perPreferences = getSharedPreferences(NumUtil.SHAREPRE_WHRTONCE, MODE_PRIVATE);
		isFirstUse = perPreferences.getBoolean(NumUtil.isFirstUse, true);
		if(times!=1){
			//异常访问
			initView();
		}else{
			if (!isFirstUse) {
				//第一次访问
				startArt();
			}else{
				//非第一次访问
				initView();
			}
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.wel_viewpager);
		LayoutInflater inflater = LayoutInflater.from(this);
		view1 = createImageView(imgRid[0]);
		view2 = createImageView(imgRid[1]);
		view3 = createImageView(imgRid[2]);
		view4 = createImageView(imgRid[3]);
		view4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(times==2)
					WelcomeActivity.this.finish();
				else
					startArt();
			}
		});
		views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);

		initDotView();
		pagerAdapter = new ViewPagerAdapter(views, this);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(this);
	}

	//创建ImageView控件
	public ImageView createImageView(int rid){
		ImageView imgView = new ImageView(this);
		imgView.setScaleType(ScaleType.FIT_XY);
//		imgView.setImageResource(rid);
		ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.DRAWABLE.wrap(rid+""),imgView);
		return imgView;
	}

	public void initDotView(){
		dots = new View[views.size()];
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (View) findViewById(ids[i]);
		}
	}

	@Override
	public void onClick(View v) {
		startArt();
	}

	public void startArt(){
		initView();
		Editor editor = perPreferences.edit();
		editor.putBoolean(NumUtil.isFirstUse, false);
		editor.commit();
		
		startActivity(new Intent(this,SpaceActivity.class));
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);    //向左切换
		this.finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		for (int i = 0; i < ids.length; i++) {
			//若当前的界面是用户选中的界面，则点设置为选中状态，反之，则为没选中状态
			if (arg0 == i) {
				dots[i].setBackgroundResource(R.drawable.dot_focused);
			} else {
				dots[i].setBackgroundResource(R.drawable.dot_normal);
			}
		}
	}
}