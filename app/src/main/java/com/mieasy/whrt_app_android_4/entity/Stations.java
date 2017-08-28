package com.mieasy.whrt_app_android_4.entity;

import com.litesuits.orm.db.annotation.Check;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Stations entity. 站点信息表
 */
@Table("STATIONS")
public class Stations implements Parcelable {
	@Ignore
	public static String STATION_ID="STATION_ID";
	
	@Ignore
	public static String PHASE="PHASE";
	
	@Ignore
	public static String STATION_NUM="STATION_NUM";
	
	@Ignore
	public static String STATION_NAME="STATION_NAME";
	
	@Ignore
	public static String ENGLISH_NAME="ENGLISH_NAME";
	
	@Ignore
	public static String IMAGE="IMAGE";
	
	@Ignore
	public static String LINE_ID="LINE_ID";
	
	@Ignore
	public static String IS_TRANSFER="IS_TRANSFER";

	@Ignore
	public static String SEQUENCE="SEQUENCE";

	@Column(value = "STATION_ID")
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	private Integer stationId;				//站点ID

	@Column(value = "PHASE")
	private Integer phase;				//多少期

	@Column(value = "STATION_NUM")
	private Integer stationNum;			//站点ID

	@Column(value = "STATION_NAME")
	private String stationName;			//站点名称

	@Column(value = "ENGLISH_NAME")
	private String englishName;			//站点英文名称

	@Column(value = "IMAGE")
	private String image;				//站内图地址

	@Column(value = "LINE_ID")
	private Integer lineId;				//线路ID

	@Column(value = "IS_TRANSFER")
	@Check("IS_TRANSFER = 0 OR IS_TRANSFER < 1")
	private Integer isTransfer;			//是否是换乘站   1：是换乘站   0 ：不是

	@Column(value = "SEQUENCE")
	private Integer sequence;			//该站是该线路的第几站

	@Override
	public String toString() {
		return "Stations [stationId=" + stationId + ", phase=" + phase + ", stationNum=" + stationNum + ", stationName="
				+ stationName + ", englishName=" + englishName + ", image=" + image + ", lineId=" + lineId
				+ ", isTransfer=" + isTransfer + ", sequence=" + sequence + "]";
	}

	public Stations() {
		super();
	}

	public Stations(Integer stationId, Integer phase, Integer stationNum, String stationName, String englishName,
			String image, Integer lineId, Integer isTransfer, Integer sequence) {
		super();
		this.stationId = stationId;
		this.phase = phase;
		this.stationNum = stationNum;
		this.stationName = stationName;
		this.englishName = englishName;
		this.image = image;
		this.lineId = lineId;
		this.isTransfer = isTransfer;
		this.sequence = sequence;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Integer getPhase() {
		return phase;
	}

	public void setPhase(Integer phase) {
		this.phase = phase;
	}

	public Integer getStationNum() {
		return stationNum;
	}

	public void setStationNum(Integer stationNum) {
		this.stationNum = stationNum;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public Integer getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(Integer isTransfer) {
		this.isTransfer = isTransfer;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}



	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(stationId);
		dest.writeInt(phase);
		dest.writeInt(stationNum);
		dest.writeString(stationName);
		dest.writeString(englishName);
		dest.writeString(image);
		dest.writeInt(lineId);
		dest.writeInt(isTransfer);
		dest.writeInt(sequence);
	}

	public static final Parcelable.Creator<Stations> CREATOR =new Creator<Stations>() {

		@Override
		public Stations[] newArray(int size) {
			return new Stations[size];
		}

		@Override
		public Stations createFromParcel(Parcel source) {
			return new Stations(source.readInt(), source.readInt(), source.readInt(),  source.readString(),  source.readString(),  source.readString(),source.readInt(), source.readInt(), source.readInt());
		}
	};
}