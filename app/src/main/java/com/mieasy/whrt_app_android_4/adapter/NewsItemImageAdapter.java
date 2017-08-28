package com.mieasy.whrt_app_android_4.adapter;

import java.util.List;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.news.NewsActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.NewsInfoDetail;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsItemImageAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<NewsInfoDetail> listNewsItem;
	private DisplayImageOptions options;
	
	public NewsItemImageAdapter(Context context, List<NewsInfoDetail> listNewsItem,DisplayImageOptions options) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.listNewsItem = listNewsItem;
		this.options = options;
	}

	@Override
	public int getCount() {
		return listNewsItem.size();
	}

	@Override
	public Object getItem(int position) {
		return listNewsItem.get(position);
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
			convertView = mInflater.inflate(R.layout.news_item, null);
			viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tv_item_txt);
			viewHolder.ivIcon = (ImageView)convertView.findViewById(R.id.iv_item_img);
			viewHolder.tvTitleSource = (TextView) convertView.findViewById(R.id.tv_item_txt_source);
			viewHolder.tvTitleTime = (TextView) convertView.findViewById(R.id.tv_item_txt_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHodler) convertView.getTag();
		}
		String url = listNewsItem.get(position).getPic();
		if(listNewsItem.get(position).getSource()!=null){
			viewHolder.tvTitleSource.setText(listNewsItem.get(position).getSource());
		}
		viewHolder.tvTitleTime.setText(listNewsItem.get(position).getPublishTime());
		ImageLoader.getInstance().displayImage(NumUtil.APP_URL_SERVER_2+"image/"+url, viewHolder.ivIcon, options);
		viewHolder.tvTitle.setText(listNewsItem.get(position).getTitle());
		return convertView;
	}
	
	class ViewHodler{
		public TextView tvTitle,tvTitleTime,tvTitleSource;
		public ImageView ivIcon;
	}
	
	public void listAddAll(List<NewsInfoDetail> list){
		listNewsItem.addAll(list);
	}
}