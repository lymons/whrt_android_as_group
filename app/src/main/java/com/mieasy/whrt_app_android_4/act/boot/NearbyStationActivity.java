package com.mieasy.whrt_app_android_4.act.boot;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.Point;
import com.mieasy.whrt_app_android_4.entity.Map;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.overlay.BusRouteOverlay;
import com.mieasy.whrt_app_android_4.overlay.DrivingRouteOverlay;
import com.mieasy.whrt_app_android_4.overlay.WalkRouteOverlay;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.AMapStationUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.util.LocationUtils;

import java.util.List;

public class NearbyStationActivity extends Activity implements RouteSearch.OnRouteSearchListener, View.OnClickListener {

    private static final String TAG = NearbyStationActivity.class.getSimpleName();
    MapView mapView;
    RouteSearch routeSearch;
    AMap aMap;
    Button bt_walk;
    Button bt_transit;
    Button bt_driving;
    DriveRouteResult mDriveRouteResult;

    LatLonPoint startStation;
    LatLonPoint stopStation;

    private AMapLocationClient locationClient = null;
    AMapLocation aMapLocation;
    private LiteOrm liteOrm;
    ImageButton iv_top_left_back;
    private TextView mSite;
    private Stations stationByStationID;
    private Stations stationsName = new Stations();

