package com.mieasy.whrt_app_android_4.act.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.news.ShowImgActivity;
import com.mieasy.whrt_app_android_4.adapter.CircuitAdapter;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.BuildingLine;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.view.RefreshableView;
import com.mieasy.whrt_app_android_4.view.RefreshableView.PullToRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 线路图Fragment
 * @author Administrator
 */
public class CircuitFragment extends Fragment implements OnItemClickListener{
	private View view;
	private RefreshableView refreshableView;
	private ListView mListView;
	
	private Boolean IsNetworkStatu;
	private String netWorkUrl;
	private int item_logo;
	private int mTitle;
	
	private List<String> str;
	private boolean bool = false;	//更新否
	Gson gson = new Gson();
	private RequestQueue mQueue;
	private String url = "";
	private List<BuildingLine> buildList;
	private CircuitAdapter circuitAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.news_main_group, null);
		initView();
		return view;
	}
	
	private void initListView() {
		mListView = (ListView) view.findViewById(R.id.lv_listview);
		circuitAdapter = new CircuitAdapter(view.getContext(),buildList,item_logo);
		mListView.setAdapter(circuitAdapter);
		mListView.setOnItemClickListener(this);
		//下拉刷新控件初始化
		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					initQueue();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, item_logo);
	}
	
	//数据初始化
	private void initView() {
		//有无网络
		IsNetworkStatu = ContentApplication.getInstance().IsNetWorkAvailable;
		mQueue  = ContentApplication.getInstance().mQueue;
		//获取父Activity传过来的数据
		Bundle  bundle= getArguments();
		if(bundle!=null){
			url = bundle.getString(NumUtil.NEWS_URL);			//获取后缀
			item_logo = bundle.getInt(NumUtil.ITEM_LOGO);		//item img
			mTitle = bundle.getInt(NumUtil.NEWS_DATEIL_TITLE);	//详细页标题
		}
		netWorkUrl = NumUtil.APP_URL_SERVER_2+url;			//详细的URL
		if(url.length()!=0){
			initQueue();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Intent intent = new Intent(view.getContext(),ShowCircuitActivity.class);
		Intent intent = new Intent(view.getContext(),ShowImgActivity.class);
		intent.putExtra(NumUtil.JUMP_INTENT, buildList.get(position));
		startActivity(intent);
	}
	
	//网络获取新闻
	private void initQueue() {
		StringRequest stringRequest = new StringRequest(netWorkUrl,
				new Response.Listener<String>() {
			@Override  
			public void onResponse(String str) {
				buildList = new ArrayList<BuildingLine>();
				Message msg = Message.obtain();
				if(str.length()>0){
					try {
						buildList = gson.fromJson(str, new TypeToken<List<BuildingLine>>(){}.getType());
						msg.what = 1;
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
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what!=0){
				initListView();
			}
		};
	};

	public static CircuitFragment newsInstance(String strUrl, int titId, int typeId){
		CircuitFragment mGroupFragment = new CircuitFragment();
		Bundle bundle = new Bundle();
		bundle.putString(NumUtil.NEWS_URL, strUrl);					//传递列表URL
		bundle.putInt(NumUtil.ITEM_LOGO, titId);
		bundle.putInt(NumUtil.NEWS_DATEIL_TITLE, typeId);
		mGroupFragment.setArguments(bundle);
		return mGroupFragment;
	}
}