package com.mieasy.whrt_app_android_4.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Devices entity. 设备实体
 */
@Table("DEVICES")
public class Devices implements Parcelable {
	
	@Column(value = "DEVICE_ID")
	private Integer deviceId;		//设备ID
	
	@Column(value = "DEVICE_NAME")
	private String deviceName;			//设备名称

	public Devices(Integer deviceId, String deviceName) {
		super();
		this.deviceId = deviceId;
		this.deviceName = deviceName;
	}

	/** full constructor */
	public Devices(String deviceName) {
		this.deviceName = deviceName;
	}

	// Property accessors

	public Integer getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(deviceId); 
		dest.writeString(deviceName); 
	}
	
	 public static final Parcelable.Creator<Devices> CREATOR = new Creator<Devices>() {
		
		@Override
		public Devices[] newArray(int size) {
			return new Devices[size];
		}
		
		@Override
		public Devices createFromParcel(Parcel source) {
			return new Devices(source.readInt(), source.readString());
		}
	};
}