package com.mieasy.whrt_app_android_4.adapter;

import java.util.List;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.news.NewsActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.entity.Stations;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NavItemAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<Stations> StationList;
	
	public NavItemAdapter(Context context,List<Stations> StationList ) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.StationList = StationList;
	}

	@Override
	public int getCount() {
		return StationList.size();
	}

	@Override
	public Object getItem(int position) {
		return StationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHolder = null;
		if(convertView==null){
			viewHolder = new ViewHodler();
			convertView = mInflater.inflate(R.layout.nav_item_station_name, null);
			viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tv_item_station_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHodler) convertView.getTag();
		}
		viewHolder.tvTitle.setText(StationList.get(position).getStationName());
		return convertView;
	}
	
	class ViewHodler{
		public TextView tvTitle;
	}
}