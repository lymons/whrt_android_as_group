package com.mieasy.whrt_app_android_4.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 在建线路的json
 * @author Administrator
 *
 */
public class BuildingLine implements Parcelable{
	private String content;
	private String remark;
	private String pic;
	private String title;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BuildingLine() {
		super();
	}
	public BuildingLine(String content, String remark, String pic, String title) {
		super();
		this.content = content;
		this.remark = remark;
		this.pic = pic;
		this.title = title;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(content);
		dest.writeString(remark);
		dest.writeString(pic);
		dest.writeString(title);
	}
	
	public static final Parcelable.Creator<BuildingLine> CREATOR =new Creator<BuildingLine>() {

		@Override
		public BuildingLine[] newArray(int size) {
			return new BuildingLine[size];
		}

		@Override
		public BuildingLine createFromParcel(Parcel source) {
			return new BuildingLine(source.readString(),source.readString(),source.readString(),source.readString());
		}
	};
}
