package com.mieasy.whrt_app_android_4.act.boot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.nav.ShowOutSideActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.Point;
import com.mieasy.whrt_app_android_4.entity.Map;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.imp.PointCallBackInterface;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.BitmapUtil;
import com.mieasy.whrt_app_android_4.util.LocationUtils;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.view.ConfirmDialog;
import com.mieasy.whrt_app_android_4.view.DragImageView;
import com.mieasy.whrt_app_android_4.view.LocationDialog;

import java.io.InputStream;
import java.util.List;

/**
 * 选取站内站外
 *
 * @author Administrator
 */
public class MapNavActivity extends Activity implements OnClickListener {
    private static final String TAG = MapNavActivity.class.getSimpleName();
    private Point point, pointByStations;
    private LiteOrm liteOrm;
    private Stations station, stationStart, stationStop;
    private int[] lineNum;

    private int window_width, window_height;// 控件宽度
    private DragImageView dragImageView;    // 自定义控件
    private int state_height;                // 状态栏的高度

    private ViewTreeObserver viewTreeObserver;
    private Context context;

    private Bundle bundle;
    private ImageButton mImageButton;

    private Bitmap bmp, bmpStart, bmpStop, bitmapStation;

    private ConfirmDialog confirmDialog;    //onWindowFocusChanged

    TextView tv_start_tracation;
    TextView tv_end_tracation;
    ImageButton imgbt_right_showSite;
    ImageView img_latelySite;
    ImageView img_push_up;

    LinearLayout ll_selectorsite;
    int requestCode;

    boolean isHideSelectorSite = false;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;
    private static final double EARTH_RADIUS = 6378137.0;

    private Stations stationSite = new Stations();

