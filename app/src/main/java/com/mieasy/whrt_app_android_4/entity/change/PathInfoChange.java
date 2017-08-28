package com.mieasy.whrt_app_android_4.entity.change;

import com.mieasy.whrt_app_android_4.entity.Stations;

import java.util.List;

public class PathInfoChange {
	private Stations startStation;				//起点
	private Stations stopStation;				//终点
	private String prices;						//票价
	private int change_count;					//换乘次数
	private int total_stations;					//总站数-1
	private String type;						//路线类型
	private List<DetailsChange> detailsChange;	//路线详细
	private String distance;					//里程
	private int time;					//时间

	public PathInfoChange(Stations startStation, Stations stopStation, String prices, int change_count,
			int total_stations, String type, String distance,int time,List<DetailsChange> detailsChange) {
		super();
		this.startStation = startStation;
		this.stopStation = stopStation;
		this.prices = prices;
		this.change_count = change_count;
		this.total_stations = total_stations;
		this.type = type;
		this.distance = distance;
		this.time = time;
		this.detailsChange = detailsChange;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public PathInfoChange() {
		super();
	}

	public Stations getStartStation() {
		return startStation;
	}

	public void setStartStation(Stations startStation) {
		this.startStation = startStation;
	}

	public Stations getStopStation() {
		return stopStation;
	}

	public void setStopStation(Stations stopStation) {
		this.stopStation = stopStation;
	}

	public String getPrices() {
		return prices;
	}

	public void setPrices(String prices) {
		this.prices = prices;
	}

	public int getChange_count() {
		return change_count;
	}

	public void setChange_count(int change_count) {
		this.change_count = change_count;
	}

	public int getTotal_stations() {
		return total_stations;
	}

	public void setTotal_stations(int total_stations) {
		this.total_stations = total_stations;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<DetailsChange> getDetailsChange() {
		return detailsChange;
	}

	public void setDetailsChange(List<DetailsChange> detailsChange) {
		this.detailsChange = detailsChange;
	}

	@Override
	public String toString() {
		return "PathInfoChange{" +
				"startStation=" + startStation +
				", stopStation=" + stopStation +
				", prices='" + prices + '\'' +
				", change_count=" + change_count +
				", total_stations=" + total_stations +
				", type='" + type + '\'' +
				", detailsChange=" + detailsChange +
				", distance='" + distance + '\'' +
				", time=" + time +
				'}';
	}
}
