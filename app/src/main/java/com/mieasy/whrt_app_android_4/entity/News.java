package com.mieasy.whrt_app_android_4.entity;

import com.litesuits.orm.db.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * News entity. 新闻接口实体
 */
@Table("News")
public class News implements Parcelable {
	private Integer newsId;			//新闻ID
	private String newsName;		//新闻类别名称
	private String newsUrl;			//新闻URL接口

	/** full constructor */
	public News(Integer newsId,String newsName, String newsUrl) {
		this.newsId = newsId;
		this.newsName = newsName;
		this.newsUrl = newsUrl;
	}

	// Property accessors

	public Integer getNewsId() {
		return this.newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getNewsName() {
		return this.newsName;
	}

	public void setNewsName(String newsName) {
		this.newsName = newsName;
	}

	public String getNewsUrl() {
		return this.newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(newsId);
		dest.writeString(newsName);
		dest.writeString(newsUrl);
	}
	
	public static final Parcelable.Creator<News> CREATOR = new Creator<News>() {
		
		@Override
		public News[] newArray(int size) {
			return new News[size];
		}
		
		@Override
		public News createFromParcel(Parcel source) {
			return new News(source.readInt(), source.readString(), source.readString());
		}
	};
}