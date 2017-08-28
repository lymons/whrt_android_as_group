package com.mieasy.whrt_app_android_4.bean;


/**
 * Created by alan on 16-11-24.
 */
public class SiteCollection {
    private String id;
    private String userid;
    private String title;
    private String stationfrom;
    private String stationto;
    private String rank;
    private String type;
    private String addtime;

    public SiteCollection() {}

    public SiteCollection(String id, String userid, String title, String stationfrom, String stationto, String rank, String type, String addtime) {
        this.id = id;
        this.userid = userid;
        this.title = title;
        this.stationfrom = stationfrom;
        this.stationto = stationto;
        this.rank = rank;
        this.type = type;
        this.addtime = addtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStationfrom() {
        return stationfrom;
    }

    public void setStationfrom(String stationfrom) {
        this.stationfrom = stationfrom;
    }

    public String getStationto() {
        return stationto;
    }

    public void setStationto(String stationto) {
        this.stationto = stationto;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "SiteCollection{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", title='" + title + '\'' +
                ", stationfrom='" + stationfrom + '\'' +
                ", stationto='" + stationto + '\'' +
                ", rank='" + rank + '\'' +
                ", type='" + type + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