    private static final double EARTH_RADIUS = 6378137.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nearbystation);
        liteOrm = ContentApplication.getInstance().liteOrm;

        mapView = (MapView) findViewById(R.id.route_map);
        iv_top_left_back = (ImageButton) findViewById(R.id.iv_top_left_back);
        iv_top_left_back.setOnClickListener(this);

        mSite = (TextView) findViewById(R.id.tv_top_left_site);
        mapView.onCreate(savedInstanceState); //必须写

        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);

        getData();//获取页面传递的数据
        init();
    }


    private void getData() {
        boolean mapNav = getIntent().hasExtra(NumUtil.MAPNAV);
        if (mapNav) {
            String stationName = getIntent().getStringExtra("stationname");
            Log.e(TAG,"stationName:"+stationName);
            mSite.setText("："+stationName);
            startStation = getIntent().getParcelableExtra(NumUtil.START_STATION);
            stopStation = getIntent().getParcelableExtra(NumUtil.STOP_STATION);
            Log.e(TAG,"stopStation:" +stopStation);
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startStation, stopStation);
            Log.e(TAG,"stopStation:" +fromAndTo);
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
            Log.e(TAG,"stopStation:" +query);
            routeSearch.calculateWalkRouteAsyn(query);

        } else {
            //获取最近地铁站和当前位置
            initLocation();//初始化定位
            locationClient.startLocation();
        }
    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                Log.e(TAG, "定位结果：" + LocationUtils.getLocationStr(loc));
                if (loc.getErrorCode() == 0) {
                    locationClient.stopLocation();
                    aMapLocation = loc;
                    double longitude = loc.getLongitude();
                    double latitude = loc.getLatitude();

                    startStation = new LatLonPoint(latitude, longitude);


                    List<Map> allMapInfo = LiteOrmServices.getAllMapInfo(liteOrm);
                    double misDistance = 10000000000.00;
                    int subscript = 1000000;
                    for (int i = 0; i < allMapInfo.size(); i++) {
                        Double amapLongitude = allMapInfo.get(i).getAmapLongitude();
                        Double amapLatitude = allMapInfo.get(i).getAmapLatitude();

                        double distance = gps2m(amapLongitude,amapLatitude, latitude, longitude);
                        if (misDistance > distance) {
                            misDistance = distance;
                            subscript = i;
                        }
                    }

                    int stationId = allMapInfo.get(subscript).getStationId();
                    //根据stationId查询站点的map信息
                    Map mapBySiteID = LiteOrmServices.getMapBySiteID(liteOrm, stationId);
                    stopStation = new LatLonPoint(mapBySiteID.getAmapLongitude(),
                            mapBySiteID.getAmapLatitude());
                    stationByStationID = LiteOrmServices.getStationByStationID(liteOrm, stationId);
                    Toast.makeText(getApplicationContext(), "定位成功,离您最近的地铁站是："+stationByStationID.getStationName(), Toast.LENGTH_LONG).show();
                    //添加的标题的附近站点
                    mSite.setText("："+stationByStationID.getStationName());
                    RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startStation, stopStation);
                    RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
                    routeSearch.calculateWalkRouteAsyn(query);
                }  else if (loc.getErrorCode() == 12) {
                    locationClient.stopLocation();
                    Toast.makeText(getApplicationContext(), "定位失败,请打开定位权限后再重新操作!", Toast.LENGTH_LONG).show();
                } else if (loc.getErrorCode() == 4) {
                    locationClient.stopLocation();
                    Toast.makeText(getApplicationContext(), "网络连接异常,请检查设备网络是否通畅!", Toast.LENGTH_LONG).show();
                } else if (loc.getErrorCode() == 14) {
                    locationClient.stopLocation();
                    Toast.makeText(getApplicationContext(), "定位失败,GPS状态差,建议持设备到相对开阔的露天场所再次尝试!", Toast.LENGTH_LONG).show();
                } else {
                    locationClient.stopLocation();
                    Toast.makeText(getApplicationContext(), "定位失败,无法接收定位信号,请稍后再试!", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "定位失败:" + loc.getErrorInfo() + ",请重新操作!");
                }
            } else {
                locationClient.stopLocation();
                Toast.makeText(getApplicationContext(), "定位失败,无法接收定位信号,请稍后再试.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "定位失败:" + loc.getErrorInfo() + ",请重新操作!");
            }
        }
    };

    /**
     * 根据站点详细信息获取站点的横纵坐标
     *
     * @param stations
     * @return
     */
    private Point getPointByStations(Stations stations) {
        List<Map> mapList = LiteOrmServices.getMapByStation(liteOrm, stations);
        Point point = new Point();
        if (mapList.size() != 0) {
            point.setPointX((int) (mapList.get(0).getAmapX() + 0));
            point.setPointY((int) (mapList.get(0).getAmapY() + 0));
        }
        return point;
    }

    private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        return mOption;
    }

    private void init() {

        bt_walk = (Button) findViewById(R.id.bt_walk);
        bt_transit = (Button) findViewById(R.id.bt_transit);
        bt_driving = (Button) findViewById(R.id.bt_driving);

        //默认都是一个样式
        btColorAndBgReset();
        bt_walk.setTextColor(Color.parseColor("#4895e4"));
        bt_walk.setBackgroundResource(R.drawable.bt_selector_oneclick);


        bt_walk.setOnClickListener(this);
        bt_transit.setOnClickListener(this);
        bt_driving.setOnClickListener(this);

        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    private void btColorAndBgReset() {
        bt_walk.setTextColor(Color.parseColor("#FFFFFF"));
        bt_transit.setTextColor(Color.parseColor("#FFFFFF"));
        bt_driving.setTextColor(Color.parseColor("#FFFFFF"));
        bt_walk.setBackgroundResource(R.drawable.bt_selector_one);
        bt_transit.setBackgroundResource(R.drawable.bt_selector_two);
        bt_driving.setBackgroundResource(R.drawable.bt_selector_three);
    }


    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        Log.e(TAG, "result:" + result);
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    BusRouteResult mBusRouteResult = result;
                    BusRouteOverlay mBusrouteOverlay = new BusRouteOverlay(getApplicationContext(), aMap,
                            mBusRouteResult.getPaths().get(0), mBusRouteResult.getStartPos(),
                            mBusRouteResult.getTargetPos());
                    mBusrouteOverlay.setNodeIconVisibility(true);
                    mBusrouteOverlay.removeFromMap();
                    mBusrouteOverlay.addToMap();
                    mBusrouteOverlay.zoomToSpan();

                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(getApplicationContext(), "对不起，没有搜索到相关数据！", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "对不起，没有搜索到相关数据！", Toast.LENGTH_LONG).show();
            }
        } else if (errorCode == AMapException.CODE_AMAP_CLIENT_UNKNOWHOST_EXCEPTION) {
            Log.e(TAG, "获取数据失败，请检查网络连接是否畅通！");
            Toast.makeText(this, "获取数据失败，请检查网络连接是否畅通！", Toast.LENGTH_LONG).show();
        } else {
            Log.e(TAG, "获取数据失败！");
            Toast.makeText(this, "获取数据失败！", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            getApplicationContext(), aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapStationUtil.getFriendlyTime(dur) + "(" + AMapStationUtil.getFriendlyLength(dis) + ")";

                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();


                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(getApplicationContext(), "对不起，没有搜索到相关数据！", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "对不起，没有搜索到相关数据！", Toast.LENGTH_LONG).show();
            }
        } else if (errorCode == AMapException.CODE_AMAP_CLIENT_UNKNOWHOST_EXCEPTION) {
            Log.e(TAG, "获取数据失败，请检查网络连接是否畅通！");
            Toast.makeText(this, "获取数据失败，请检查网络连接是否畅通！", Toast.LENGTH_LONG).show();
        } else {
            Log.e(TAG, "获取数据失败！");
            Toast.makeText(this, "获取数据失败！", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    WalkRouteResult mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();

                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapStationUtil.getFriendlyTime(dur) + "(" + AMapStationUtil.getFriendlyLength(dis) + ")";

                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(getApplicationContext(), "对不起，没有搜索到相关数据！", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "对不起，没有搜索到相关数据！", Toast.LENGTH_LONG).show();
            }
        } else if (errorCode == AMapException.CODE_AMAP_CLIENT_UNKNOWHOST_EXCEPTION) {
            Log.e(TAG, "获取数据失败，请检查网络连接是否畅通！");
            Toast.makeText(this, "获取数据失败，请检查网络连接是否畅通！", Toast.LENGTH_LONG).show();
        } else {
            Log.e(TAG, "获取数据失败！");
            Toast.makeText(this, "获取数据失败！", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁定位
        destroyLocation();
        mapView.onDestroy();
        startStation = null;
        stopStation = null;
        aMapLocation = null;
    }

    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
        }
    }

    @Override
    public void onClick(View v) {
        btColorAndBgReset();
        switch (v.getId()) {
            case R.id.bt_walk:
                //步行
                bt_walk.setTextColor(Color.parseColor("#4895e4"));
                bt_walk.setBackgroundResource(R.drawable.bt_selector_oneclick);

                RouteSearch.FromAndTo fromAndTo_walk = new RouteSearch.FromAndTo(startStation, stopStation);
                RouteSearch.WalkRouteQuery query_walk = new RouteSearch.WalkRouteQuery(fromAndTo_walk, RouteSearch.WalkDefault);
                routeSearch.calculateWalkRouteAsyn(query_walk);
                break;

            case R.id.bt_transit:
                //公交
                bt_transit.setTextColor(Color.parseColor("#4895e4"));
                bt_transit.setBackgroundResource(R.drawable.bt_selector_twoclick);
                RouteSearch.FromAndTo fromAndTo_bus = new RouteSearch.FromAndTo(startStation, stopStation);
                RouteSearch.BusRouteQuery query_bus = new RouteSearch.BusRouteQuery(fromAndTo_bus, RouteSearch.BusDefault,
                        "027", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
                routeSearch.calculateBusRouteAsyn(query_bus);// 异步路径规划公交模式查询
                break;

            case R.id.bt_driving:
                //自驾
                bt_driving.setTextColor(Color.parseColor("#4895e4"));
                bt_driving.setBackgroundResource(R.drawable.bt_selector_threeclick);

                RouteSearch.FromAndTo fromAndTo_drivi = new RouteSearch.FromAndTo(startStation, stopStation);
                // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
                RouteSearch.DriveRouteQuery query_drivi = new RouteSearch.DriveRouteQuery(fromAndTo_drivi, RouteSearch.DrivingDefault,
                        null, null, "");
                routeSearch.calculateDriveRouteAsyn(query_drivi);
                break;
            case R.id.iv_top_left_back:
                finish();
                break;
        }
    }
}