package com.mieasy.whrt_app_android_4.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by alan on 16-12-28.
 */
public class Content implements Parcelable{
    private int para_count;
    private int img_count;
    private List<Info> info;
    public Content(int para_count, int img_count, List<Info> info) {
        super();
        this.para_count = para_count;
        this.img_count = img_count;
        this.info = info;
    }
    public Content() {
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
    public static final Creator<Body> CREATOR =new Creator<Body>() {

        @Override
        public Body[] newArray(int size) {
            return new Body[size];
        }

        @Override
        public Body createFromParcel(Parcel source) {
            return new Body(source);
        }
    };
    protected Content(Parcel parcel) {
        para_count = parcel.readInt();
        img_count = parcel.readInt();
        Parcelable[] pars = parcel.readParcelableArray(Info.class.getClassLoader());
        info = Arrays.asList(Arrays.asList(pars).toArray(new Info[pars.length]));
    }
}
