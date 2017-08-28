package com.mieasy.whrt_app_android_4.act.nav;

import java.util.ArrayList;
import java.util.List;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.adapter.NavItemAdapter;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 路线导航的线路站点名称列表
 * @author Administrator
 *
 */
public class NavItemFragment extends Fragment implements OnItemClickListener{
	private View view;
	private View handView;
	private TextView mHandViewText;
	private int standLineNumStr;	//轨道交通一号线  等

	private ListView mListView;
	private int strStationLine;		//线路  1、等
	private LiteOrm liteOrm;
	private List<Stations> StationList;

	private Stations stations;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.nav_list, null);
		liteOrm = ContentApplication.getInstance().liteOrm;
		initView();
		initListView();
		return view;
	}

	/*
	 * 初始化ListView
	 */
	private void initListView() {
		LayoutInflater mInflater = LayoutInflater.from(view.getContext());
		handView = mInflater.inflate(R.layout.nav_list_handview, null);
		mHandViewText = (TextView) handView.findViewById(R.id.tv_nav_handlist);
		mHandViewText.setText(standLineNumStr);

		mListView = (ListView) view.findViewById(R.id.lv_nav_listview);
		NavItemAdapter adapter = new NavItemAdapter(view.getContext(), StationList);
		mListView.addHeaderView(handView,null,false);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
	}

	private void initView() {
		StationList = new ArrayList<Stations>();

		Bundle bundle = getArguments();
		if(bundle!=null){
			int strStationLine =  (Integer) bundle.get(NumUtil.STATIONLINE);
			StationList = liteOrm.query(new QueryBuilder(Stations.class)
					.where(Stations.LINE_ID+ "=?", new String[]{strStationLine+""}));
			//StationList = liteOrm.query(Stations.class);
			standLineNumStr =  bundle.getInt(NumUtil.STATIONLINENUM);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Intent intent = new Intent(view.getContext(),ShowOutSideActivity.class);  
		Bundle bundle = new Bundle();
		stations =  StationList.get(position-1);				
		bundle.putParcelable(NumUtil.JUMP_BUNDLE,stations);		//传入该站的所有信息	
		intent.putExtra(NumUtil.JUMP_INTENT, bundle);			//bundle写入Intent
		startActivity(intent);
//		Toast.makeText(view.getContext(), "你点击了"+(position-1)+"条；名称是:"+StationList.get(position-1).getStationName(), 3000).show();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
}