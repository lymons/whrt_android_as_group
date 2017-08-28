package com.mieasy.whrt_app_android_4.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by alan on 16-12-13.
 */
@Table("WhrtsInfo")
public class SiteCollect implements Parcelable {
    @Ignore
    public static final String ID ="ID";
    @Column(value = "ID")
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private Integer id;
    @Ignore
    public static final String STATION_ID ="STATION_ID";
    @Column("STATION_ID")
    private String stationId;
    @Ignore
    public static final String STATIONNAME ="STATIONNAME";
    @Column("STATIONNAME")
    private String stationName;
    public static final String STATIONTOID ="STATIONTOID";
    @Column("STATIONTOID")
    private String stationtoId;
    public static final String STATIONTO ="STATIONTO";
    @Column("STATIONTO")
    private String stationto;
    public static final String TYPE ="TYPE";
    @Column("TYPE")
    private String type;

    public SiteCollect(Parcel in) {
        stationId = in.readString();
        stationName = in.readString();
        stationtoId = in.readString();
        stationto = in.readString();
        type = in.readString();
    }

    public static final Creator<SiteCollect> CREATOR = new Creator<SiteCollect>() {
        @Override
        public SiteCollect createFromParcel(Parcel in) {
            return new SiteCollect(in);
        }

        @Override
        public SiteCollect[] newArray(int size) {
            return new SiteCollect[size];
        }
    };

    public SiteCollect() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationto() {
        return stationto;
    }

    public void setStationto(String stationto) {
        this.stationto = stationto;
    }

    public String getStationtoId() {
        return stationtoId;
    }

    public void setStationtoId(String stationtoId) {
        this.stationtoId = stationtoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(stationId);
        dest.writeString(stationName);
        dest.writeString(stationtoId);
        dest.writeString(stationto);
        dest.writeString(type);
    }

    @Override
    public String toString() {
        return "SiteCollect{" +
                "id=" + id +
                ", stationId='" + stationId + '\'' +
                ", stationName='" + stationName + '\'' +
                ", stationtoId='" + stationtoId + '\'' +
                ", stationto='" + stationto + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
