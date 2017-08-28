package com.mieasy.whrt_app_android_4.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Map entity. 地图信息表
 */
@Table("MAP")
public class Map implements Parcelable {
	@Ignore
	public static String MAP_ID = "MAP_ID";
	@Ignore
	public static String STATION_ID = "STATION_ID";
	@Ignore
	public static String DEVICE_ID = "DEVICE_ID";
	@Ignore
	public static String BMAP_X = "BMAP_X";
	@Ignore
	public static String BMAP_Y = "BMAP_Y";
	@Ignore
	public static String BMAP_LONGITUDE = "BMAP_LONGITUDE";
	@Ignore
	public static String BMAP_PLATITUDE = "BMAP_LATITUDE";
	@Ignore
	public static String AMAP_X = "AMAP_X";
	@Ignore
	public static String AMAP_Y = "AMAP_Y";
	@Ignore
	public static String AMAP_LONGITUDE = "AMAP_LONGITUDE";
	@Ignore
	public static String AMAP_PLATITUDE = "AMAP_LATITUDE";
	
	@Column(value = "MAP_ID")
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	private Integer mapId;				//表ID
	
	@Column(value = "STATION_ID")
	private Integer stationId;			//站点ID
	
	@Column(value = "DEVICE_ID")
	private Integer deviceId;			//设备ID
	
	@Column(value = "BMAP_X")
	private Integer bmapX;				//图片上站点的X坐标
	
	@Column(value = "BMAP_Y")
	private Integer bmapY;				//图片上站点的Y坐标
	
	@Column(value = "BMAP_LONGITUDE")
	private String bmapLongitude;		//百度地图    站点X坐标
	
	@Column(value = "BMAP_LATITUDE")
	private String bmapLatitude;		//百度地图    站点Y坐标
	
	@Column(value = "AMAP_X")
	private Double amapX;				//加3号线      图片上X坐标
	
	@Column(value = "AMAP_Y")
	private Double amapY;				//加3号线      图片上Y坐标
	
	@Column(value = "AMAP_LONGITUDE")
	private Double amapLongitude;		//高德地图    站点X坐标
	
	@Column(value = "AMAP_LANGITUDE")
	private Double amapLatitude;		//高德地图    站点Y坐标

	/** full constructor */
	public Map(Integer mapId, Integer stationId, Integer deviceId,
			Integer bmapX, Integer bmapY, String bmapLongitude,
			String bmapLatitude, Double amapX, Double amapY,
			   Double amapLongitude, Double amapLatitude) {
		super();
		this.mapId = mapId;
		this.stationId = stationId;
		this.deviceId = deviceId;
		this.bmapX = bmapX;
		this.bmapY = bmapY;
		this.bmapLongitude = bmapLongitude;
		this.bmapLatitude = bmapLatitude;
		this.amapX = amapX;
		this.amapY = amapY;
		this.amapLongitude = amapLongitude;
		this.amapLatitude = amapLatitude;
	}

	// Property accessors

	public Integer getMapId() {
		return this.mapId;
	}

	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}

	public Integer getStationId() {
		return this.stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Integer getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getBmapLongitude() {
		return this.bmapLongitude;
	}

	public void setBmapLongitude(String bmapLongitude) {
		this.bmapLongitude = bmapLongitude;
	}

	public String getBmapLatitude() {
		return this.bmapLatitude;
	}

	public void setBmapLatitude(String bmapLatitude) {
		this.bmapLatitude = bmapLatitude;
	}


	public Integer getBmapX() {
		return bmapX;
	}

	public void setBmapX(Integer bmapX) {
		this.bmapX = bmapX;
	}

	public Integer getBmapY() {
		return bmapY;
	}

	public void setBmapY(Integer bmapY) {
		this.bmapY = bmapY;
	}

	public Double getAmapX() {
		return amapX;
	}

	public void setAmapX(Double amapX) {
		this.amapX = amapX;
	}

	public Double getAmapY() {
		return amapY;
	}

	public void setAmapY(Double amapY) {
		this.amapY = amapY;
	}

	public Double getAmapLongitude() {
		Log.e("longs","amapLongitude"+amapLongitude);
		return this.amapLongitude;
	}

	public void setAmapLongitude(Double amapLongitude) {
		this.amapLongitude = amapLongitude;
	}

	public Double getAmapLatitude() {
		Log.e("longs","amapLatitude"+amapLatitude);
		return this.amapLatitude;

	}

	public void setAmapLatitude(Double amapLatitude) {
		this.amapLatitude = amapLatitude;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mapId);
		dest.writeInt(stationId);
		dest.writeInt(deviceId);
		dest.writeInt(bmapX);
		dest.writeInt(bmapY);
		dest.writeString(bmapLongitude);
		dest.writeString(bmapLatitude);
		dest.writeDouble(amapX);
		dest.writeDouble(amapY);
		dest.writeDouble(amapLongitude);
		dest.writeDouble(amapLatitude);

	}

	public static final Parcelable.Creator<Map> CREATOR = new Creator<Map>() {
		
		@Override
		public Map[] newArray(int size) {
			return new Map[size];
		}
		
		@Override
		public Map createFromParcel(Parcel source) {
			Log.e("longs","amapLongitude"+source.readDouble());
			return new Map(source.readInt(),source.readInt(),source.readInt(),source.readInt(),source.readInt(),
					source.readString(), source.readString(), source.readDouble(), source.readDouble(),  source.readDouble(),  source.readDouble());
		}
	};
}