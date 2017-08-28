package com.mieasy.whrt_app_android_4.bean;

import java.util.Arrays;

/**
 * 路线PathInfo下的Details对象
 * @author Administrator
 */
public class Details {
	private  int direction_id;
	private  int[] stations;
	
	public int getDirection_id() {
		return direction_id;
	}
	public void setDirection_id(int direction_id) {
		this.direction_id = direction_id;
	}
	public int[] getStations() {
		return stations;
	}
	public void setStations(int[] stations) {
		this.stations = stations;
	}
	public Details(int direction_id, int[] stations) {
		super();
		this.direction_id = direction_id;
		this.stations = stations;
	}
	public Details() {
		super();
	}

	@Override
	public String toString() {
		return "Details{" +
				"direction_id=" + direction_id +
				", stations=" + Arrays.toString(stations) +
				'}';
	}
}
