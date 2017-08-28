package com.mieasy.whrt_app_android_4.act.news;

import java.util.ArrayList;
import java.util.List;

import com.mieasy.whrt_app_android_4.view.RefreshableView;
import com.mieasy.whrt_app_android_4.view.RefreshableView.PullToRefreshListener;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.adapter.NewsItemAdapter;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.NewsInfoDetail;
import com.mieasy.whrt_app_android_4.util.FileUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TenderFragment extends Fragment{
	private View view;
	private ListView mListView;
	private Gson gson = new Gson();
	private RefreshableView refreshableView;
	private NewsItemAdapter newsItemAdapter;
	private View loadMoreView;
	private TextView loadMoreButton;

	//新闻类型 
	private int item_logo;
	private int mTitle;

	private List<NewsInfoDetail> newsInfoDetail;

	private String prefixUrl = NumUtil.APP_URL_SERVER_2;
	private String centerUrl;
	private int times = 1;
	private String suffixUrl = NumUtil.JSON_SUFFIX;
	private String url = prefixUrl+centerUrl+times+suffixUrl;

	private int getLastVisiblePosition = 0,lastVisiblePositionY=0;
	private boolean checkLoad = true;
	private boolean isLastRow = false;
	private RequestQueue mQueue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.news_main_group,container, false);
		initListView();
		return view;
	}

	public static TenderFragment newsInstance(String strUrl,int logoId,int titId){
		TenderFragment mTenderFragment = new TenderFragment();
		Bundle bundle = new Bundle();
		bundle.putString(NumUtil.NEWS_URL, strUrl);
		bundle.putInt(NumUtil.ITEM_LOGO, logoId);
		bundle.putInt(NumUtil.NEWS_DATEIL_TITLE,titId);
		mTenderFragment.setArguments(bundle);
		return mTenderFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		newsInfoDetail = new ArrayList<NewsInfoDetail>();
		//获取父Activity传过来的数据
		Bundle  bundle= getArguments();
		if(bundle!=null){
			centerUrl = bundle.getString(NumUtil.NEWS_URL);
			item_logo = bundle.getInt(NumUtil.ITEM_LOGO);
			mTitle = bundle.getInt(NumUtil.NEWS_DATEIL_TITLE);
			if(item_logo==0){
				item_logo = R.drawable.notice_img;
			}
		}
	}

	//UI线程用于处理数据的显示
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1://第一次加载数据
				List<NewsInfoDetail> list = new ArrayList((List<NewsInfoDetail>)msg.obj);
				if(newsItemAdapter!=null){
					if(times!=1){
						loadMoreButton.setText("正在加载...");  //恢复按钮文字
						newsItemAdapter.listAddAll(list);
						newsItemAdapter.notifyDataSetChanged();
					}else{
						newsItemAdapter = new NewsItemAdapter(view.getContext(), list,item_logo);
						mListView.setAdapter(newsItemAdapter);
						setListEvent();
					}
				}else{
					newsItemAdapter = new NewsItemAdapter(view.getContext(), list,item_logo);
					newsItemAdapter.refreshAddAll();
					mListView.setAdapter(newsItemAdapter);
					setListEvent();
				}
				break;
			case 0:											   //获取数据失败
				loadMoreButton.setText("已经是最后一条数据了");  //恢复按钮文字
				checkLoad = false;
				break;
			default:
				break;
			}
		};
	}; 

	//处理控件的初始化以及异步网络获取数据
	private void initListView() {
		//下拉刷新控件初始化
		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
		loadMoreView = LayoutInflater.from(view.getContext()).inflate(R.layout.loadmore, null);
		loadMoreButton = (TextView)loadMoreView.findViewById(R.id.loadMoreButton);
		mListView = (ListView) view.findViewById(R.id.lv_listview);

		mQueue = ContentApplication.getInstance().mQueue;
		//		mQueue = Volley.newRequestQueue(view.getContext());
		//需要更新   或者   本地不存在
//		initQueue();
	}

	//异步读取本地数据
	private void readLocalFile(String houStr){
		new AsyncTask<String, Void, List<NewsInfoDetail>>(){

			@Override
			protected List<NewsInfoDetail> doInBackground(String... params) {
				String str = "";
				List<NewsInfoDetail> news = new ArrayList<NewsInfoDetail>();
				try{
					str = FileUtil.readStorageFileString(params[0]);
					news = gson.fromJson(str, new TypeToken<List<NewsInfoDetail>>(){}.getType());
					return null;
				}catch(NotFoundException notFoundE){
					notFoundE.printStackTrace();
				}catch (JsonParseException jsonParse) {
					jsonParse.printStackTrace();
				}
				//				if(news.size()<30){
				//					checkLoad = false;
				//					loadMoreButton.setVisibility(View.INVISIBLE);
				//				}
				return news;
			}

			protected void onPostExecute(java.util.List<NewsInfoDetail> news) {
				Message msg = Message.obtain();
				if(news.size()!=0){
					msg.what = 1;
					msg.obj = news;
					newsInfoDetail.addAll(news);
				}else{
					msg.what = 0;
				}
				handler.sendMessage(msg);
			};
		}.execute(houStr);
	}

	//网络获取新闻
	private void initQueue() {
		url = prefixUrl+centerUrl+times+suffixUrl;
		StringRequest stringRequest = new StringRequest(url,  
				new Response.Listener<String>() {  
			@Override  
			public void onResponse(String str) {
				List<NewsInfoDetail> news = new ArrayList<NewsInfoDetail>();
				Message msg = Message.obtain();
				if(str.length()>0){
					try {
						FileUtil.saveStorageFile(view.getContext(),str, centerUrl+times+suffixUrl);
						news = gson.fromJson(str, new TypeToken<List<NewsInfoDetail>>(){}.getType());
						msg.what = 1;
						msg.obj = news;
						newsInfoDetail.addAll(news);
					} catch (Exception e) {
						e.printStackTrace();
						msg.what = 0;
					}
				}else{
					msg.what = 0;
				}
				handler.sendMessage(msg);
			}
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) {  
				Message msg = Message.obtain();
				msg.what = 0;
				handler.sendMessage(msg);
			}  
		});  
		mQueue.add(stringRequest); 
	}

	private void ArtStart(int position) {
		Intent intent = new Intent(view.getContext(),ShowNewsInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(NumUtil.ID, position);
		bundle.putInt(NumUtil.NEWS_DATEIL_TITLE, mTitle);
		NewsInfoDetail nn = newsInfoDetail.get(position);
		bundle.putParcelable(NumUtil.JUMP_PARCELABLE, nn);
		intent.putExtra(NumUtil.NEWS_URL, bundle);
		startActivity(intent);
	}

	//
	public void setListEvent(){
		//		mListView.addFooterView(loadMoreView,null,false);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					times = 1;
					initQueue();
					Thread.sleep(1000);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, item_logo);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//跳转到详细页浏览
				ArtStart(position);
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
							//							Toast.makeText(view.getContext(), "再次拖至底部，即可翻页",500).show();
							getLastVisiblePosition=view.getLastVisiblePosition();
							lastVisiblePositionY=y;
							return;
						}
						else if (view.getLastVisiblePosition()==getLastVisiblePosition
								&& lastVisiblePositionY==y)//第二次拖至底部
						{
							if(!checkLoad){
								loadMoreButton.setText("已经是最后一条数据了");  //恢复按钮文字
								Toast.makeText(view.getContext(), "已经是最后一条了",500).show();
							}else {
								times+=1;
								initQueue();
							}
							//需要更新   或者   本地不存在
							//if((!bool)||!FileUtil.checkFilePathIsExist(view.getContext(),centerUrl+times+suffixUrl)){ 
							//}else{//不需要更新  并且  本地存在
							//readLocalFile(centerUrl+times+suffixUrl);
							//}
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
		if(newsInfoDetail.size()==0){
			initQueue();
		}
		if(newsItemAdapter == null)
		{
			newsItemAdapter = new NewsItemAdapter(view.getContext(), newsInfoDetail,item_logo);
		}
		if(mListView!=null){
			mListView.removeFooterView(loadMoreView);
		}
		mListView.setAdapter(newsItemAdapter);
		setListEvent();
	}

//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		if (isVisibleToUser) {
//			if(mQueue!=null&&mQueue.getSequenceNumber()!=0){
//				mQueue.start();
//			}
//		} else {
//			if(mQueue!=null&&mQueue.getSequenceNumber()!=0){
//				mQueue.stop();
//			}
//		}
//	}
}