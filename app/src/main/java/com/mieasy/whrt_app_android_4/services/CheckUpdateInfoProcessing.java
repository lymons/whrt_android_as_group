package com.mieasy.whrt_app_android_4.services;

import com.mieasy.whrt_app_android_4.bean.CheckUpdateInfo;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.content.Context;

public class CheckUpdateInfoProcessing {
	/**
	 * 后期改成SharePreference获取数据
	 * @param context
	 * @param oldCheckUpdateInfo
	 * @param newCheckUpdateInfo
	 */
	public static void checkUpdateInfoCheck(Context context,CheckUpdateInfo oldCheckUpdateInfo,CheckUpdateInfo newCheckUpdateInfo){
		//轮播图是否改变
		if((oldCheckUpdateInfo.getHomePageUpdateTime().compareTo(newCheckUpdateInfo.getHomePageUpdateTime())!=0)){
			NumUtil.indexDateMap.put(NumUtil.HOMEPAGETAG, newCheckUpdateInfo.getHomePageUpdateTime());
		}
		//判断数据库有没有更新
		if(oldCheckUpdateInfo.getDb().getCreatedTime().compareTo(newCheckUpdateInfo.getDb().getCreatedTime())!=0){
			checkUpdateInfoCheck(context,newCheckUpdateInfo);
		}
		//在建线路
		if(oldCheckUpdateInfo.getBuildingLineUpdateTime().compareTo(newCheckUpdateInfo.getBuildingLineUpdateTime())!=0){
			NumUtil.indexDateMap.put(NumUtil.BUILDINGLINE, newCheckUpdateInfo.getBuildingLineUpdateTime());
		}
		//在线问答
		if(oldCheckUpdateInfo.getQaUpdateTime().compareTo(newCheckUpdateInfo.getQaUpdateTime())!=0){
			NumUtil.indexDateMap.put(NumUtil.QAUPDATETIME, newCheckUpdateInfo.getQaUpdateTime());
		}
		//新闻
		if(oldCheckUpdateInfo.getNewsUpdateTime().compareTo(newCheckUpdateInfo.getNewsUpdateTime())!=0){
			NumUtil.indexDateMap.put(NumUtil.NEWSUPDATETIME, newCheckUpdateInfo.getNewsUpdateTime());
		}
	}
	
	/**
	 * 第一次访问时数据库处理
	 * @param newCheckUpdateInfo
	 */
	public static void checkUpdateInfoCheck(Context context,CheckUpdateInfo newCheckUpdateInfo){
		NumUtil.indexDateMap.put(NumUtil.ANDROIDDB, newCheckUpdateInfo.getDb().getDbFile());
		NumUtil.DB_SIZE = newCheckUpdateInfo.getDb().getSize();
	}
}