    AMapLocation aMapLocation;
    LocationDialog locationDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_boot);
        this.context = this;
        liteOrm = ContentApplication.getInstance().liteOrm;

        List<Stations> station = LiteOrmServices.getStation(liteOrm);
        Log.e(TAG, "station:" + station.size());
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        getWindow().setLayout(window_width,window_height-10);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initView();
        setDragImageView();
        initLocation();
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
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
                    //根据经纬度查找匹配的数据库获取站点的ID
                    List<Map> allMapInfo = LiteOrmServices.getAllMapInfo(liteOrm);
                    double misDistance = 10000000000.00;
                    int subscript = 1000000;
                    for (int i = 0; i < allMapInfo.size(); i++) {
                        Double amapLongitude = allMapInfo.get(i).getAmapLongitude();
                        Double amapLatitude = allMapInfo.get(i).getAmapLatitude();

                        double distance = gps2m(amapLongitude, amapLatitude, latitude, longitude);
                        if (misDistance > distance) {
                            misDistance = distance;
                            subscript = i;
                        }
                    }

                    int stationId = allMapInfo.get(subscript).getStationId();
                    //根据stationId查询站点信息
                    stationSite = LiteOrmServices.getStationByStationID(liteOrm, stationId);
                    Toast.makeText(getApplicationContext(), "定位成功,离您最近的地铁站是：" + stationSite.getStationName(), Toast.LENGTH_SHORT).show();

                    pointByStations = getPointByStations(stationSite);
                    dragImageView.setStationPoint(pointByStations);

                    dragImageView.setStationVisible(pointByStations);


                } else if (loc.getErrorCode() == 12) {
                    locationClient.stopLocation();
                    Toast.makeText(getApplicationContext(), "定位失败,请打开定位权限后再重新操作!", Toast.LENGTH_SHORT).show();
                } else if (loc.getErrorCode() == 4) {
                    locationClient.stopLocation();
                    Toast.makeText(getApplicationContext(), "网络连接异常,请检查设备网络是否通畅!", Toast.LENGTH_SHORT).show();
                } else if (loc.getErrorCode() == 14) {
                    locationClient.stopLocation();
                    Toast.makeText(getApplicationContext(), "定位失败,GPS状态差,建议持设备到相对开阔的露天场所再次尝试!", Toast.LENGTH_SHORT).show();
                } else {
                    locationClient.stopLocation();
                    Toast.makeText(getApplicationContext(), "定位失败,无法接收定位信号,请稍后再试!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "定位失败:" + loc.getErrorInfo() + ",请重新操作!");
                }
            } else {
                locationClient.stopLocation();
                Toast.makeText(getApplicationContext(), "定位失败,无法接收定位信号,请稍后再试.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "定位失败:" + loc.getErrorInfo() + ",请重新操作!");
            }
        }
    };

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

    private double distanceRadians(double nextLat, double nextLng, double prevLat, double prevLng) {
        return arcHav(havDistance(nextLat, prevLat, nextLng - prevLng));
    }


    /**
     * Computes inverse haversine. Has good numerical stability around 0.
     * arcHav(x) == acos(1 - 2 * x) == 2 * asin(sqrt(x)).
     * The argument must be in [0, 1], and the result is positive.
     */
    static double arcHav(double x) {
        return 2 * Math.asin(Math.sqrt(x));
    }

    /**
     * Returns hav() of distance from (lat1, lng1) to (lat2, lng2) on the unit sphere.
     */
    static double havDistance(double lat1, double lat2, double dLng) {
        return hav(lat1 - lat2) + hav(dLng) * Math.cos(lat1) * Math.cos(lat2);
    }

    /**
     * Returns haversine(angle-in-radians).
     * hav(x) == (1 - cos(x)) / 2 == sin(x / 2)^2.
     */
    static double hav(double x) {
        double sinHalf = Math.sin(x * 0.5);
        return sinHalf * sinHalf;
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        // 启动定位
        locationClient.startLocation();
    }

    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(false);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        //设置是否使用传感器
        locationOption.setSensorEnable(true);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        locationOption.setWifiScan(true);

        try {
            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            locationOption.setInterval(Long.valueOf(1000));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            // 设置网络请求超时时间
            locationOption.setHttpTimeOut(1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    private void initView() {
        ll_selectorsite = (LinearLayout) findViewById(R.id.ll_selectorsite);

        mImageButton = (ImageButton) findViewById(R.id.iv_top_left_back);
        imgbt_right_showSite = (ImageButton) findViewById(R.id.imgbt_right_showSite);
        tv_start_tracation = (TextView) findViewById(R.id.tv_start_tracation);
        tv_end_tracation = (TextView) findViewById(R.id.tv_end_tracation);
        img_latelySite = (ImageView) findViewById(R.id.img_latelySite);
        img_push_up = (ImageView) findViewById(R.id.img_push_up);

        mImageButton.setOnClickListener(this);
        imgbt_right_showSite.setOnClickListener(this);
        tv_start_tracation.setOnClickListener(this);
        tv_end_tracation.setOnClickListener(this);
        img_latelySite.setOnClickListener(this);
        img_push_up.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_top_left_back:
                MapNavActivity.this.finish();
                break;
            case R.id.imgbt_right_showSite:
                if (isHideSelectorSite) {
                    ll_selectorsite.setVisibility(View.VISIBLE);
                    isHideSelectorSite = false;
                } else {
                    ll_selectorsite.setVisibility(View.INVISIBLE);
                    isHideSelectorSite = true;
                }
                break;
            case R.id.tv_start_tracation:
                Intent intentStatr = new Intent(this, SelectSiteActivity.class);
                //Intent intentStatr = new Intent(this, SelectSiteLineActivity.class);
                requestCode = 1;
                startActivityForResult(intentStatr, requestCode);
                break;
            case R.id.tv_end_tracation:
                Intent intentEnd = new Intent(this, SelectSiteActivity.class);
                //Intent intentEnd = new Intent(this, SelectSiteLineActivity.class);
                requestCode = 2;
                startActivityForResult(intentEnd, requestCode);
                break;
            case R.id.img_latelySite:
                //站点定位
                startLocation();
                break;
            case R.id.img_push_up:
                ll_selectorsite.setVisibility(View.INVISIBLE);
                isHideSelectorSite = true;
                break;
        }
    }


    String startName;
    String endName;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "requestCode:" + requestCode + ",resultCode:" + resultCode);
        if (requestCode == 1 && resultCode == 0) {
            startName = data.getStringExtra("name");
            tv_start_tracation.setText(startName);
            //根据站点名获取站点ID
            int siteID = LiteOrmServices.getSiteIDBySiteName(liteOrm, startName);
            //根据站点ID获取
            Map mapBySiteID = LiteOrmServices.getMapBySiteID(liteOrm, siteID);
            Point point = new Point(mapBySiteID.getAmapX(), mapBySiteID.getAmapY());
            Stations station = getStationPoint(point);
            stationStart = station;
            dragImageView.setStartPoint(getPointByStations(station));
            findPathByPointByStation(stationStart, stationStop);

        } else if (requestCode == 2 && resultCode == 0) {
            endName = data.getStringExtra("name");
            tv_end_tracation.setText(endName);
            int siteID = LiteOrmServices.getSiteIDBySiteName(liteOrm, endName);
            //根据站点ID获取
            Map mapBySiteID = LiteOrmServices.getMapBySiteID(liteOrm, siteID);
            Point point = new Point(mapBySiteID.getAmapX(), mapBySiteID.getAmapY());
            Stations station = getStationPoint(point);
            stationStop = station;
            dragImageView.setStopPoint(getPointByStations(station));
            findPathByPointByStation(stationStart, stationStop);
        }


    }

    //设置弹窗
    public void setDragImageView() {
        /** 获取可見区域高度 **/
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();

        dragImageView = (DragImageView) findViewById(R.id.div_main);
        if (bmp == null) {
            bmp = BitmapUtil.ReadBitmapByAssetFileName(this, "map.jpg", 2000, 2000);
            if (bmpStart == null) {
                bmpStart = BitmapUtil.ReadBitmapById(this, R.drawable.icon_st, 96, 96);
                if (bmpStop == null) {
                    bmpStop = BitmapUtil.ReadBitmapById(this, R.drawable.icon_en, 96, 96);
                    if (bitmapStation == null) {
                        bitmapStation = BitmapUtil.ReadBitmapById(this, R.drawable.indicate, 96, 96);
                    }
                }
            }
        }
        // 设置图片
        dragImageView.setStationImageBitmap(bitmapStation);
        dragImageView.setStopImageBitmap(bmpStop);
        dragImageView.setStartImageBitmap(bmpStart);
        dragImageView.setImageBitmap(bmp);

//		dragImageView.setmActivity(this);//注入Activity.
        dragImageView.SetOnClickCallBack(new PointCallBackInterface() {
            @Override
            public void CallBack(double pointX, double pointY) {
                //查询pointX，pointY坐标   并返回站名以及站名对应的坐标
                point = new Point((int) pointX, (int) pointY);
                station = getStationPoint(point);

                if (station != null && station.getStationId() != stationSite.getStationId()) {
                    confirmDialog = new ConfirmDialog(context, station.getStationName(), "退出", "取消", lineNum);
//                    Window dialogWindow = confirmDialog.getWindow();
//                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                    lp.x = 10; // 新位置X坐标
//                    lp.y = 10; // 新位置Y坐标
//                    dialogWindow.setAttributes(lp);
                    confirmDialog.show();
                    confirmDialog.setCanceledOnTouchOutside(true);
                    confirmDialog.setCancelable(true);
                    confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {

                        @Override
                        public void doStart() {            //设为起点
                            stationStart = station;
                            dragImageView.setStartPoint(getPointByStations(station));
                            findPathByPoint(stationStart, stationStop);
                            tv_start_tracation.setText(stationStart.getStationName());
                            confirmDialog.dismiss();
                        }

                        @Override
                        public void doStop() {            //设为终点
                            stationStop = station;
                            dragImageView.setStopPoint(getPointByStations(station));
                            findPathByPoint(stationStart, stationStop);
                            tv_end_tracation.setText(stationStop.getStationName());
                            confirmDialog.dismiss();
                        }

                        @Override
                        public void doOutside() {        //站外信息
                            jumpActivity(station, 1);
                            confirmDialog.dismiss();
                        }

                        @Override
                        public void doIntra() {            //站内信息
                            jumpActivity(station, 0);
                            confirmDialog.dismiss();
                        }
                    });
                } else if (station != null && station.getStationId() == stationSite.getStationId()) {
                    locationDialog = new LocationDialog(context, stationSite.getStationName(), "退出", "取消", lineNum);
                    locationDialog.show();
                    locationDialog.setCanceledOnTouchOutside(true);
                    locationDialog.setCancelable(true);
                    locationDialog.setClicklistener(new LocationDialog.ClickListenerInterface() {

                        @Override
                        public void doStart() {            //设为起点
                            stationStart = station;
                            dragImageView.setStartPoint(getPointByStations(station));
                            findPathByPoint(stationStart, stationStop);
                            locationDialog.dismiss();
                        }

                        @Override
                        public void doStop() {            //设为终点
                            stationStop = station;
                            dragImageView.setStopPoint(getPointByStations(station));
                            findPathByPoint(stationStart, stationStop);
                            locationDialog.dismiss();
                        }

                        @Override
                        public void doOutside() {        //站外信息
                            jumpActivity(station, 1);
                            locationDialog.dismiss();
                        }

                        @Override
                        public void doIntra() {            //站内信息
                            jumpActivity(station, 0);
                            locationDialog.dismiss();
                        }

                        @Override
                        public void gohere() {
                            Intent intent = new Intent(MapNavActivity.this, NearbyStationActivity.class);
                            bundle = new Bundle();
                            //传递页面选择信息
                            Map stopLonpint = LiteOrmServices.getMapBySiteID(liteOrm, stationSite.getStationId());
                            LatLonPoint startStation = new LatLonPoint(aMapLocation.getLatitude(),
                                    aMapLocation.getLongitude());
                            LatLonPoint stopStation = new LatLonPoint(stopLonpint.getAmapLongitude(),
                                    stopLonpint.getAmapLatitude());
                            String stationSites = stationSite.getStationName();
                            intent.putExtra(NumUtil.START_STATION, startStation);
                            intent.putExtra(NumUtil.STOP_STATION, stopStation);
                            intent.putExtra("stationname",stationSites);
                            intent.putExtra(NumUtil.MAPNAV, true);
                            startActivity(intent);
                            locationDialog.dismiss();
                        }
                    });
                }

            }
        });
        /** 测量状态栏高度 **/
        viewTreeObserver = dragImageView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (state_height == 0) {
//					 获取状况栏高度
                    Rect frame = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    state_height = frame.top;
                    dragImageView.setScreen_H(window_height - state_height - 120);
                    dragImageView.setScreen_W(window_width);
                }
            }
        });
    }

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


    //查看站外站外
    private void jumpActivity(Stations stationXY, int item) {
        Intent intent = new Intent(MapNavActivity.this, ShowOutSideActivity.class);
        bundle = new Bundle();
        //传递页面选择信息
        intent.putExtra(NumUtil.STATION_ITEM, item);
        //传递站点信息
        bundle.putParcelable(NumUtil.JUMP_BUNDLE, stationXY);
        intent.putExtra(NumUtil.JUMP_INTENT, bundle);
        startActivity(intent);
//        confirmDialog.dismiss();
    }

    //查找路线
    public void findPathByPoint(Stations stationStart, Stations stationStop) {
        if (stationStart != null && stationStop != null) {
            Intent intent = new Intent(MapNavActivity.this, PathActivity.class);
            bundle = new Bundle();
            bundle.putParcelable(NumUtil.STATIONS_START, stationStart);
            bundle.putParcelable(NumUtil.STATIONS_STOP, stationStop);
            intent.putExtra(NumUtil.STATIONS, bundle);

            startActivity(intent);
//            confirmDialog.dismiss();
        }
    }

    //查找路线
    public void findPathByPointByStation(Stations stationStart, Stations stationStop) {
        if (stationStart != null && stationStop != null) {
            Intent intent = new Intent(MapNavActivity.this, PathActivity.class);
            bundle = new Bundle();
            bundle.putParcelable(NumUtil.STATIONS_START, stationStart);
            bundle.putParcelable(NumUtil.STATIONS_STOP, stationStop);
            intent.putExtra(NumUtil.STATIONS, bundle);
            startActivity(intent);
        }
    }

    //获取线路以及坐标
    public Stations getStationPoint(Point pointTouch) {
        Stations stationsTouch;
        List<Map> mapList = LiteOrmServices.getMapInfoByTouch(liteOrm, pointTouch);
        //		List<Map> mapList = LiteOrmServices.getMapInfoByTouch(liteOrm, new Point(215, 675));
        if (mapList.size() != 0) {
            Log.i("MapNavActivity", "坐标对比成功");
            List<Stations> stationList = LiteOrmServices.getStationByMap(liteOrm, mapList.get(0));
            if (stationList.size() != 0) {
                Log.i("MapNavActivity", "地点获取成功");
                stationsTouch = stationList.get(0);
            } else {
                Log.i("MapNavActivity", "地点获取失败");
                stationsTouch = null;
            }
        } else {
            Log.i("MapNavActivity", "坐标对比失败");
            stationsTouch = null;
        }
        return stationsTouch;
    }

    /**
     * 读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (confirmDialog != null)
            confirmDialog.dismiss();
        if (!bmp.isRecycled())
            bmp.recycle();
        if (!bmpStart.isRecycled())
            bmpStart.recycle();
        if (!bmpStop.isRecycled())
            bmpStop.recycle();
        if (!bitmapStation.isRecycled())
            bitmapStation.recycle();
        //销毁定位
        destroyLocation();

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MapNav Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
    }
}