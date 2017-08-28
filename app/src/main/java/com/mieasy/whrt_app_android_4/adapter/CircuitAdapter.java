package com.mieasy.whrt_app_android_4.adapter;

import java.util.List;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.bean.BuildingLine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CircuitAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<BuildingLine> strList;
	private int item_logo;
	
	public CircuitAdapter(Context context,List<BuildingLine> strList,int item_logo) {
		super();
		this.item_logo = item_logo;
		this.mInflater = LayoutInflater.from(context);
		this.strList = strList;
	}

	@Override
	public int getCount() {
		return strList.size();
	}

	@Override
	public Object getItem(int position) {
		return strList.get(position);
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
			convertView = mInflater.inflate(R.layout.circuit_list_item, null);
			viewHolder.tvImg = (ImageView) convertView.findViewById(R.id.notice_item_img);
			viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.notice_item_txt);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHodler) convertView.getTag();
		}
		viewHolder.tvImg.setImageResource(item_logo);
		viewHolder.tvTitle.setText(strList.get(position).getTitle());
		viewHolder.tvTitle.setTextSize(18.0f);
		return convertView;
	}
	
	class ViewHodler{
		public TextView tvTitle;
		public ImageView tvImg;
	}
}
