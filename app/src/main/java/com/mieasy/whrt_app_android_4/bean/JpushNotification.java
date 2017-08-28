package com.mieasy.whrt_app_android_4.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alan on 16-12-28.
 */
public class JpushNotification implements Parcelable {
    private String title;
    private String create_at;
    private String admin;
    private Content content;
    private String id;

    public JpushNotification(Parcel source) {
        title = source.readString();
        create_at = source.readString();
        admin = source.readString();
        content = source.readParcelable(Content.class.getClassLoader());
        id = source.readString();
    }

    public JpushNotification() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JpushNotification(String title, String create_at, String admin, Content content, String id) {
     super();
        this.title = title;
        this.create_at = create_at;
        this.admin = admin;
        this.content = content;
        this.id = id;
    }

    public static final Creator<JpushNotification> CREATOR =new Creator<JpushNotification>() {

        @Override
        public JpushNotification[] newArray(int size) {
            return new JpushNotification[size];
        }

        @Override
        public JpushNotification createFromParcel(Parcel source) {
            return new JpushNotification(source);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(create_at);
        dest.writeString(admin);
        dest.writeParcelable(content,flags);
        dest.writeString(id);
    }
}
