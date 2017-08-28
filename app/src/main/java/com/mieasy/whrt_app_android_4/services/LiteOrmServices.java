package com.mieasy.whrt_app_android_4.services;

import android.util.Log;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.mieasy.whrt_app_android_4.bean.ExitInfos;
import com.mieasy.whrt_app_android_4.bean.Point;
import com.mieasy.whrt_app_android_4.bean.SiteCollect;
import com.mieasy.whrt_app_android_4.entity.Directions;
import com.mieasy.whrt_app_android_4.entity.Lines;
import com.mieasy.whrt_app_android_4.entity.Map;
import com.mieasy.whrt_app_android_4.entity.Options;
import com.mieasy.whrt_app_android_4.entity.Path;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.entity.Timetable;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LiteOrmServices {
	/**
	 * 根据手指点击的坐标从数据库中找相应的站点坐标
	 *
	 * @param liteOrm
	 * @param pointTouch 点击图片上的坐标
	 * @return
	 */
	public static List<Map> getMapInfoByTouch(LiteOrm liteOrm, Point pointTouch) {
		List<Map> mapList = liteOrm.query(new QueryBuilder(Map.class)
				.where(Map.DEVICE_ID + "= ?", new String[]{"1"})
				.whereAppendAnd()
				.whereGreaterThan(Map.AMAP_X, pointTouch.getPointX() - NumUtil.MAP_POINT_EXCURSION)     //图片X坐标大于点击坐标
				.whereAppendAnd()
				.whereLessThan(Map.AMAP_X, pointTouch.getPointX() + NumUtil.MAP_POINT_EXCURSION)         //图片X坐标小于点击坐标
				.whereAppendAnd()
				.whereGreaterThan(Map.AMAP_Y, pointTouch.getPointY() - NumUtil.MAP_POINT_EXCURSION)     //图片Y坐标大于点击坐标
				.whereAppendAnd()
				.whereLessThan(Map.AMAP_Y, pointTouch.getPointY() + NumUtil.MAP_POINT_EXCURSION));     //图片Y坐标小于点击坐标
//		List<Map> mapList = liteOrm.query(Map.class);
		System.out.println(mapList.size());
		return mapList;
	}

	/**
	 * 根据map的stationID来获取指定站点的所有信息
	 *
	 * @param liteOrm
	 * @param map
	 * @return
	 */
	public static List<Stations> getStationByMap(LiteOrm liteOrm, Map map) {
		List<Stations> stationList = liteOrm.query(new QueryBuilder(Stations.class)
				.where(Map.STATION_ID + "= ?", new String[]{map.getStationId() + ""}));
		return stationList;
	}

	/**
	 * 根据map的stationID来获取指定站点的所有信息
	 *
	 * @param liteOrm
	 * @return
	 */
	public static List<Options> getStationByOptions(LiteOrm liteOrm) {
		List<Options> stationList = liteOrm.query(new QueryBuilder(Options.class));
		return stationList;
	}

	/**
	 * 根据stationID来获取指定站点的站名
	 *
	 * @param liteOrm
	 * @param stations
	 * @return
	 */
	public static List<Map> getMapByStation(LiteOrm liteOrm, Stations stations) {
		List<Map> mapList = liteOrm.query(new QueryBuilder(Map.class)
				.where(Map.STATION_ID + "= ?", new String[]{stations.getStationId() + ""}));
		Log.i("DB Info 2", liteOrm.getDataBaseConfig().toString());
		Log.i("Station Map", String.valueOf(stations.getStationId()));
		return mapList;
	}

	/**
	 * 根据线路的ID查询station站点列表
	 *
	 * @param liteOrm
	 * @return
	 */
	public static List<Stations> getStationByLineIDOrderSq(LiteOrm liteOrm, int item) {
		List<Stations> stationList = liteOrm.query(new QueryBuilder(Stations.class)
				.where(Stations.LINE_ID + "= ?", new String[]{item + ""})
				.orderBy(Stations.SEQUENCE));
		return stationList;
	}


	/**
	 * 返回每个线路对应的站点的Map对象
	 *
	 * @param liteOrm
	 * @return
	 */
	public static HashMap<String, String[]> getStationName(LiteOrm liteOrm) {
		HashMap<String, String[]> details = new HashMap<String, String[]>();
		HashMap<String, List<Stations>> map = new HashMap<String, List<Stations>>();
		List<Stations> stationList_0 = LiteOrmServices.getStationByLineIDOrderSq(liteOrm, 1);
		map.put(NumUtil.STATIONLINE_STR[0], stationList_0);
		List<Stations> stationList_1 = LiteOrmServices.getStationByLineIDOrderSq(liteOrm, 2);
		map.put(NumUtil.STATIONLINE_STR[1], stationList_1);
		List<Stations> stationList_2 = LiteOrmServices.getStationByLineIDOrderSq(liteOrm, 3);
		map.put(NumUtil.STATIONLINE_STR[2], stationList_2);
		List<Stations> stationList_3 = LiteOrmServices.getStationByLineIDOrderSq(liteOrm, 4);
		map.put(NumUtil.STATIONLINE_STR[3], stationList_3);
		List<Stations> stationList_4 = LiteOrmServices.getStationByLineIDOrderSq(liteOrm, 6);
		map.put(NumUtil.STATIONLINE_STR[4], stationList_4);

		for (int i = 0; i < map.size(); i++) {        //遍历map
			List<Stations> list = map.get(NumUtil.STATIONLINE_STR[i]);
			String[] str = new String[list.size()];
			for (int j = 0; j < list.size(); j++) {        //遍历List
				str[j] = list.get(j).getStationName();
			}
			details.put(NumUtil.STATIONLINE_STR[i], str);
		}
		return details;
	}

	/**
	 * 根据站点ID查询站点详细
	 *
	 * @param liteOrm
	 * @param item    //数
	 * @param line    //线路数
	 * @return
	 */
	public static Stations getStationById(LiteOrm liteOrm, int item, int line) {
		List<Stations> stationsList = new ArrayList<Stations>();
		List<Stations> stationList = liteOrm.query(new QueryBuilder(Stations.class)
				.where(Stations.STATION_ID + "= ?", new String[]{item + ""})
				.where(Stations.LINE_ID + "= ?", new String[]{line + ""})
				.orderBy(Stations.SEQUENCE));
		if (stationList.size() > item) {
			return stationList.get(item);
		} else {
			return null;
		}
	}

	/**
	 * 根据Station 返回路线方向信息
	 *
	 * @param stations
	 * @return
	 */
	public static List<Directions> getDirectionsByStations(LiteOrm liteOrm, Stations stations) {
		return liteOrm.query(new QueryBuilder(Directions.class).where(Directions.LINE_ID + "= ?", new String[]{stations.getLineId() + ""}));
	}

	/**
	 * 根据station ID查找地铁时刻表
	 *
	 * @param liteOrm
	 * @param id
	 * @return
	 */
	public static List<Timetable> getTimetableByStationID(LiteOrm liteOrm, int id) {
		return liteOrm.query(new QueryBuilder(Timetable.class).where(Timetable.STATION_ID + "= ?", new String[]{id + ""}));
	}

	/**
	 * 查询所有叫stationName的站
	 *
	 * @param liteOrm
	 * @param stationName
	 * @return
	 */
	public static List<Stations> getStationsById(LiteOrm liteOrm, String stationName) {
		return liteOrm.query(new QueryBuilder(Stations.class).where(Stations.STATION_NAME + "= ?", new String[]{stationName}));
	}

	/**
	 * 根据起点终点获取路线信息
	 *
	 * @param liteOrm
	 * @param stationStart 起点的stations对象
	 * @param stationStop  终点的stations对象
	 * @return
	 */
	public static Path getPathInfoByFromAndTo(LiteOrm liteOrm, Stations stationStart, Stations stationStop) {
		Path path = new Path();
		List<Path> pathList = liteOrm.query(new QueryBuilder(Path.class)
				.where(Path.START_NUM + " = ?", new String[]{stationStart.getStationNum() + ""})
				.whereAnd(Path.END_NUM + " = ?", new String[]{stationStop.getStationNum() + ""}));
		if (pathList.size() > 0) {
			path = pathList.get(0);
		}
		return path;
	}

	/**
	 * 根据ID获取站点名称
	 *
	 * @param liteOrm
	 * @param stationfrom 站点的名称
	 * @return
	 */
	public static Stations getStationsNameByStationsId(LiteOrm liteOrm, String stationfrom) {
		Stations stations = liteOrm.queryById(stationfrom, Stations.class);
		return stations;
	}

	/**
	 * 根据方向表ID查询出Lines表数据
	 *
	 * @param liteOrm
	 * @param dirId
	 * @return
	 */
	public static Lines getLinesInfo(LiteOrm liteOrm, int dirId) {
		Lines lines = new Lines();
		List<Lines> linesList = new ArrayList<Lines>();
		List<Directions> directionsList = new ArrayList<Directions>();
		directionsList = liteOrm.query(new QueryBuilder(Directions.class)
				.where(Directions.DIRECTION_ID + " = ?", new String[]{dirId + ""}));
		if (directionsList.size() != 0) {
			linesList = liteOrm.query(new QueryBuilder(Lines.class)
					.where(Lines.LINE_ID + " = ?", new String[]{directionsList.get(0).getLineId() + ""}));
			if (linesList.size() != 0) {
				lines = linesList.get(0);
			}
		}
		return lines;
	}

	/**
	 * 根据站点ID获取站点图片上的坐标
	 *
	 * @param liteOrm
	 * @param details 线路详细
	 * @return
	 */
	public static List<Point> getListPoint(LiteOrm liteOrm, int[] details) {
		List<Point> pointList = new ArrayList<Point>();
		Point point;
		for (int de : details) {
			List<Map> mapList = liteOrm.query(new QueryBuilder(Map.class).where(Map.STATION_ID + " = ?", new String[]{de + ""}));
			if (mapList.size() != 0) {
				point = new Point(mapList.get(0).getAmapX(), mapList.get(0).getAmapY());
				pointList.add(point);
			}
		}
		return pointList;
	}

	/**
	 * 根据站点ID获取站点信息
	 *
	 * @param liteOrm
	 * @param details 线路详细
	 * @return
	 */
	public static List<Stations> getStationsDetails(LiteOrm liteOrm, int[] details) {
		List<Stations> stationDetailsList = new ArrayList<Stations>();
		for (int de : details) {
			List<Stations> stationsList = liteOrm.query(new QueryBuilder(Stations.class).where(Stations.STATION_ID + " = ?", new String[]{de + ""}));
			if (stationsList.size() != 0) {
				stationDetailsList.add(stationsList.get(0));
			}
		}
		return stationDetailsList;
	}

	/**
	 * 查询方向表Directions所有数据
	 *
	 * @param liteOrm
	 * @return
	 */
	public static List<Directions> getAllDirections(LiteOrm liteOrm) {
		List<Directions> directionsList = liteOrm.query(new QueryBuilder(Directions.class));
		return directionsList;
	}

	/**
	 * 查询线路表Lines所有数据
	 *
	 * @param liteOrm
	 * @return
	 */
	public static List<Lines> getAllLines(LiteOrm liteOrm) {
		List<Lines> linesList = liteOrm.query(new QueryBuilder(Lines.class));
		return linesList;
	}

	/**
	 * 查询所有站点名
	 */
	public static ArrayList<String> getSelectSiteName(LiteOrm liteOrm) {
		List<Stations> stations = liteOrm.query(new QueryBuilder(Stations.class));
		ArrayList<String> list_stationname = new ArrayList<String>();
		for (Stations data : stations) {
			String stationName = data.getStationName();
			list_stationname.add(stationName);
		}
		for (int i = 0; i < list_stationname.size(); i++) {
			for (int j = 0; j < list_stationname.size(); j++) {
				if (i == j)
					continue;
				if (list_stationname.get(i).equals(list_stationname.get(j))) {
					list_stationname.remove(j);
				}

			}
		}
		return list_stationname;
	}

	/**
	 * 根据站点名查询站点列表ID
	 */
	public static int getSiteIDBySiteName(LiteOrm liteOrm, String name) {
		ArrayList<Stations> stations = liteOrm.query(new QueryBuilder(Stations.class).where(Stations.STATION_NAME + "= ?", new String[]{name + ""}));
		return stations.get(0).getStationId();
	}

	/**
	 * 根据站点ID查询map表
	 */
	public static Map getMapBySiteID(LiteOrm liteOrm, int siteId) {
		ArrayList<Map> map = liteOrm.query(new QueryBuilder(Map.class).where(Map.STATION_ID + "= ?", new String[]{siteId + ""}));
		Log.e("map","maps"+map);
		return map.get(0);
	}

	/**
	 * 根据STATION_ID查询station站点列表
	 *
	 * @param liteOrm
	 * @param item
	 * @return
	 */
	public static Stations getStationByStationID(LiteOrm liteOrm, int item) {
		List<Stations> stationList = liteOrm.query(new QueryBuilder(Stations.class)
				.where(Stations.STATION_ID + "= ?", new String[]{item + ""}));
		return stationList.get(0);
	}

	/**
	 * 查询MAP表所有数据
	 *
	 * @param liteOrm
	 * @return
	 */
	public static List<Map> getAllMapInfo(LiteOrm liteOrm) {
		List<Map> maps = liteOrm.query(new QueryBuilder(Map.class));
		return maps;
	}

	public static SiteCollect getSiteCollectBySiteCollectId(LiteOrm liteOrm_add, String id) {
		ArrayList<SiteCollect> query = liteOrm_add.query(new QueryBuilder(SiteCollect.class).where(SiteCollect.ID + "= ?", new String[]{id + ""}));
		return query.get(0);
	}

	public static SiteCollect getSiteName(LiteOrm liteOrm_add, String stationName) {
		ArrayList<SiteCollect> query = liteOrm_add.query(new QueryBuilder(SiteCollect.class).where(SiteCollect.STATIONNAME + "= ?", new String[]{stationName}));
		if (!query.isEmpty()) {
			return query.get(0);
		} else {
			return null;
		}
	}

	public static SiteCollect getstatioto(LiteOrm liteOrm_add, String stationto) {
		ArrayList<SiteCollect> query = liteOrm_add.query(new QueryBuilder(SiteCollect.class).where(SiteCollect.STATIONTO + "= ?", new String[]{stationto}));
		if (!query.isEmpty()) {
			return query.get(0);
		} else {
			return null;
		}
	}

	public static List<Stations> getStation(LiteOrm liteOrm) {
		List<Stations> query = liteOrm.query(new QueryBuilder(Stations.class));
		return query;
	}

	public static List<SiteCollect> getSiteCollectInfo(LiteOrm liteOrm_add) {
		List<SiteCollect> stations = liteOrm_add.query(new QueryBuilder(SiteCollect.class));
		return stations;
	}


	/**
	 * 通过stationNum查询出口详细信息
	 *
	 * @param liteOrm
	 * @param stationID
	 */
	public static List<ExitInfos> getExitInfoByStationID(LiteOrm liteOrm, int stationID) {
		List<ExitInfos> exitInfos = liteOrm.query(new QueryBuilder(ExitInfos.class).where(ExitInfos.STATION_ID + "= ?", new String[]{stationID + ""}));
		Log.e("exitInfos", "exitInfos.size():" + exitInfos.size());
		for (int i = 0; i < exitInfos.size(); i++) {
			Log.e("exitInfos", "exitInfos.get(i).getExitCode():" + exitInfos.get(i).getExitCode());
			Log.e("exitInfos", "exitInfos.get(i).getInfoDetialName():" + exitInfos.get(i).getInfoDetialName());
		}
		return exitInfos;
	}

	public static Integer getStationLineID(LiteOrm liteOrm, String name) {
		ArrayList<Stations> stations = liteOrm.query(new QueryBuilder(Stations.class).where(Stations.STATION_NAME + "= ?", new String[]{name + ""}));
        return stations.get(0).getLineId();
	}
}