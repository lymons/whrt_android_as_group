//package com.mieasy.whrt_app_android_4.act.boot;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.overlay.BusRouteOverlay;
//import com.amap.api.services.busline.BusLineItem;
//import com.amap.api.services.busline.BusStationItem;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.route.BusPath;
//import com.amap.api.services.route.BusRouteResult;
//import com.amap.api.services.route.BusStep;
//import com.amap.api.services.route.DriveRouteResult;
//import com.amap.api.services.route.RouteBusLineItem;
//import com.amap.api.services.route.RouteSearch;
//import com.amap.api.services.route.WalkRouteResult;
//import com.amap.api.services.route.RouteSearch.BusRouteQuery;
//import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
//import com.litesuits.orm.LiteOrm;
//import com.mieasy.whrt_app_android_4.R;
//import com.mieasy.whrt_app_android_4.app.ContentApplication;
//import com.mieasy.whrt_app_android_4.entity.Map;
//import com.mieasy.whrt_app_android_4.entity.Stations;
//import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
//import com.mieasy.whrt_app_android_4.util.NumUtil;
//import com.mieasy.whrt_app_android_4.util.ToastUtil;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * 查看高德地图  路线的信息引导
// * @author Administrator
// *
// */
//public class PathAMapLineFragment extends Fragment implements OnRouteSearchListener{
//	private TextView mTime,mStationNum,mHuan,mPrice;
//	private LinearLayout linHuan1,linHuan2;
//	private TextView mRoadStart,mRoadHuan1,mRoadHuan2,mRoadStop;
//	private ImageView imgView1,imgView2;
//
//	private View view;
//	private LiteOrm liteOrm;
//	private Stations startStation,stopStation;
//	//浏览路线节点相关
//	private RouteSearch routeSearch;
//	private BusRouteResult busRouteResult;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		view = inflater.inflate(R.layout.map_boot_pathline, null);
//		liteOrm = ContentApplication.getInstance().liteOrm;
//		getStartStopPoint();
//		initMapView();
//		return view;
//	}
//
//	private void initView() {
//		//Top显示
//		mTime = (TextView) view.findViewById(R.id.tv_road_time);				//花了多长时间
//		mStationNum = (TextView) view.findViewById(R.id.tv_road_stationNum);	//经过了多少站
//		mHuan = (TextView) view.findViewById(R.id.tv_road_huancheng_num);		//经过多少次换乘
//		mPrice = (TextView) view.findViewById(R.id.tv_road_price);				//花费多少钱
//
//		linHuan1 = (LinearLayout) view.findViewById(R.id.lin_huancheng1);		//换乘1整个布局
//		imgView1 = (ImageView) view.findViewById(R.id.img_lianxian1);			//换乘1中的图片
//		linHuan2 = (LinearLayout) view.findViewById(R.id.lin_huancheng2);		//换乘2整个布局
//		imgView2 = (ImageView) view.findViewById(R.id.img_lianxian2);			//换乘2中的图片
//
//		mRoadStart = (TextView) view.findViewById(R.id.tv_road_start_top);		//起点
//		mRoadStop = (TextView) view.findViewById(R.id.tv_road_stop_bottom);		//终点
//		mRoadHuan1 = (TextView) view.findViewById(R.id.tv_road_huancheng1);		//换乘站点1
//		mRoadHuan2 = (TextView) view.findViewById(R.id.tv_road_huancheng2);		//换乘站点2
//
//
//	}
//
//	//获取地图控件引用
//	private void initMapView() {
//		List<Map> startMap = LiteOrmServices.getMapByStation(liteOrm, startStation);
//		List<Map> stopMap = LiteOrmServices.getMapByStation(liteOrm, stopStation);
//
//		LatLonPoint from = new LatLonPoint(
//				Double.parseDouble(startMap.get(0).getAmapLongitude()),
//				Double.parseDouble(startMap.get(0).getAmapLatitude()));   //起点  光谷广场
//		LatLonPoint to = new LatLonPoint(
//				Double.parseDouble(stopMap.get(0).getAmapLongitude()),
//				Double.parseDouble(stopMap.get(0).getAmapLatitude()));	   //终点  循礼门
//		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(from, to);
//
//		routeSearch = new RouteSearch(view.getContext());
//		routeSearch.setRouteSearchListener(this);
//		// fromAndTo包含路径规划的起点和终点，RouteSearch.BusLeaseWalk表示公交查询模式
//		// 第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算,1表示计算
//		//第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
//		BusRouteQuery query = new BusRouteQuery(fromAndTo, RouteSearch.BusLeaseWalk, "武汉",0);
//		routeSearch.calculateBusRouteAsyn(query);//开始规划路径
//	}
//
//	//获取站点信息
//	private void getStartStopPoint() {
//		Bundle bundle = getArguments();
//		if(bundle!=null){
//			startStation = bundle.getParcelable(NumUtil.STATIONS_START);
//			stopStation = bundle.getParcelable(NumUtil.STATIONS_STOP);
//		}
//	}
//
//	/**
//	 * 公交路线查询
//	 */
//	@Override
//	public void onBusRouteSearched(BusRouteResult result, int rCode) {
//		if (rCode == 0) {
//			if (result != null && result.getPaths() != null&& result.getPaths().size() > 0) {
//				showStationInfo(result);
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
//	}
//
//	/**
//	 * 取出方案
//	 * @param busRouteResult
//	 * @return
//	 */
//	public void showStationInfo(BusRouteResult busRouteResult){
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
//		if(flag){
//			getLineStationInfo(busPath.getSteps());
//		}else{
//			getLineStationInfo(busRouteResult.getPaths().get(0).getSteps());
//		}
//	}
//
//	/**
//	 * 根据线路获取方案
//	 * @param busStepList
//	 */
//	public void getLineStationInfo(List<BusStep> busStepList){
//		List<Float> times = new ArrayList<Float>();
//		HashMap<String,String> mapStationName = new HashMap<String,String>();				//取换乘站点名称
//		HashMap<String,String> mapStationFor = new HashMap<String,String>();				//存取从什么站到什么站点
//		HashMap<String,List<String>> mapStationAll = new HashMap<String,List<String>>();	//存取中间经过什么站点
//
//		for (int j = 0; j < busStepList.size(); j++) {
//			List<RouteBusLineItem> routeBusLineItemList = busStepList.get(j).getBusLines();
//			if(routeBusLineItemList.size()>0){
//				for(int k = 0;k<routeBusLineItemList.size();k++){
//					List<BusStationItem> busStationItemList = routeBusLineItemList.get(k).getPassStations();
//					mapStationFor.put(j+"", routeBusLineItemList.get(k).getBusLineName());
//					mapStationName.put(j+"", routeBusLineItemList.get(k).getArrivalBusStation().getBusStationName());
//					times.add(routeBusLineItemList.get(k).getDuration());
//					List<String> strList = new ArrayList<String>();
//					for(int i = 0;i<busStationItemList.size();i++){
//						strList.add(busStationItemList.get(i).getBusStationName());
//					}
//					mapStationAll.put(j+"", strList);
//				}
//			}
//		}
//		initStationInfo(mapStationFor,mapStationAll,mapStationName,times);
//	}
//
//	private void initStationInfo(
//			HashMap<String, String> mapStationFor, 	//从某站到某站
//			HashMap<String, List<String>> mapStationAll,	//经过的所有站点名称
//			HashMap<String,String> mapStationName,		//经过的中转站
//			List<Float> times) {					//从某站到某站所花的时间
//		initView();
//		//		int priceTxt = LiteOrmServices.getPriceByFromTo(ContentApplication.getInstance().liteOrm,startStation,stopStation).getPrice();
//		HashMap<String, String> StationFor=mapStationFor;
//		HashMap<String, List<String>> StationAll = mapStationAll;
//		//{2=轨道交通4号线(黄金口--武汉火车站), 1=轨道交通2号线(金银潭--光谷广场), 0=轨道交通1号线(汉口北--东吴大道)}
//		//{2=[楚河汉街], 1=[江汉路, 积玉桥, 螃蟹岬, 小龟山], 0=[大智路]}
//		//{2=青鱼嘴, 1=洪山广场, 0=循礼门}
//
//		float f = 0.0f;			//总时间
//		int statNum = 0;		//多少站
//		int statDir = 0;		//多少次换乘
//		String str1="",str2="",str3="";
//
//		for(int x=0;x<times.size();x++){
//			//[476.0, 1032.0, 680.0]
//			f = f+times.get(x);
//		}
//		if((StationFor.size()!=0)&&(StationAll.size()!=0)&&(mapStationName.size()!=0)){
//			if((StationFor.size()==1)&&(StationAll.size()==1)&&(mapStationName.size()==1)){
//				statNum = StationAll.get("0").size()+1;
//				str1 = "乘坐 武汉"+StationFor.get("0")+"方向\n经过"+statNum+"站,到达"+mapStationName.get("0");
//			}
//			if((StationFor.size()==2)&&(StationAll.size()==2)&&(mapStationName.size()==2)){
//				statDir = 1;
//				statNum = StationAll.get("0").size()+1+ StationAll.get("1").size()+1;
//				str1 = "乘坐 武汉"+StationFor.get("0")+"方向\n经过"+((int)(StationAll.get("0").size())+1)+"站,到达"+mapStationName.get("0");
//				str2 = "乘坐 武汉"+StationFor.get("1")+"方向\n经过"+((int)(StationAll.get("1").size())+1)+"站,到达"+mapStationName.get("1");
//			}
//			if((StationFor.size()==3)&&(StationAll.size()==3)&&(mapStationName.size()==3)){
//				statDir =2;
//				statNum = StationAll.get("0").size()+1+ StationAll.get("1").size()+1+StationAll.get("2").size()+1;
//				str1 = "乘坐 武汉"+StationFor.get("0")+"方向\n经过"+((int)(StationAll.get("0").size())+1)+"站,到达"+mapStationName.get("0");
//				str2 = "乘坐 武汉"+StationFor.get("1")+"方向\n经过"+((int)(StationAll.get("1").size())+1)+"站,到达"+mapStationName.get("1");
//				str3 = "乘坐 武汉"+StationFor.get("2")+"方向\n经过"+((int)(StationAll.get("2").size())+1)+"站,到达"+mapStationName.get("2");
//			}
//		}
//		mTime.setText((int)(f/60)+"分钟");			//设置时间
//		mStationNum.setText("经过"+statNum+"站");		//设置总站数
//		if(statDir>0){
//			mHuan.setText(statDir +"次换乘");				//设置总站数
//		}else{
//			mHuan.setText("无需换乘");				//设置总站数
//		}
//
////		mPrice.setText(price+"元");
//
//		mRoadStart.setText(startStation.getStationName());		//起点
//		mRoadStop.setText(stopStation.getStationName());		//终点
//		mRoadHuan1.setText(str1);		//换乘站点1
//		if(str2!=""){
//			mRoadHuan2.setText(str2+"\n"+str3);;		//换乘站点2
//		}else{
//			linHuan2.setVisibility(View.GONE);
//		}
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