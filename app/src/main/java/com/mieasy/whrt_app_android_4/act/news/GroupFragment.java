package com.mieasy.whrt_app_android_4.act.news;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.adapter.NewsItemImageAdapter;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.NewsInfoDetail;
import com.mieasy.whrt_app_android_4.util.FileUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.view.RefreshableView;
import com.mieasy.whrt_app_android_4.view.RefreshableView.PullToRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GroupFragment extends Fragment{
	private View loadMoreView;
	private TextView loadMoreButton;
	private int currentW,currentH;

	private List<NewsInfoDetail> newsInfoDetail;
	private NewsItemImageAdapter newsItemImageAdapter;
	private View view;
	private View handView;
	private ImageView imgView;
	private TextView txtView;
	private ListView mListView;
	private DisplayImageOptions options;
	
	private ProgressBar mProgressBar;
	//新闻URL
	private String prefixUrl = NumUtil.APP_URL_SERVER_2;
	private String centerUrl;
	private int times = 1;
	private String suffixUrl = NumUtil.JSON_SUFFIX;
	private String url = prefixUrl+centerUrl+times+suffixUrl;
	//显示新闻Title
	private int mTitle;
	private RefreshableView refreshableView;
	private String imgUrl;
	private boolean IsNetworkStatu;
	private Gson gson = new Gson();
	private int getLastVisiblePosition = 0,lastVisiblePositionY=0;
	private boolean checkLoad = true;
	private boolean isLastRow = false;
	private boolean clickTrue = false;	//当前条目被点击
	private RequestQueue mQueue;
	private String imgTitle;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.news_main_group, container, false);
		initView();			//初始化数据以及控件
		return view;
	}
	
	public static GroupFragment newsInstance(String strUrl,int titId,int typeId){
		GroupFragment mGroupFragment = new GroupFragment();
		Bundle bundle = new Bundle();
		bundle.putString(NumUtil.NEWS_URL, strUrl);					//传递列表URL
		bundle.putInt(NumUtil.NEWS_DATEIL_TITLE, titId);
		bundle.putInt(NumUtil.FRAGMENT_TYPE, typeId);
		mGroupFragment.setArguments(bundle);
		return mGroupFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mQueue = Volley.newRequestQueue(getActivity());
		mQueue = ContentApplication.getInstance().mQueue;
		
		newsInfoDetail = new ArrayList<NewsInfoDetail>();
		//获取父Activity传过来的数据
		Bundle  bundle= getArguments();
		if(bundle!=null){
			centerUrl = bundle.getString(NumUtil.NEWS_URL);
			mTitle = bundle.getInt(NumUtil.NEWS_DATEIL_TITLE);
			switch (bundle.getInt(NumUtil.FRAGMENT_TYPE)) {
			case 2:
				options = ContentApplication.getInstance().options2;
				break;
			case 3:
				options = ContentApplication.getInstance().options3;
				break;
			default:
				break;
			}
		}
	}

	//获取网络数据
	private void initQueue() {
		url = prefixUrl+centerUrl+times+suffixUrl;
		Log.e("GroupFragment","request"+getResources().getString(mTitle));
		StringRequest stringRequest = new StringRequest(
				url,
				new Response.Listener<String>() {  
					@Override  
					public void onResponse(String str) {
						Log.e("GroupFragment","sucess"+getResources().getString(mTitle));
						List<NewsInfoDetail> news = new ArrayList<NewsInfoDetail>();
						Message msg = Message.obtain();
						try {
							FileUtil.saveStorageFile(view.getContext(),str, centerUrl+times+suffixUrl);
							news = gson.fromJson(str, new TypeToken<List<NewsInfoDetail>>(){}.getType());
							msg.what = 1;
							msg.obj = news;
							newsInfoDetail.addAll(news);

							imgTitle = newsInfoDetail.get(0).getTitle();
							imgUrl = NumUtil.APP_URL_SERVER_1+"image/"+newsInfoDetail.get(0).getBigPic();
							txtView.setText(imgTitle);
							ImageLoader.getInstance().displayImage(imgUrl, imgView,options);
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = 0;
						}
						handler.sendMessage(msg);
					}  
				}, new Response.ErrorListener() {
					@Override  
					public void onErrorResponse(VolleyError error) {  
						Log.e("GroupFragment","error"+getResources().getString(mTitle));
						Log.e("GroupFragment","error"+getResources().getString(mTitle)+":"+error.getMessage());
						List<NewsInfoDetail> news = new ArrayList<NewsInfoDetail>();
						Message msg = Message.obtain();
						checkLoad = false;
						try {
							String str =FileUtil.readStorageFileString(centerUrl+times+suffixUrl);
							if(str.length()==0){
								msg.what = 0;
							}else{
								news = gson.fromJson(str, new TypeToken<List<NewsInfoDetail>>(){}.getType());
								msg.what = 1;
								msg.obj = news;
								newsInfoDetail.addAll(news);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						handler.sendMessage(msg);
					}  
				});
		mQueue.add(stringRequest); 
	}


	//数据初始化
	private void initView() {
		mListView = (ListView) view.findViewById(R.id.lv_listview);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.pro_bar);
		//下拉刷新控件初始化
		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
		loadMoreView = LayoutInflater.from(view.getContext()).inflate(R.layout.loadmore, null);
		loadMoreButton = (TextView)loadMoreView.findViewById(R.id.loadMoreButton);
		IsNetworkStatu = ContentApplication.getInstance().IsNetWorkAvailable;

		DisplayMetrics outMetrics = new DisplayMetrics();
		((NewsActivity)getActivity()).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		currentW = outMetrics.widthPixels;
		currentH = outMetrics.heightPixels;

		LayoutInflater mInflater = LayoutInflater.from(view.getContext());
		handView = mInflater.inflate(R.layout.news_main_group_top_img, null);
		txtView = (TextView) handView.findViewById(R.id.page_top_txt);
		imgView = (ImageView) handView.findViewById(R.id.page_top_img);
		imgView.setScaleType(ScaleType.FIT_XY);
		imgView.setLayoutParams(new RelativeLayout.LayoutParams(currentW,(int)(currentW/1.5)));
		handView.setOnClickListener(new NoDoubleClickListener() {
			@Override
			public void onNoDoubleClick(View v) {
				ArtStart(0);
			}
		}); 
	}

	//点击列表item跳转
	private void ArtStart(int posstion) {
		Intent intent = new Intent(view.getContext(),ShowNewsInfoActivity.class);
		Bundle bundle = new Bundle();
		//传值   详细页URL、详细页Title、position
		bundle.putInt(NumUtil.NEWS_DATEIL_TITLE, mTitle);
		bundle.putInt(NumUtil.ID, posstion);
		bundle.putParcelable(NumUtil.JUMP_PARCELABLE, newsInfoDetail.get(posstion));
		intent.putExtra(NumUtil.NEWS_URL, bundle);
		startActivity(intent);
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1://第一次加载数据
				List<NewsInfoDetail> list =(List<NewsInfoDetail>)msg.obj;
				if(newsItemImageAdapter!=null){
					if(times!=1){
						loadMoreButton.setText("正在加载...");  //恢复按钮文字
						newsItemImageAdapter.listAddAll(list);
						newsItemImageAdapter.notifyDataSetChanged();
					}else{
						newsItemImageAdapter = new NewsItemImageAdapter(view.getContext(), list,options);
						mListView.setAdapter(newsItemImageAdapter);
						//mListView.addHeaderView(handView);
						setListEvent();
					}
				}else{
					newsItemImageAdapter = new NewsItemImageAdapter(view.getContext(), list,options);
					mListView.setAdapter(newsItemImageAdapter);
					//mListView.addHeaderView(handView);
					setListEvent();
				}
				break;
			case 0://获取数据失败
				loadMoreButton.setText("已经是最后一条数据了");  //恢复按钮文字
				checkLoad = false;
				break;
			default:
				break;
			}
		};
	}; 

	public void setListEvent(){
//				mListView.addFooterView(loadMoreView);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					times = 1;
					initQueue();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, mTitle);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					//跳转到详细页浏览
					ArtStart(position-1);
			}
		});
		mListView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					//滚动到底部
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						View v=(View) view.getChildAt(view.getChildCount()-1);
						int[] location = new  int[2] ;
						v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
						int y=location [1];
						if (view.getLastVisiblePosition()!=getLastVisiblePosition
								&& lastVisiblePositionY!=y)//第一次拖至底部
						{
							Toast.makeText(view.getContext(), "再次拖至底部，即可翻页",500).show();
							getLastVisiblePosition=view.getLastVisiblePosition();
							lastVisiblePositionY=y;
							return;
						}
						else if (view.getLastVisiblePosition()==getLastVisiblePosition
								&& lastVisiblePositionY==y)//第二次拖至底部
						{
							Log.e("GroupFragment", ">>>>>拖至底部");
							Log.e("GroupFragment", ">>>>>拖至底部>>>处理方法");
							times+=1;
							initQueue();
						}
					}
					//未滚动到底部，第二次拖至底部都初始化
					getLastVisiblePosition=0;
					lastVisiblePositionY=0;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
				//firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
				//visibleItemCount：当前能看见的列表项个数（小半个也算）
				//totalItemCount：列表项共数

				//判断是否滚到最后一行
				if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0){
					isLastRow =true;
				}
				if(!checkLoad){
					//Toast.makeText(view.getContext(), "数据全部加载完!", Toast.LENGTH_LONG).show();
					loadMoreButton.setText("没有数据了");  //恢复按钮文字
				}
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		if(newsInfoDetail.size()==0){
			times = 1;
			initQueue();
//		}
		if(newsItemImageAdapter == null)
		{
			newsItemImageAdapter = new NewsItemImageAdapter(view.getContext(), newsInfoDetail,options);
		}
		if(mListView!=null){
			mListView.removeHeaderView(handView);
			mListView.removeFooterView(loadMoreView);
		}
		mListView.addHeaderView(handView);
		newsItemImageAdapter.notifyDataSetChanged();
//		mListView.setAdapter(newsItemImageAdapter);
		setListEvent();
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