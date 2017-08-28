package com.mieasy.whrt_app_android_4.bean;

import java.util.Arrays;
import java.util.List;

import com.mieasy.whrt_app_android_4.entity.Stations;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 新闻详细中的NewsInfoDetail下的Body对象
 * @author Administrator
 *
 */
public class Body  implements Parcelable {
	private int para_count;
	private int img_count;
	private List<Info> info;
	public Body(int para_count, int img_count, List<Info> info) {
		super();
		this.para_count = para_count;
		this.img_count = img_count;
		this.info = info;
	}
	public Body() {
		super();
	}
	public int getPara_count() {
		return para_count;
	}
	public void setPara_count(int para_count) {
		this.para_count = para_count;
	}
	public int getImg_count() {
		return img_count;
	}
	public void setImg_count(int img_count) {
		this.img_count = img_count;
	}
	public List<Info> getInfo() {
		return info;
	}
	public void setInfo(List<Info> info) {
		this.info = info;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(para_count);
		dest.writeInt(img_count);
		dest.writeParcelableArray((info.toArray(new Info[info.size()])), flags);
	}
	public static final Parcelable.Creator<Body> CREATOR =new Creator<Body>() {

		@Override
		public Body[] newArray(int size) {
			return new Body[size];
		}

		@Override
		public Body createFromParcel(Parcel source) {
			return new Body(source);
		}
	};
	 protected Body(Parcel parcel) {
         para_count = parcel.readInt();
         img_count = parcel.readInt();
         Parcelable[] pars = parcel.readParcelableArray(Info.class.getClassLoader());
         info = Arrays.asList(Arrays.asList(pars).toArray(new Info[pars.length]));
     }
}
