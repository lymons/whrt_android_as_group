package com.mieasy.whrt_app_android_4.services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.bean.Colors;
import com.mieasy.whrt_app_android_4.bean.Details;
import com.mieasy.whrt_app_android_4.bean.PathInfo;
import com.mieasy.whrt_app_android_4.entity.Directions;
import com.mieasy.whrt_app_android_4.entity.Lines;
import com.mieasy.whrt_app_android_4.entity.Path;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.entity.change.DetailsChange;
import com.mieasy.whrt_app_android_4.entity.change.PathInfoChange;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 线路信息转化
 * @author Administrator
 *
 */
public class PathInfoChangeManager {
    private static final String TAG =PathInfoChangeManager.class.getSimpleName();
    private static Gson gson  = new Gson();

	/**
	 * 根据起点终点站点信息获取转化后的数据
	 * @param liteOrm
	 * @param startStation
	 * @param stopStation
	 * @return
	 */
	public static List<PathInfoChange> getAllPathInfo(LiteOrm liteOrm,Stations startStation,Stations stopStation){
        List<PathInfoChange> pathInfoChangeList = new ArrayList<PathInfoChange>();
        PathInfoChange pathInfoChang = new PathInfoChange();
		Path path = LiteOrmServices.getPathInfoByFromAndTo(liteOrm, startStation, stopStation);//获取所有线路以及价格
		try{
            Log.e(TAG,"path.getJsonData():"+path.getJsonData());
            PathInfo pathInfo = new Gson().fromJson(path.getJsonData(), new TypeToken<PathInfo>() {
            }.getType());

            Log.e(TAG,"pathInfo:"+pathInfo.toString());
            pathInfoChang.setStartStation(startStation);
            pathInfoChang.setStopStation(stopStation);
            pathInfoChang.setPrices(path.getPrice());
            pathInfoChang.setDistance(path.getDistance());
            pathInfoChang.setTime(path.getTime());
            pathInfoChang.setChange_count(pathInfo.getChange_count());
            pathInfoChang.setTotal_stations(pathInfo.getTotal_stations());

            List<DetailsChange> detailsChangeList = new ArrayList<DetailsChange>();
            List<Details> detailsChangelist = pathInfo.getDetails();
            for(int j=0;j<detailsChangelist.size();j++){
                DetailsChange detailsChange = new DetailsChange();
                detailsChange.setDirection_id(detailsChangelist.get(j).getDirection_id());
                detailsChange.setPointList(LiteOrmServices.getListPoint(liteOrm, detailsChangelist.get(j).getStations()));
                detailsChange.setStationsList(LiteOrmServices.getStationsDetails(liteOrm, detailsChangelist.get(j).getStations()));
                detailsChangeList.add(detailsChange);
            }
            pathInfoChang.setDetailsChange(detailsChangeList);
            pathInfoChangeList.add(pathInfoChang);

		}catch(Exception e){
			e.printStackTrace();
		}
		return pathInfoChangeList;
	}
	
	/**
	 * 传递颜色值
	 * @param liteOrm
	 */
	public static void setColorMap(LiteOrm liteOrm){
		HashMap<String,Colors> hashMap = new HashMap<String,Colors>();
		List<Directions> directionsList = LiteOrmServices.getAllDirections(liteOrm);
		Colors colors;
		for(Directions directions:directionsList){
			Lines lines = LiteOrmServices.getLinesInfo(liteOrm, directions.getDirectionId());
			colors = new Colors(lines.getLineRed(),lines.getLineGreen(),lines.getLineBlue(),0);
			hashMap.put(directions.getDirectionId()+"", colors);
		}
		NumUtil.colorsList = hashMap;
	}
	
	/**
	 * 传递线路信息
	 * @param liteOrm
	 */
	public static void setLinesMap(LiteOrm liteOrm){
		HashMap<String,String> hashMap = new HashMap<String,String>();
		List<Lines> linesList = LiteOrmServices.getAllLines(liteOrm);
		for(Lines lines:linesList){
			hashMap.put(lines.getLineId()+"", lines.getLineName());
		}
		NumUtil.linesMap = hashMap;
	}
	
	/**
	 * 传递方向信息
	 * @param liteOrm
	 */
	public static void setDirectionsMap(LiteOrm liteOrm){
		HashMap<String,String> hashMap = new HashMap<String,String>();
		List<Directions> directionsList = LiteOrmServices.getAllDirections(liteOrm);
		for(Directions directions:directionsList){
			hashMap.put(directions.getDirectionId()+"", directions.getDirectionName());
		}
		NumUtil.directionsMap = hashMap;
	}
}