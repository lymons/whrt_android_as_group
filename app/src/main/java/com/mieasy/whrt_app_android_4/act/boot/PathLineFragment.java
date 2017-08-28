package com.mieasy.whrt_app_android_4.act.boot;

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
import android.widget.TextView;

import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.nav.ShowOutSideActivity;
import com.mieasy.whrt_app_android_4.adapter.PathInfoAdapter;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.entity.change.DetailsChange;
import com.mieasy.whrt_app_android_4.entity.change.PathInfoChange;
import com.mieasy.whrt_app_android_4.services.PathInfoChangeManager;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据库路线的信息引导
 * @author Administrator
 *
 */
public class PathLineFragment extends Fragment implements OnItemClickListener{
	private ListView mListView;

	private TextView mStationNum,mHuan,mPrice,mTime;
	private PathInfoAdapter mPathInfoAdapter;

	private View view;
	private LiteOrm liteOrm;
	private Stations startStation,stopStation;

	DecimalFormat df=new DecimalFormat("#.#");
	private List<Stations> stationsAllList;		//全部路线信息
	private HashMap<String,Integer> hashMap;	//全部重要站点信息   "list中的ID","方向信息"
	private int item = 0;							//记录换乘站在list中的id

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.map_boot_lineinfo, null);
		liteOrm = ContentApplication.getInstance().liteOrm;
		getStartStopPoint();
		setInfoView();
		return view;
	}

	private void setInfoView() {
		initView();
		new Thread(new mRunnable()).start();
	}

	//初始化控件
	private void initView() {
		mListView = (ListView) view.findViewById(R.id.lv_boot_pathinfo);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		stationsAllList = new ArrayList<Stations>();
		hashMap = new HashMap<String, Integer>();
	}

	//获取站点信息
	private void getStartStopPoint() {
		Bundle bundle = getArguments();
		if(bundle!=null){
			startStation = bundle.getParcelable(NumUtil.STATIONS_START);
			stopStation = bundle.getParcelable(NumUtil.STATIONS_STOP);
		}
	}
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what!=0){
				setInitViewInfo((List<PathInfoChange>)msg.obj);
			}
		};
	};

	/**
	 * 设置数据
	 * @param pathInfoChangeList
	 */
	private void setInitViewInfo(List<PathInfoChange> pathInfoChangeList){
		List<Stations> stationsList;
		HashMap<String,Integer> mapStation = new HashMap<String, Integer>();
		List<DetailsChange> mDetailsChangeList = pathInfoChangeList.get(0).getDetailsChange();
		for(int j= 0;j<mDetailsChangeList.size();j++){
			int dirId = mDetailsChangeList.get(j).getDirection_id();	//获取方向ID
			stationsList = mDetailsChangeList.get(j).getStationsList();
			item = stationsList.size()-1;
			for(int i = 0;i<stationsList.size();i++){					//遍历路线列表
				if(i==0){
					if(j==0){						//遍历第一行第一个站点
						hashMap.put("0", mDetailsChangeList.get(j).getDirection_id());
						stationsAllList.add(stationsList.get(i));
					}else{							//遍历多遍的第一个站
						item = stationsAllList.size();
						hashMap.put(item+"", mDetailsChangeList.get(j).getDirection_id());
						stationsAllList.add(stationsList.get(i));
					}
				}else{								//不是第一条数据
					if(i!=stationsList.size()-1){	//不是最后一条数据
						stationsAllList.add(stationsList.get(i));
					}
					if((j==mDetailsChangeList.size()-1)&&(i==stationsList.size()-1)){
						stationsAllList.add(stationsList.get(i));
					}
				}
			}
		}
		mPathInfoAdapter = new PathInfoAdapter(view.getContext(),stationsAllList);
		mPathInfoAdapter.addHuanInfoMap(hashMap);
		mListView.setAdapter(mPathInfoAdapter);
		mListView.setOnItemClickListener(this);
	}

	class mRunnable implements Runnable{
		@Override
		public void run() {
			PathInfoChangeManager.setLinesMap(liteOrm);
			PathInfoChangeManager.setDirectionsMap(liteOrm);
			List<PathInfoChange> pathInfoChangeList = PathInfoChangeManager.getAllPathInfo(liteOrm, startStation, stopStation);
			Message msg = Message.obtain();
			if(pathInfoChangeList.size()!=0){
				msg.what=1;
				msg.obj = pathInfoChangeList;
			}else{
				msg.what=0;
			}
			handler.sendMessage(msg);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(hashMap.get(position+"")!=null){
			jumpActivity(stationsAllList.get(position),2);
		}
		if((stationsAllList.size()-1)==position){//终点     跳转到出口信息
			jumpActivity(stationsAllList.get(position),0);
		}
	}

	//查看站外站外
	private void jumpActivity(Stations stationXY,int item) {
		Intent intent = new Intent(view.getContext(),ShowOutSideActivity.class);
		Bundle bundle = new Bundle();
		//传递页面选择信息
		intent.putExtra(NumUtil.STATION_ITEM, item);
		//传递站点信息
		bundle.putParcelable(NumUtil.JUMP_BUNDLE, stationXY);
		intent.putExtra(NumUtil.JUMP_INTENT, bundle);
		startActivity(intent);
	}
}