package com.mieasy.whrt_app_android_4.adapter;

import java.util.List;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.news.NewsActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.Translation;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class AskItemAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<Translation> listTranslation;
	private int item_logo;
	
	public AskItemAdapter(Context context, List<Translation> listTranslation,int item_logo) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.listTranslation = listTranslation;
		this.item_logo = item_logo;
	}

	@Override
	public int getCount() {
		return listTranslation.size();
	}

	@Override
	public Object getItem(int position) {
		return listTranslation.get(position);
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
		viewHolder.tvTitle.setText(listTranslation.get(position).getTitle());
		viewHolder.tvTitle.setTextSize(15.0f);
		return convertView;
	}
	
	class ViewHodler{
		public TextView tvTitle;
		public ImageView tvImg;
	}
	
	public void addList(List<Translation> list){
		listTranslation.addAll(list);
	}
}