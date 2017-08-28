package com.mieasy.whrt_app_android_4.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 出口详细信息表
 */
@Table("EXIT_INFO")
public class ExitInfos implements Parcelable {

    @Ignore
    public static String STATION_ID="STATION_ID";

    @Ignore
    public static String STATION_NAME="STATION_NAME";

    @Ignore
    public static String EXIT_CODE="EXIT_CODE";

    @Ignore
    public static String INFO_1="INFO_1";

    @Ignore
    public static String INFO_1_EN="INFO_1_EN";

    @Ignore
    public static String INFO_2="INFO_2";

    @Ignore
    public static String INFO_2_EN="INFO_2_EN";


    @Column(value = "STATION_ID")
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private Integer stationID;

    @Column(value = "STATION_NAME")
    private String stationName;

    @Column(value = "EXIT_CODE")
    private String exitCode;

    @Column(value = "INFO_1")
    private String infoRoadName;

    @Column(value = "INFO_1_EN")
    private String infoRoadEnglishName;

    @Column(value = "INFO_2")
    private String infoDetialName;

    @Column(value = "INFO_2_EN")
    private String infoDetialEnglishName;

    protected ExitInfos(Parcel in) {
        stationName = in.readString();
        exitCode = in.readString();
        infoRoadName = in.readString();
        infoRoadEnglishName = in.readString();
        infoDetialName = in.readString();
        infoDetialEnglishName = in.readString();
    }

    public ExitInfos() {
    }


    public ExitInfos(Integer stationID, String stationName, String exitCode, String infoRoadName, String infoRoadEnglishName, String infoDetialName, String infoDetialEnglishName) {
        this.stationID = stationID;
        this.stationName = stationName;
        this.exitCode = exitCode;
        this.infoRoadName = infoRoadName;
        this.infoRoadEnglishName = infoRoadEnglishName;
        this.infoDetialName = infoDetialName;
        this.infoDetialEnglishName = infoDetialEnglishName;
    }

    public Integer getStationID() {
        return stationID;
    }

    public void setStationID(Integer stationID) {
        this.stationID = stationID;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getExitCode() {
        return exitCode;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    public String getInfoRoadName() {
        return infoRoadName;
    }

    public void setInfoRoadName(String infoRoadName) {
        this.infoRoadName = infoRoadName;
    }

    public String getInfoRoadEnglishName() {
        return infoRoadEnglishName;
    }

    public void setInfoRoadEnglishName(String infoRoadEnglishName) {
        this.infoRoadEnglishName = infoRoadEnglishName;
    }

    public String getInfoDetialName() {
        return infoDetialName;
    }

    public void setInfoDetialName(String infoDetialName) {
        this.infoDetialName = infoDetialName;
    }

    public String getInfoDetialEnglishName() {
        return infoDetialEnglishName;
    }

    public void setInfoDetialEnglishName(String infoDetialEnglishName) {
        this.infoDetialEnglishName = infoDetialEnglishName;
    }

    public static Creator<ExitInfos> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "ExitInfos{" +
                "stationID=" + stationID +
                ", stationName='" + stationName + '\'' +
                ", exitCode='" + exitCode + '\'' +
                ", infoRoadName='" + infoRoadName + '\'' +
                ", infoRoadEnglishName='" + infoRoadEnglishName + '\'' +
                ", infoDetialName='" + infoDetialName + '\'' +
                ", infoDetialEnglishName='" + infoDetialEnglishName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stationName);
        dest.writeString(exitCode);
        dest.writeString(infoRoadName);
        dest.writeString(infoRoadEnglishName);
        dest.writeString(infoDetialName);
        dest.writeString(infoDetialEnglishName);
    }


    public static final Creator<ExitInfos> CREATOR = new Creator<ExitInfos>() {
        @Override
        public ExitInfos createFromParcel(Parcel source) {
            return new ExitInfos(source);
        }

        @Override
        public ExitInfos[] newArray(int size) {
            return new ExitInfos[size];
        }
    };
}
