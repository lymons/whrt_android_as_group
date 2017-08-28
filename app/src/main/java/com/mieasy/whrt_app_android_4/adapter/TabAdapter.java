package com.mieasy.whrt_app_android_4.adapter;

import java.util.List;

import com.mieasy.whrt_app_android_4.act.info.ItemFragment;
import com.mieasy.whrt_app_android_4.bean.ColumnInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter{
	private List<ColumnInfo> columnInfoList;
	
	public TabAdapter(FragmentManager fm,List<ColumnInfo> columnInfoList) {
		super(fm);
		this.columnInfoList = columnInfoList;
	}

	@Override
	public Fragment getItem(int item) {
		ItemFragment fragment = new ItemFragment();  
        Bundle bundle = new Bundle();
        String tit = columnInfoList.get(item).getItemTitle();
        int logo = columnInfoList.get(item).getItemLogo();
        String type = columnInfoList.get(item).getItemType();
        String strUrl = columnInfoList.get(item).getItemUrl();
        bundle.putString("Title", tit);
        bundle.putInt("logo", logo);
        bundle.putString("type", type);
        bundle.putString("strUrl", strUrl);
        fragment.setArguments(bundle);
        return fragment;
	}
	
    @Override
    public CharSequence getPageTitle(int position) {
    	return columnInfoList.get(position).getItemTitle();
    }  
  
    @Override  
    public int getCount() {
    	return columnInfoList.size(); 
    } 
}
