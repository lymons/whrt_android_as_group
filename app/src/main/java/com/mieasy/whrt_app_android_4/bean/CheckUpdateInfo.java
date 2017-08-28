package com.mieasy.whrt_app_android_4.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 打开应用开始获取的indexjson检查的json对象
 * @author Administrator
 *
 */
public class CheckUpdateInfo implements Parcelable {
	private String homePageUpdateTime;			//轮播图片更新时间
	private UpdateDB db;					//Android数据库更新时间
	private String buildingLineUpdateTime;		//在建线路更新时间
	private String qaUpdateTime;				//在线问答更新时间
	private String newsUpdateTime;				//新闻更新时间
	
	public CheckUpdateInfo(String homePageUpdateTime, UpdateDB db, String buildingLineUpdateTime,
			String qaUpdateTime, String newsUpdateTime) {
		super();
		this.homePageUpdateTime = homePageUpdateTime;
		this.db = db;
		this.buildingLineUpdateTime = buildingLineUpdateTime;
		this.qaUpdateTime = qaUpdateTime;
		this.newsUpdateTime = newsUpdateTime;
	}

	public CheckUpdateInfo() {
		super();
	}
	
	public UpdateDB getDb() {
		return db;
	}

	public void setDb(UpdateDB db) {
		this.db = db;
	}

	public String getHomePageUpdateTime() {
		return homePageUpdateTime;
	}

	public void setHomePageUpdateTime(String homePageUpdateTime) {
		this.homePageUpdateTime = homePageUpdateTime;
	}

	public String getBuildingLineUpdateTime() {
		return buildingLineUpdateTime;
	}

	public void setBuildingLineUpdateTime(String buildingLineUpdateTime) {
		this.buildingLineUpdateTime = buildingLineUpdateTime;
	}

	public String getQaUpdateTime() {
		return qaUpdateTime;
	}

	public void setQaUpdateTime(String qaUpdateTime) {
		this.qaUpdateTime = qaUpdateTime;
	}

	public String getNewsUpdateTime() {
		return newsUpdateTime;
	}

	public void setNewsUpdateTime(String newsUpdateTime) {
		this.newsUpdateTime = newsUpdateTime;
	}

	public static Parcelable.Creator<CheckUpdateInfo> getCreator() {
		return CREATOR;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(homePageUpdateTime);
		dest.writeParcelable(db,flags);
		dest.writeString(buildingLineUpdateTime);
		dest.writeString(qaUpdateTime);
		dest.writeString(newsUpdateTime);
	}
	
	public static final Parcelable.Creator<CheckUpdateInfo> CREATOR =new Creator<CheckUpdateInfo>() {

		@Override
		public CheckUpdateInfo[] newArray(int size) {
			return new CheckUpdateInfo[size];
		}

		@Override
		public CheckUpdateInfo createFromParcel(Parcel source) {
			return new CheckUpdateInfo(source);
		}
	};
	
	 protected CheckUpdateInfo(Parcel parcel) {
		 homePageUpdateTime = parcel.readString();
		 qaUpdateTime = parcel.readString();
		 newsUpdateTime = parcel.readString();
		 buildingLineUpdateTime = parcel.readString();
		 db = parcel.readParcelable(UpdateDB.class.getClassLoader());
     }

	@Override
	public int describeContents() {
		return 0;
	}
}