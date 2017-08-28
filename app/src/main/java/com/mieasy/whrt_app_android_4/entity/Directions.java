package com.mieasy.whrt_app_android_4.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Directions entity. 线路方向实体
 */
@Table("Directions")
public class Directions implements Parcelable {
	@Ignore
	public static String DIRECTION_ID="DIRECTION_ID";
	@Ignore
	public static String LINE_ID="LINE_ID";
	@Ignore
	public static String DIRECTION_NAME="DIRECTION_NAME";
	@Ignore
	public static String STATION_ID="STATION_ID";
	
	@Column(value = "DIRECTION_ID")
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	private Integer directionId;		//表ID
	
	@Column(value = "LINE_ID")
	private Integer lineId;				//线路ID

	@Column(value = "DIRECTION_NAME")
	private String directionName;		//开往的方向名称

	@Column(value = "STATION_ID")
	private Integer stationId;			//站名ID

	
	public Directions(Integer directionId,Integer lineId, String directionName, Integer stationId) {
		this.directionId = directionId;
		this.lineId = lineId;
		this.directionName = directionName;
		this.stationId = stationId;
	}

	public Integer getDirectionId() {
		return this.directionId;
	}

	public void setDirectionId(Integer directionId) {
		this.directionId = directionId;
	}

	public Integer getLineId() {
		return this.lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public String getDirectionName() {
		return this.directionName;
	}

	public void setDirectionName(String directionName) {
		this.directionName = directionName;
	}

	public Integer getStationId() {
		return this.stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(lineId);
		dest.writeInt(stationId);
		dest.writeString(directionName);
		dest.writeInt(stationId);
	}
	
	public static final Parcelable.Creator<Directions> CREATOR = new Creator<Directions>() {
		
		@Override
		public Directions[] newArray(int size) {
			return new Directions[size];
		}
		
		@Override
		public Directions createFromParcel(Parcel source) {
			return new Directions(source.readInt(),source.readInt(), source.readString(), source.readInt());
		}
	};
}