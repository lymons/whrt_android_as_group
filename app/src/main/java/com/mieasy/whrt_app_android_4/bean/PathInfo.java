package com.mieasy.whrt_app_android_4.bean;

import java.util.List;

/**
 * 路线json
 *
 * @author Administrator
 */
public class PathInfo {
    private List<Details> details;
    private int change_count;
    private int total_stations;

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

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public PathInfo(List<Details> details, int change_count, int total_stations) {
        this.details = details;
        this.change_count = change_count;
        this.total_stations = total_stations;
    }

    public PathInfo() {
    }

    @Override
    public String toString() {
        return "PathInfo{" +
                "details=" + details +
                ", change_count=" + change_count +
                ", total_stations=" + total_stations +
                '}';
    }
}
