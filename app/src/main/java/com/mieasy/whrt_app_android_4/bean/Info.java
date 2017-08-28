package com.mieasy.whrt_app_android_4.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 新闻详细中的NewsInfoDetail下的Body对象下的Info对象
 * @author Administrator
 *
 */
public class Info implements Parcelable {
	private String content;
	private String type;
	
	public Info(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	
	public Info() {
		super();
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(content);
		dest.writeString(type);
		
	}
	public static final Parcelable.Creator<Info> CREATOR =new Creator<Info>() {

		@Override
		public Info[] newArray(int size) {
			return new Info[size];
		}

		@Override
		public Info createFromParcel(Parcel source) {
			return new Info(source.readString(),source.readString());
		}
	};
}
