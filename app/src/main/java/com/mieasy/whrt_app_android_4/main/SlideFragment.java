package com.mieasy.whrt_app_android_4.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.news.ShowNewsInfoActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.NewsInfoDetail;
import com.mieasy.whrt_app_android_4.util.CheckUrlState;
import com.mieasy.whrt_app_android_4.util.FileUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.util.StringToList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.view.LayoutInflater;

public class SlideFragment extends Fragment {
	private View view;
	
	private ViewPager adViewPager;
	private List<ImageView> imageViews;// 滑动的图片集合

	private List<View> dots; // 图片标题正文的那些点
	private List<View> dotList;

	private TextView tv_date;
	private TextView tv_title;
	private TextView tv_topic_from;
	private TextView tv_topic;
	private int currentItem = 0; // 当前图片的索引号
	// 定义的三个指示点
	private View dot0;
	private View dot1;
	private View dot2;
	
	private List<NewsInfoDetail> newsList;
	private ScheduledExecutorService scheduledExecutorService;

	// 异步加载图片
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private Gson gson = new Gson();
	
	private String serverUrl = NumUtil.APP_URL_SERVER_2;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adViewPager.setCurrentItem(currentItem);
		};
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.layout_slideadshow, null);
		// 获取图片加载实例
		mImageLoader = ImageLoader.getInstance();
		options = ContentApplication.getInstance().options1;
		initView();
		initAdData();
		return view;
	};

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 广告数据
		imageViews = new ArrayList<ImageView>();
		adViewPager = (ViewPager) view.findViewById(R.id.vp);
		// 点
		dots = new ArrayList<View>();
		dotList = new ArrayList<View>();
		dot0 = view.findViewById(R.id.v_dot0);
		dot1 = view.findViewById(R.id.v_dot1);
		dot2 = view.findViewById(R.id.v_dot2);
		dots.add(dot0);
		dots.add(dot1);
		dots.add(dot2);

		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		
		Bundle bundle = getArguments();
	}

	/**
	 * 初始化轮播数据
	 */
	private void initAdData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String str = "";
				newsList = new ArrayList<NewsInfoDetail>();
				str = CheckUrlState.checkServerURL(view.getContext(),true, "", NumUtil.URL_LOCAL_HOMEPAGE);
				try {
					if(str.length()==0){
						str = FileUtil.readStorageFileString(NumUtil.URL_LOCAL_HOMEPAGE);
					}
					newsList = gson.fromJson(str, new TypeToken<List<NewsInfoDetail>>(){}.getType());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = Message.obtain();
				msg.obj = newsList;
				handList.sendMessage(msg);
			}
		}).start();
	}

	private void addDynamicView(List<NewsInfoDetail> adList) {
		// 动态添加图片和下面指示的圆点
		// 初始化图片资源
		for (int i = 0; i < adList.size(); i++) {
			ImageView imageView = new ImageView(view.getContext());
			// 异步加载图片
			mImageLoader.displayImage(NumUtil.APP_URL_SERVER_2+"image/"+adList.get(i).getBigPic(), imageView,options);
//			mImageLoader.displayImage(adList.get(i).getImgUrl(), imageView,options);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
			dots.get(i).setVisibility(View.VISIBLE);
			dotList.add(dots.get(i));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		startAd();
	}

	private void startAd() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,TimeUnit.SECONDS);
	}

	private class ScrollTask implements Runnable {

		@Override
		public void run() {
			synchronized (adViewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		scheduledExecutorService.shutdown();
	}
	
	private class MyPageChangeListener implements OnPageChangeListener {
		private List<NewsInfoDetail> adList;
		public MyPageChangeListener(List<NewsInfoDetail> adList) {
			super();
			this.adList = adList;
		}

		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			NewsInfoDetail imageCarousel = adList.get(position);
			tv_title.setText(imageCarousel.getTitle()); // 设置标题
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}
	}

	private class MyAdapter extends PagerAdapter {
		private List<NewsInfoDetail> adList;
		public MyAdapter(List<NewsInfoDetail> adList) {
			super();
			this.adList = adList;
		}

		@Override
		public int getCount() {
			return adList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container,final int position) {
			ImageView iv = imageViews.get(position);
			((ViewPager) container).addView(iv);
			NewsInfoDetail imageCarousel = adList.get(position);
			// 在这个方法里面设置图片的点击事件
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转
					ArtActivity(position);
				}
			});
			return iv;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}
	
	/**
	 * 设置图片数据
	 */
	Handler handList = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.obj!=null){
				List<NewsInfoDetail> list = (List<NewsInfoDetail>)msg.obj;
				addDynamicView(list);
				adViewPager.setAdapter(new MyAdapter(list));// 设置填充ViewPager页面的适配器
				// 设置一个监听器，当ViewPager中的页面改变时调用
				adViewPager.setOnPageChangeListener(new MyPageChangeListener(list));
			}
		};
	};
	
	//界面跳转
	public void ArtActivity(int position){
		Intent intent = new Intent(view.getContext(),ShowNewsInfoActivity.class);
//		intent.putExtra(NumUtil.ID, position);
		Bundle bundle = new Bundle();
		bundle.putInt(NumUtil.ID,position);
		bundle.putParcelable(NumUtil.JUMP_PARCELABLE, newsList.get(position));
		bundle.putInt(NumUtil.NEWS_DATEIL_TITLE,R.string.group_express);
		intent.putExtra(NumUtil.NEWS_URL, bundle);
		startActivity(intent);
	}
}