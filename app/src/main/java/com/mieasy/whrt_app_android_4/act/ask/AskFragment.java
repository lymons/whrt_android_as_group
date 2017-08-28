package com.mieasy.whrt_app_android_4.act.ask;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.adapter.AskItemAdapter;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.Translation;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.view.RefreshableView;
import com.mieasy.whrt_app_android_4.view.RefreshableView.PullToRefreshListener;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class AskFragment extends Fragment{
	private int currentW,currentH;

	private View view;
	private View handView;
	private ImageView imgView;
	private TextView txtView;
	private ListView mListView;
	private RefreshableView refreshableView;
	private Gson gson = new Gson();
	//新闻类型 
	private String prefixUrl = NumUtil.APP_URL_SERVER_1;
	private String centerUrl;
	private int times = 1;
	private String suffixUrl = NumUtil.JSON_SUFFIX;
	private String url = prefixUrl+centerUrl+times+suffixUrl;

	private int getLastVisiblePosition = 0,lastVisiblePositionY=0;
	private boolean IsNetworkStatu;
	private int item_logo;
	private int strTitle;
	private List<Translation> listTranslation;
	private AskItemAdapter askItemAdapter;
	private View loadMoreView;
	private TextView loadMoreButton;
	private RequestQueue mQueue;

	private boolean checkLoad = true;
	private boolean isLastRow = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.news_main_group, null);
		initView();
		initListView();
		return view;
	}

	private void initListView() {
		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
		mListView = (ListView) view.findViewById(R.id.lv_listview);
		url = prefixUrl+centerUrl+times+suffixUrl;
		mQueue = ContentApplication.getInstance().mQueue;
		initQueue();
	}

	//数据初始化
	private void initView() {
		loadMoreView = LayoutInflater.from(view.getContext()).inflate(R.layout.loadmore, null);
		loadMoreButton = (TextView)loadMoreView.findViewById(R.id.loadMoreButton);
		//有无网络
		IsNetworkStatu = ContentApplication.getInstance().IsNetWorkAvailable;
		listTranslation = new ArrayList<Translation>();
		//获取父Activity传过来的数据
		Bundle bundle= getArguments();
		if(bundle!=null){
			centerUrl = bundle.getString(NumUtil.NEWS_URL);
			item_logo = bundle.getInt(NumUtil.ITEM_LOGO);
			strTitle = bundle.getInt(NumUtil.NEWS_DATEIL_TITLE);
			if(item_logo==0){
				item_logo = R.drawable.notice_img;
			}
		}
	}

	public void startAsk(int itemID){
		Intent intent = new Intent(view.getContext(),AskInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(NumUtil.NEWS_DATEIL_TITLE, strTitle);
		bundle.putParcelable(NumUtil.JUMP_PARCELABLE, listTranslation.get(itemID));
		bundle.putInt(NumUtil.ID, itemID);
		intent.putExtra(NumUtil.NEWS_URL, bundle);
		startActivity(intent);
	}

	public void setListEvent(){
		mListView.addFooterView(loadMoreView);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//跳转到详细页浏览
				startAsk(position);
			}
		});
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, item_logo);
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
					Toast.makeText(view.getContext(), "数据全部加载完!", Toast.LENGTH_LONG).show();
					loadMoreButton.setText("没有数据了");  //恢复按钮文字
				}
			}

		});
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1://第一次加载数据
				if(askItemAdapter!=null){
					loadMoreButton.setText("正在加载...");  //恢复按钮文字
					askItemAdapter.addList((List<Translation>)msg.obj);
					askItemAdapter.notifyDataSetChanged();
				}else{
					askItemAdapter = new AskItemAdapter(view.getContext(), (List<Translation>)msg.obj,item_logo);
					mListView.setAdapter(askItemAdapter);
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
	private void initQueue() {
		url = prefixUrl+centerUrl+times+suffixUrl;

		StringRequest stringRequest = new StringRequest(url,  
				new Response.Listener<String>() {  
			@Override  
			public void onResponse(String str) {
				List<Translation> news = new ArrayList<Translation>();
				Message msg = Message.obtain();
				if(str.length()>0){
					news = gson.fromJson(str, new TypeToken<List<Translation>>(){}.getType());
					msg.what = 1;
					msg.obj = news;
					listTranslation.addAll(news);
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
}