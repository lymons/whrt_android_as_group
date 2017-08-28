//package com.mieasy.whrt_app_android_4.act.boot;
//
//import java.util.List;
//
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.overlay.BusRouteOverlay;
//import com.amap.api.services.busline.BusStationItem;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.route.BusPath;
//import com.amap.api.services.route.BusRouteResult;
//import com.amap.api.services.route.BusStep;
//import com.amap.api.services.route.DriveRouteResult;
//import com.amap.api.services.route.RouteBusLineItem;
//import com.amap.api.services.route.RouteSearch;
//import com.amap.api.services.route.RouteSearch.BusRouteQuery;
//import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
//import com.amap.api.services.route.WalkRouteResult;
//import com.litesuits.orm.LiteOrm;
//import com.mieasy.whrt_app_android_4.util.ToastUtil;
//import com.mieasy.whrt_app_android_4.R;
//import com.mieasy.whrt_app_android_4.app.ContentApplication;
//import com.mieasy.whrt_app_android_4.entity.Map;
//import com.mieasy.whrt_app_android_4.entity.Stations;
//import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
//import com.mieasy.whrt_app_android_4.util.AMapUtil;
//import com.mieasy.whrt_app_android_4.util.NumUtil;
//import android.annotation.TargetApi;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
///**
// * 高德地图获取路线
// * @author Administrator
// *
// */
//public class PathAMapFragment extends Fragment implements OnRouteSearchListener{
//	private View view;
//	private LiteOrm liteOrm;
//
//	private Stations startStation,stopStation;
//	private MapView mapView;
//	private AMap aMap;
//	//浏览路线节点相关
//	private RouteSearch routeSearch;
//	private BusRouteResult busRouteResult;
//
//	private TextView mStartSpace,mStopSpace;
//	private static Bundle listBundle;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
//		view = inflater.inflate(R.layout.map_boot_path_amap, null);
//		mapView = (MapView) view.findViewById(R.id.amap_poi);
//		mapView.onCreate(savedInstanceState);// 必须要写
//		liteOrm = ContentApplication.getInstance().liteOrm;
//
//		getStartStopPoint();
//		initView();
//		initMapView();
//		return view;
//	}
//
//	private void initView() {
//		mStartSpace = (TextView) view.findViewById(R.id.start);
//		mStartSpace.setText(startStation.getStationName());
//		mStopSpace = (TextView) view.findViewById(R.id.end);
//		mStopSpace.setText(stopStation.getStationName());
//	}
//
//	//获取站点信息
//	private void getStartStopPoint() {
//		System.err.println(">>>>>>>>>>>>>"+AMapUtil.sHA1(view.getContext()));
//
//		Bundle bundle = getArguments();
//		if(bundle!=null){
//			startStation = bundle.getParcelable(NumUtil.STATIONS_START);
//			stopStation = bundle.getParcelable(NumUtil.STATIONS_STOP);
//		}
//	}
//
//	//获取地图控件引用
//	private void initMapView() {
//		List<Map> startMap = LiteOrmServices.getMapByStation(liteOrm, startStation);
//		List<Map> stopMap = LiteOrmServices.getMapByStation(liteOrm, stopStation);
//
//		aMap = mapView.getMap();
//		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//
//		LatLonPoint from = new LatLonPoint(
//				Double.parseDouble(startMap.get(0).getAmapLongitude()),
//				Double.parseDouble(startMap.get(0).getAmapLatitude()));   //起点  光谷广场
//		LatLonPoint to = new LatLonPoint(
//				Double.parseDouble(stopMap.get(0).getAmapLongitude()),
//				Double.parseDouble(stopMap.get(0).getAmapLatitude()));	   //终点  循礼门
//		RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(from, to);
//		routeSearch = new RouteSearch(view.getContext());
//		routeSearch.setRouteSearchListener(this);
//		// fromAndTo包含路径规划的起点和终点，RouteSearch.BusLeaseWalk表示公交查询模式
//		// 第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算,1表示计算
//		//第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
//		BusRouteQuery query = new BusRouteQuery(
//				fromAndTo,
////				RouteSearch.BusDefault,
//				RouteSearch.BusLeaseWalk,
//				"武汉",
//				0);
//		routeSearch.calculateBusRouteAsyn(query);//开始规划路径
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	public void onResume() {
//		super.onResume();
//		mapView.onResume();
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	public void onPause() {
//		super.onPause();
//		mapView.onPause();
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
////		super.onSaveInstanceState(outState);
//		mapView.onSaveInstanceState(outState);
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				mapView.onDestroy();
//				liteOrm.close();
//			}
//		}).start();
//	}
//
//	/**
//	 * 公交路线查询
//	 */
//	public void onBusRouteSearched(BusRouteResult result, int rCode) {
//		BusRouteResult busRouteResult = null;
//		BusPath busPath = null;
//		if (rCode == 0) {
//			if (result != null && result.getPaths() != null&& result.getPaths().size() > 0) {
//				busRouteResult = result;
//				busPath = showStationInfo(busRouteResult);
//				aMap.clear();// 清理地图上的所有覆盖物
//			} else {
//				ToastUtil.show(view.getContext(), R.string.no_result);
//			}
//		} else if (rCode == 27) {
//			ToastUtil.show(view.getContext(), R.string.error_network);
//		} else if (rCode == 32) {
//			ToastUtil.show(view.getContext(), R.string.error_key);
//		} else {
//			ToastUtil.show(view.getContext(), getString(R.string.error_other)+ rCode);
//		}
//		BusRouteOverlay routeOverlay = new BusRouteOverlay(view.getContext(), aMap,busPath, busRouteResult.getStartPos(),busRouteResult.getTargetPos());
//		routeOverlay.removeFromMap();
//		routeOverlay.addToMap();
//		routeOverlay.zoomToSpan();
//	}
//
//	/**
//	 * 取出方案
//	 * @param busRouteResult
//	 * @return
//	 */
//	public BusPath showStationInfo(BusRouteResult busRouteResult){
//		BusPath busPath = null;
//		String busLineype = "地铁路线";
//		boolean flag = false;
//		for(int y = 0;y < busRouteResult.getPaths().size();y++){
//			if(flag){
//				break;
//			}
//			busPath = busRouteResult.getPaths().get(y);
//			List<BusStep> busStepList = busPath.getSteps();
//			for (int j = 0; j < busStepList.size(); j++) {
//				List<RouteBusLineItem> routeBusLineItemList = busStepList.get(j).getBusLines();
//				if(routeBusLineItemList.size()>0){
//					String str = routeBusLineItemList.get(0).getBusLineType();
//					if(busLineype.length()==str.length()){
//						flag = true;
//						break;
//					}
//				}
//			}
//		}
//		return flag?busPath:(busRouteResult.getPaths().get(0));
//	}
//
//	@Override
//	public void onDriveRouteSearched(DriveRouteResult result, int item) {
//	}
//
//	@Override
//	public void onWalkRouteSearched(WalkRouteResult result, int item) {
//	}
//}