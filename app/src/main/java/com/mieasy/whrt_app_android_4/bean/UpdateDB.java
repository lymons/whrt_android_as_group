package com.mieasy.whrt_app_android_4.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * CheckUpdateInfo下的UpdateDB对象
 * @author Administrator
 *
 */
public class UpdateDB implements Parcelable {
	private String createdTime;
	
	private String dbFile;
	
	private String dbVersion;
	
	private int size;
	
	public UpdateDB() {
		super();
	}

	public UpdateDB(String createdTime, String dbFile, String dbVersion, int size) {
		super();
		this.createdTime = createdTime;
		this.dbFile = dbFile;
		this.dbVersion = dbVersion;
		this.size = size;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getDbFile() {
		return dbFile;
	}

	public void setDbFile(String dbFile) {
		this.dbFile = dbFile;
	}

	public String getDbVersion() {
		return dbVersion;
	}

	public void setDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(createdTime);
		dest.writeString(dbFile);
		dest.writeString(dbVersion);
		dest.writeInt(size);
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<UpdateDB> CREATOR =new Creator<UpdateDB>() {
		@Override
		public UpdateDB[] newArray(int size) {
			return new UpdateDB[size];
		}

		@Override
		public UpdateDB createFromParcel(Parcel source) {
			return new UpdateDB(source.readString(),source.readString(),source.readString(),source.readInt());
		}
	};
}
