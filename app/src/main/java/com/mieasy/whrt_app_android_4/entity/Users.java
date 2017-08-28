package com.mieasy.whrt_app_android_4.entity;

import com.litesuits.orm.db.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Users entity. 用户信息
 */
@Table("Users")
public class Users implements Parcelable {
	// Fields

	private Integer userId;			//用户ID
	private Integer deviceId;		//设备ID
	private String userName;		//用户名
	private String password;		//用户密码
	private String nickName;		//昵称
	private String mobie;			//设备
	private String email;			//EMAIL

	/** minimal constructor */
	public Users(Integer deviceId) {
		this.deviceId = deviceId;
	}

	/** full constructor */
	public Users(Integer userId,Integer deviceId, String userName, String password,
			String nickName, String mobie, String email) {
		this.userId = userId;
		this.deviceId = deviceId;
		this.userName = userName;
		this.password = password;
		this.nickName = nickName;
		this.mobie = mobie;
		this.email = email;
	}

	// Property accessors

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobie() {
		return this.mobie;
	}

	public void setMobie(String mobie) {
		this.mobie = mobie;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(userId);
		dest.writeInt(deviceId);
		dest.writeString(userName);
		dest.writeString(nickName);
		dest.writeString(mobie);
		dest.writeString(email);
	}
	
	public static final Parcelable.Creator<Users> CREATOR = new Creator<Users>() {
		
		@Override
		public Users[] newArray(int size) {
			return new Users[size];
		}
		
		@Override
		public Users createFromParcel(Parcel source) {
			return new Users(source.readInt(),source.readInt(), source.readString(),source.readString(),source.readString(),source.readString(),source.readString());
		}
	};
}