package com.mieasy.whrt_app_android_4.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.bean.NewsInfoDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsItemAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<NewsInfoDetail> listNewsInfoDetail;
	private int itemLogo;
	
	public NewsItemAdapter(Context context, List<NewsInfoDetail> listNewsInfoDetail,int itemLogo) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.listNewsInfoDetail = listNewsInfoDetail;
		this.itemLogo = itemLogo;
	}

	@Override
	public int getCount() {
		return listNewsInfoDetail.size();
	}

	@Override
	public Object getItem(int position) {
		return listNewsInfoDetail.get(position);
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
			convertView = mInflater.inflate(R.layout.notice_list_item, null);
			viewHolder.ivImg = (ImageView) convertView.findViewById(R.id.notice_item_img);
			viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.notice_item_txt);
			viewHolder.tvTitleTime = (TextView) convertView.findViewById(R.id.notice_item_txt_time);
			viewHolder.tvTitleSource = (TextView) convertView.findViewById(R.id.notice_item_txt_source);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHodler) convertView.getTag();
		}
		viewHolder.ivImg.setImageResource(itemLogo);
		viewHolder.tvTitle.setText(listNewsInfoDetail.get(position).getTitle());
		if(listNewsInfoDetail.get(position).getSource()!=null){
			viewHolder.tvTitleSource.setText(listNewsInfoDetail.get(position).getSource());
		}
		viewHolder.tvTitleTime.setText(listNewsInfoDetail.get(position).getPublishTime());
		return convertView;
	}
	
	class ViewHodler{
		public TextView tvTitle,tvTitleTime,tvTitleSource;
		public ImageView ivImg;
	}
	
	public void listAddAll(List<NewsInfoDetail> list){
		listNewsInfoDetail.addAll(list);
	}
	
	public void refreshAddAll(){
		listNewsInfoDetail = new ArrayList<NewsInfoDetail>();
	}
}