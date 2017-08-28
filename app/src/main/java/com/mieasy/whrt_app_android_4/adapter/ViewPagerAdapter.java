package com.mieasy.whrt_app_android_4.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter{
	private List<View> views;
	private Context context;

	/**
	 * 实例化View
	 * @param views
	 * @param context
	 */
	public ViewPagerAdapter(List<View> views, Context context) {
		super();
		this.views = views;
		this.context = context;
	}
	
	/**
	 * 销毁View
	 */
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}
	
	@Override
	public int getCount() {
		return views.size();
	}
	
	/**
	 * 用于确认instantiateItem是否返回了和关键对象有关的Page视图
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}
	
	/**
	 * 实例化View
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(views.get(position));
		return views.get(position);
	}
}