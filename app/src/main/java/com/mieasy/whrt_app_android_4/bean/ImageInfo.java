package com.mieasy.whrt_app_android_4.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageInfo  implements Parcelable {
	private int isControl_V;
	private int isControl_H;
	private float bitmap_W;
	private float bitmap_H;
	private float screen_H;
	private float screen_W;
	
	
	public int getIsControl_V() {
		return isControl_V;
	}

	public void setIsControl_V(int isControl_V) {
		this.isControl_V = isControl_V;
	}

	public int getIsControl_H() {
		return isControl_H;
	}

	public void setIsControl_H(int isControl_H) {
		this.isControl_H = isControl_H;
	}

	public float getBitmap_W() {
		return bitmap_W;
	}

	public void setBitmap_W(float bitmap_W) {
		this.bitmap_W = bitmap_W;
	}

	public float getBitmap_H() {
		return bitmap_H;
	}

	public void setBitmap_H(float bitmap_H) {
		this.bitmap_H = bitmap_H;
	}

	public float getScreen_H() {
		return screen_H;
	}

	public void setScreen_H(float screen_H) {
		this.screen_H = screen_H;
	}

	public float getScreen_W() {
		return screen_W;
	}

	public void setScreen_W(float screen_W) {
		this.screen_W = screen_W;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public ImageInfo() {
		super();
	}

	public ImageInfo(int isControl_V, int isControl_H, float bitmap_W, float bitmap_H, float screen_H, float screen_W) {
		super();
		this.isControl_V = isControl_V;
		this.isControl_H = isControl_H;
		this.bitmap_W = bitmap_W;
		this.bitmap_H = bitmap_H;
		this.screen_H = screen_H;
		this.screen_W = screen_W;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(isControl_V);
		dest.writeInt(isControl_H);
		dest.writeFloat(bitmap_W);
		dest.writeFloat(bitmap_H);
		dest.writeFloat(screen_H);
		dest.writeFloat(screen_W);
	}
	
	public static final Parcelable.Creator<ImageInfo> CREATOR =new Creator<ImageInfo>(){

		@Override
		public ImageInfo createFromParcel(Parcel source) {
			return new ImageInfo(source.readInt(),source.readInt(),source.readFloat(),source.readFloat(),source.readFloat(),source.readFloat());
		}

		@Override
		public ImageInfo[] newArray(int size) {
			return new ImageInfo[size];
		}
		
	};
}
