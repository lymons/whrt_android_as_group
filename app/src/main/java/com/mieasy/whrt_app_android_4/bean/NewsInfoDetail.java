package com.mieasy.whrt_app_android_4.bean;

import java.util.Arrays;
import java.util.List;

import com.mieasy.whrt_app_android_4.entity.Stations;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 新闻详细json
 * @author Administrator
 *
 */
public class NewsInfoDetail implements Parcelable {
	private int readNum;
	private String author;
	private String pic;
	private String publishTime;
	private String unid;
	private String dept;
	private String bigPic;
	private String source;
	private String title;
	private String summary;
	private Body body;
	public NewsInfoDetail(int readNum, String author, String pic, String publishTime, String unid, String dept,
			String bigPic, String source, String title, String summary, Body body) {
		super();
		this.readNum = readNum;
		this.author = author;
		this.pic = pic;
		this.publishTime = publishTime;
		this.unid = unid;
		this.dept = dept;
		this.bigPic = bigPic;
		this.source = source;
		this.title = title;
		this.summary = summary;
		this.body = body;
	}
	public NewsInfoDetail() {
		super();
	}
	public int getReadNum() {
		return readNum;
	}
	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getUnid() {
		return unid;
	}
	public void setUnid(String unid) {
		this.unid = unid;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getBigPic() {
		return bigPic;
	}
	public void setBigPic(String bigPic) {
		this.bigPic = bigPic;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(readNum);
		dest.writeString(author);
		dest.writeString(pic);
		dest.writeString(publishTime);
		dest.writeString(unid);
		dest.writeString(dept);
		dest.writeString(bigPic);
		dest.writeString(source);
		dest.writeString(title);
		dest.writeString(summary);
		dest.writeParcelable(body,flags);
	}
	public static final Parcelable.Creator<NewsInfoDetail> CREATOR =new Creator<NewsInfoDetail>() {

		@Override
		public NewsInfoDetail[] newArray(int size) {
			return new NewsInfoDetail[size];
		}

		@Override
		public NewsInfoDetail createFromParcel(Parcel source) {
			return new NewsInfoDetail(source);
		}
	};
	 protected NewsInfoDetail(Parcel parcel) {
		 readNum = parcel.readInt();
		 author = parcel.readString();
		 pic = parcel.readString();
		 publishTime = parcel.readString();
		 unid = parcel.readString();
		 dept = parcel.readString();
		 bigPic = parcel.readString();
		 source = parcel.readString();
		 title = parcel.readString();
		 summary = parcel.readString();
		 body = parcel.readParcelable(Body.class.getClassLoader());
     }
}
