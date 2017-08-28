package com.mieasy.whrt_app_android_4.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Timetable entity. 站点时间信息
 */
@Table("Timetable")
public class Timetable implements Parcelable {
	@Ignore
	public static String STATION_ID="STATION_ID";
	@Ignore
	public static String DIRECTION_ID="DIRECTION_ID";
	@Ignore
	public static String WEEKDAY_FIRST="WEEKDAY_FIRST";
	@Ignore
	public static String WEEKDAY_LAST="WEEKDAY_LAST";
	@Ignore
	public static String WEEKEND_FIRST="WEEKEND_FIRST";
	@Ignore
	public static String WEEKEND_LAST="WEEKEND_LAST";
	
	@Column(value = "STATION_ID")
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	private Integer stationId;			//站点ID
	
	@Column(value = "DIRECTION_ID")
	private Integer directionId;		//方向ID
	
	@Column(value = "WEEKDAY_FIRST")
	private String weekdayFirst;		//工作日第一班
	
	@Column(value = "WEEKDAY_LAST")
	private String weekdayLast;			//工作日最后一般
	
	@Column(value = "WEEKEND_FIRST")
	private String weekendFirst;		//周末第一班
	
	@Column(value = "WEEKEND_LAST")
	private String weekendLast;			//周末最后一般

	public Timetable(Integer stationId, Integer directionId,
			String weekdayFirst, String weekdayLast, String weekendFirst,
			String weekendLast) {
		super();
		this.stationId = stationId;
		this.directionId = directionId;
		this.weekdayFirst = weekdayFirst;
		this.weekdayLast = weekdayLast;
		this.weekendFirst = weekendFirst;
		this.weekendLast = weekendLast;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Integer getDirectionId() {
		return directionId;
	}

	public void setDirectionId(Integer directionId) {
		this.directionId = directionId;
	}

	public String getWeekdayFirst() {
		return weekdayFirst;
	}

	public void setWeekdayFirst(String weekdayFirst) {
		this.weekdayFirst = weekdayFirst;
	}

	public String getWeekdayLast() {
		return weekdayLast;
	}

	public void setWeekdayLast(String weekdayLast) {
		this.weekdayLast = weekdayLast;
	}

	public String getWeekendFirst() {
		return weekendFirst;
	}

	public void setWeekendFirst(String weekendFirst) {
		this.weekendFirst = weekendFirst;
	}

	public String getWeekendLast() {
		return weekendLast;
	}

	public void setWeekendLast(String weekendLast) {
		this.weekendLast = weekendLast;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(stationId);
		dest.writeInt(directionId);
		dest.writeString(weekdayFirst);
		dest.writeString(weekdayLast);
		dest.writeString(weekendLast);
		dest.writeString(weekendLast);
	}
	
	public static final Parcelable.Creator<Timetable> CREATOR = new Creator<Timetable>() {
		
		@Override
		public Timetable[] newArray(int size) {
			return new Timetable[size];
		}
		
		@Override
		public Timetable createFromParcel(Parcel source) {
			return new Timetable(source.readInt(), source.readInt(), source.readString(),source.readString(),source.readString(),source.readString());
		}
	};
}