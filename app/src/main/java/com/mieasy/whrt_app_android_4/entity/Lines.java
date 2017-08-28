package com.mieasy.whrt_app_android_4.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Lines entity. 线路表
 */
@Table("Lines")
public class Lines implements Parcelable {
	@Ignore
	public static String LINE_ID="LINE_ID";
	@Ignore
	public static String LINE_NAME="LINE_NAME";
	@Ignore
	public static String LINE_ENGLISHNAME="LINE_ENGLISHNAME";
	@Ignore
	public static String LINE_RED="LINE_RED";
	@Ignore
	public static String LINE_GREEN="LINE_GREEN";
	@Ignore
	public static String LINE_BLUE="LINE_BLUE";
	@Ignore
	public static String LINE_COLOR="LINE_COLOR";

	@Column(value = "LINE_ID")
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	private Integer lineId;				//线路表ID

	@Column(value = "LINE_NAME")
	private String lineName;			//线路名称		1,2,4

	@Column(value = "LINE_ENGLISHNAME")
	private String lineEnglishname;		//线路英文名称	line1，line2，line4

	@Column(value = "LINE_RED")
	private Integer lineRed;

	@Column(value = "LINE_GREEN")
	private Integer lineGreen;

	@Column(value = "LINE_BLUE")
	private Integer lineBlue;

	@Column(value = "LINE_COLOR")
	private String lineColor;

	public Lines() {
		super();
	}

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}

	public Lines(Integer lineId, String lineName, String lineEnglishname, Integer lineRed, Integer lineGreen,
			Integer lineBlue,String lineColor) {
		super();
		this.lineId = lineId;
		this.lineName = lineName;
		this.lineEnglishname = lineEnglishname;
		this.lineRed = lineRed;
		this.lineGreen = lineGreen;
		this.lineBlue = lineBlue;
		this.lineColor = lineColor;
	}

	public Integer getLineRed() {
		return lineRed;
	}

	public void setLineRed(Integer lineRed) {
		this.lineRed = lineRed;
	}

	public Integer getLineGreen() {
		return lineGreen;
	}

	public void setLineGreen(Integer lineGreen) {
		this.lineGreen = lineGreen;
	}

	public Integer getLineBlue() {
		return lineBlue;
	}

	public void setLineBlue(Integer lineBlue) {
		this.lineBlue = lineBlue;
	}

	public Integer getLineId() {
		return this.lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return this.lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLineEnglishname() {
		return this.lineEnglishname;
	}

	public void setLineEnglishname(String lineEnglishname) {
		this.lineEnglishname = lineEnglishname;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(lineId);
		dest.writeString(lineName);
		dest.writeString(lineEnglishname);
		dest.writeInt(lineRed);
		dest.writeInt(lineGreen);
		dest.writeInt(lineBlue);
		dest.writeString(lineColor);
	}

	public static final Parcelable.Creator<Lines> CREATOR = new Creator<Lines>() {

		@Override
		public Lines[] newArray(int size) {
			return new Lines[size];
		}

		@Override
		public Lines createFromParcel(Parcel source) {
			return new Lines(source.readInt(), 
					source.readString(),
					source.readString(),
					source.readInt(),
					source.readInt(),
					source.readInt(),
					source.readString());
		}
	};
}