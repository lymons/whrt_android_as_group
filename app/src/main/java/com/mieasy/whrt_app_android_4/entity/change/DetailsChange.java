package com.mieasy.whrt_app_android_4.entity.change;

import java.util.List;

import com.mieasy.whrt_app_android_4.bean.Point;
import com.mieasy.whrt_app_android_4.entity.Stations;

public class DetailsChange {
	private  int direction_id;				//路线方向
	private List<Point> pointList;			//点坐标列表
	private List<Stations> stationsList;	//站点信息列表

	public int getDirection_id() {
		return direction_id;
	}
	public void setDirection_id(int direction_id) {
		this.direction_id = direction_id;
	}
	public List<Point> getPointList() {
		return pointList;
	}
	public void setPointList(List<Point> pointList) {
		this.pointList = pointList;
	}
	public List<Stations> getStationsList() {
		return stationsList;
	}
	public void setStationsList(List<Stations> stationsList) {
		this.stationsList = stationsList;
	}
	public DetailsChange(int direction_id, List<Point> pointList, List<Stations> stationsList) {
		super();
		this.direction_id = direction_id;
		this.pointList = pointList;
		this.stationsList = stationsList;
	}
	public DetailsChange() {
		super();
	}
}
