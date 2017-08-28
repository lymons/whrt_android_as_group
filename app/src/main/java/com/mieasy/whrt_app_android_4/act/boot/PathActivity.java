package com.mieasy.whrt_app_android_4.act.boot;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.SiteCollect;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.entity.change.PathInfoChange;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.services.PathInfoChangeManager;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.view.InfoViewPager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

/**
 * 路线引导、信息引导    主Activity
 *
 * @author Administrator
 */
public class PathActivity extends FragmentActivity implements OnClickListener, OnTouchListener {
    private static final String TAG = "PathActivity";

    private ImageButton imgButton, imgMap, imgLine, imgShare, imageAdd;
    private TextView mMapPath, mLinePath;
    private TextView tvTitle;
    private Stations startStation, stopStation;
    private InfoViewPager infoViewPager;
    private FragmentPagerAdapter fragmentAdapter;
    private List<Fragment> fragments;
    private Fragment lineFragment, mapFragment;

    private RequestQueue mQueue;
    private Bundle bundle;
    private Map<String, String> map;

    private Integer id;//收藏的id
    private boolean isAddrecordSuccess = false;

    private TextView mStationNum, mHuan, mPrice, mTime;

    private LiteOrm liteOrm, liteOrm_add;
    private List<SiteCollect> list_stationname;
    private boolean isCancle;

    DecimalFormat df = new DecimalFormat("#.#");
    private List<Stations> stationsAllList;        //全部路线信息
    private HashMap<String, Integer> hashMap;    //全部重要站点信息   "list中的ID","方向信息"
    private int item = 0;                            //记录换乘站在list中的id

    public PathActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_boot_path);
        //可以将一下代码加到你的MainActivity中，或者在任意一个需要调用分享功能的activity当中
        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
        ActivityCompat.requestPermissions(PathActivity.this, mPermissionList, 100);
        mQueue = ContentApplication.getInstance().mQueue;
        liteOrm = ContentApplication.getInstance().liteOrm;
        liteOrm_add = ContentApplication.getInstance().liteOrm_add;
        setInfoView();
        initValue();
        initView();
        initViewPager();
        SelectStation();
    }

    private void setInfoView() {
        initViews();
        new Thread(new mRunnable()).start();
    }

    private void initViews() {
        mStationNum = (TextView) findViewById(R.id.tv_road_stationNum);    //经过了多少站
        mHuan = (TextView) findViewById(R.id.tv_road_huancheng_num);        //经过多少次换乘
        mPrice = (TextView) findViewById(R.id.tv_road_price);                //花费多少钱
        mTime = (TextView) findViewById(R.id.tv_road_time);                    //花多长时间
    }

    class mRunnable implements Runnable {
        @Override
        public void run() {
            PathInfoChangeManager.setLinesMap(liteOrm);
            PathInfoChangeManager.setDirectionsMap(liteOrm);
            List<PathInfoChange> pathInfoChangeList = PathInfoChangeManager.getAllPathInfo(liteOrm, startStation, stopStation);
            Message msg = Message.obtain();
            if (pathInfoChangeList.size() != 0) {
                msg.what = 1;
                msg.obj = pathInfoChangeList;
            } else {
                msg.what = 0;
            }
            handler.sendMessage(msg);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what != 0) {
                setInitViewInfo((List<PathInfoChange>) msg.obj);
            }
        }

        ;
    };

    private void setInitViewInfo(List<PathInfoChange> pathInfoChangeList) {
        mStationNum.setText((pathInfoChangeList.get(0).getTotal_stations()) + "");        //设置总站数
        mHuan.setText(pathInfoChangeList.get(0).getChange_count() + "");                        //设置总站数
        mPrice.setText(pathInfoChangeList.get(0).getPrices());
        mTime.setText("全程：" + df.format(Double.parseDouble(pathInfoChangeList.get(0).getDistance())) + "公里  |  预计用时：" + setShowTime(pathInfoChangeList.get(0).getTime()));
    }

    //设置时间
    private String setShowTime(int time) {
        String str = "";
        if (time < 3600) {
            str = time / 60 + "分钟";
            if (time < 300) {
                str += (time % 60 + "秒");
            }
        } else {
            int h = time / 3600;
            str = h + "小时" + ((time - h * 3600) / 60) + "分钟";
        }
        return str;
    }

    private void initViewPager() {
        fragments = new ArrayList<Fragment>();
//		mapFragment = new PathMapFragment();	 //百度地图版
//		mapFragment = new PathAMapFragment();	 //地图路线
        mapFragment = new PathImageFragment();     //地图路线
        lineFragment = new PathLineFragment();
        mapFragment.setArguments(bundle);
        fragments.add(mapFragment);

//		lineFragment = new PathAMapLineFragment();   //路线
        lineFragment.setArguments(bundle);
        fragments.add(lineFragment);

        fragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        };
        infoViewPager.setAdapter(fragmentAdapter);
        infoViewPager.setOnTouchListener(this);
        setViewPagerItem(0);
    }

    private void initView() {
        infoViewPager = (InfoViewPager) findViewById(R.id.vp_infoviewpager);
        tvTitle = (TextView) findViewById(R.id.tv_top_left_title);
        tvTitle.setText(startStation.getStationName() + " - " + stopStation.getStationName());
        imgButton = (ImageButton) findViewById(R.id.iv_top_left_back);        //返回

        imgShare = (ImageButton) findViewById(R.id.iv_top_left_home);
        imgShare.setImageResource(R.drawable.ic_share_black_24dp);
        imgShare.setOnClickListener(this);

        imgMap = (ImageButton) findViewById(R.id.ib_infopath);
        imgMap.setOnClickListener(this);
        imgLine = (ImageButton) findViewById(R.id.ib_linepath);
        mMapPath = (TextView) findViewById(R.id.tv_infopath);                //地图
        mLinePath = (TextView) findViewById(R.id.tv_linepath);                //线路
        imageAdd = (ImageButton) findViewById(R.id.btn_image_site_add);      //添加收藏起点和终点
        imgLine.setOnClickListener(this);
        imgButton.setOnClickListener(this);
        imageAdd.setOnClickListener(this);

    }

    private void initValue() {
        Intent intent = getIntent();
        bundle = intent.getBundleExtra(NumUtil.STATIONS);
        startStation = bundle.getParcelable(NumUtil.STATIONS_START);
        stopStation = bundle.getParcelable(NumUtil.STATIONS_STOP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_left_back:            //点击返回
                PathActivity.this.finish();
                finish();
                break;
            case R.id.ib_linepath:                //线路规划
                setViewPagerItem(0);
                break;
            case R.id.ib_infopath:                //线路概况
                setViewPagerItem(1);
                break;
            case R.id.btn_image_site_add:
                if (isAddrecordSuccess) {
                    DeleteLine();
                } else {
                    AddSiteline();
                }
                break;
            case R.id.iv_top_left_home://分享
                ShareInfo();
                //shareText(v);
                break;
            default:
                break;
        }
    }

    //站内站外的图片背景设置
    private void imgDarken() {
        imgMap.setImageResource(R.drawable.navi_exit_deselect);
        imgLine.setImageResource(R.drawable.navi_inside_deselect);

        mMapPath.setTextColor(getResources().getColor(R.color.activity_bg_color_gray));
        mLinePath.setTextColor(getResources().getColor(R.color.activity_bg_color_gray));
    }

    int itemId = -1;
    int itemIdNew = -1;

    private void SelectStation() {
        List<SiteCollect> siteCollect = LiteOrmServices.getSiteCollectInfo(liteOrm_add);
        if (siteCollect.size() > 0) {
            for (int i = 0; i < siteCollect.size(); i++) {
                String type = siteCollect.get(i).getType();
                if (type.equals("1")) {
                    String stationName1 = siteCollect.get(i).getStationName();
                    String stationto1 = siteCollect.get(i).getStationto();
                    String stationName2 = startStation.getStationName();
                    String stationto2 = stopStation.getStationName();
                    if (stationName1.equals(stationName2) && stationto1.equals(stationto2)) {
                        id = siteCollect.get(i).getId();
                        itemId = i;
                    } else {
                        itemIdNew = -1;
                    }
                }
            }

            if (itemId != -1 && itemIdNew == -1) {
                isAddrecordSuccess = true;
                imageAdd.setBackgroundResource(R.drawable.navi_collect_high);
//                SiteCollect siteCollect1 = siteCollect.get(item);
//                id = siteCollect1.getId();
            } else {
                isAddrecordSuccess = false;
                imageAdd.setBackgroundResource(R.drawable.navi_collect_add);
            }
        }
    }

    private void AddSiteline() {
        SelectStation();
        list_stationname = new ArrayList<SiteCollect>();
        final SiteCollect site = new SiteCollect();
        site.setStationId(String.valueOf(startStation.getStationId()));
        site.setStationName(startStation.getStationName());
        site.setStationtoId(String.valueOf(stopStation.getStationId()));
        site.setStationto(stopStation.getStationName());
        site.setType("1");
        list_stationname.add(site);
        liteOrm_add.cascade().insert(list_stationname);
        isAddrecordSuccess = true;
        imageAdd.setBackgroundResource(R.drawable.navi_collect_high);
        Toast.makeText(getApplicationContext(), "收藏成功！", Toast.LENGTH_SHORT).show();

    }

    private void DeleteLine() {
        SelectStation();
        list_stationname = new ArrayList<SiteCollect>();
        final SiteCollect site = new SiteCollect();
        site.setId(id);
        site.setStationId(String.valueOf(startStation.getStationId()));
        site.setStationName(startStation.getStationName());
        site.setStationtoId(String.valueOf(stopStation.getStationId()));
        site.setStationto(stopStation.getStationName());
        site.setType("1");
        list_stationname.add(site);
        liteOrm_add.cascade().delete(list_stationname);
        isAddrecordSuccess = false;
        imageAdd.setBackgroundResource(R.drawable.navi_collect_add);
        Toast.makeText(getApplicationContext(), "取消成功！", Toast.LENGTH_SHORT).show();
    }

    //设置点击站内站外按钮的事件
    public void setViewPagerItem(int itemID) {
        imgDarken();
        if (itemID == 1) {
            imgMap.setImageResource(R.drawable.navi_exit_select);
            mMapPath.setTextColor(getResources().getColor(R.color.activity_bg_color_blue));
            infoViewPager.setCurrentItem(itemID);
        } else if (itemID == 0) {
            imgLine.setImageResource(R.drawable.navi_inside_select);
            mLinePath.setTextColor(getResources().getColor(R.color.activity_bg_color_blue));
            infoViewPager.setCurrentItem(itemID);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    //分享
    public void shareText(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "武汉地铁【" + startStation.getStationName() + "】站至【" + stopStation.getStationName() + "】站详细路线信息");
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享路线到"));
    }

    //分享单张图片
    public void shareSingleImage(View view) {
        String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(imagePath));
//		Log.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/test.jpg

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享路线到"));
    }

    //分享多张图片
    public void shareMultipleImage(View view) {
        ArrayList<Uri> uriList = new ArrayList<Uri>();

        String path = Environment.getExternalStorageDirectory() + File.separator;
        uriList.add(Uri.fromFile(new File(path + "australia_1.jpg")));
//		uriList.add(Uri.fromFile(new File(path+"australia_2.jpg")));
//		uriList.add(Uri.fromFile(new File(path+"australia_3.jpg")));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享路线到"));
    }

    //ShareAction shareAction;
    //分享
    private void ShareInfo() {
       // SHARE_MEDIA.SINA,  新浪的标示
        ShareAction shareAction = new ShareAction(PathActivity.this).setDisplayList( SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SMS);
        shareAction.withTitle("分享路线");
        shareAction.withText("武汉地铁【" + startStation.getStationName() + "】站至【" + stopStation.getStationName() + "】站详细路线信息");
        shareAction.withMedia(new UMImage(this, R.drawable.logo));
        shareAction.withTargetUrl("http://app2.whrt.gov.cn/share/nav.php?START_NUM=" + startStation.getStationNum() + "&END_NUM=" + stopStation.getStationNum() + "");
        shareAction.setCallback(umShareListener);
        shareAction.open();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//            //shareAction.getShareContent().getClass().getEnclosingClass();
//    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(PathActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PathActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(PathActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(PathActivity.this, platform + " 已分享,返回！", Toast.LENGTH_SHORT).show();
        }
    };

    //	分享后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult");
    }


    //获取起点和终点的站点名,添加收藏
//    public void AddLineStartingStop() {
//        //判断是否登陆
//        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserDatabase", Context.MODE_PRIVATE);
//        final String userName = sharedPreferences.getString("Loginame", "");
//        final String password = sharedPreferences.getString("PassWord", "");
//        if (userName.equals("")) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(PathActivity.this);
//            builder.setTitle("请登录！");
//            builder.setMessage("确认是否登录？");
//            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    dialog.cancel();
//                }
//            }).create().show();
//            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    Intent intent = new Intent(PathActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
//            }).create().show();
//        } else if (userName != null && password != null) {
//            if (isAddrecordSuccess) {
//                map = new HashMap<String, String>();
//                map.put("loginname", userName);
//                map.put("password", password);
//                map.put("id", id);
//               CollectionRequest(Request.Method.POST, NumUtil.URL_COLLECTION_DELETE, map,true);
//            } else {
//                map = new HashMap<String, String>();
//                map.put("loginname", userName);
//                map.put("password", password);
//                if (startStation == stopStation) {
//                    map.put("type", "1");
//                } else {
//                    map.put("type", "0");
//                }
//                map.put("rank", "1");
//                map.put("stationfrom", startStation.getStationId() + "");
//                map.put("stationto", stopStation.getStationId() + "");
//               CollectionRequest(Request.Method.POST, NumUtil.URL_COLLECTION_ADD, map,false);
//            }
//        }
//    }
//
//    private void CollectionRequest(int method, String url, final Map<String, String> map, final boolean isCancle) {
//        StringRequest stringRequest = new StringRequest(
//                method,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(s);
//                            String code = jsonObject.getString("code");
//                            Log.e(TAG, "code" + "-----------" + code);
//                            if (code.equals("1")) {
//                                if(isCancle){
//                                    isAddrecordSuccess = false;
//                                    imageAdd.setBackgroundResource(R.drawable.navi_collect_add);
//                                    Toast.makeText(getApplicationContext(), "取消成功！", Toast.LENGTH_LONG).show();
//                                }else{
//                                    isAddrecordSuccess = true;
//                                    imageAdd.setBackgroundResource(R.drawable.navi_collect_high);
//                                    Toast.makeText(getApplicationContext(), "收藏成功！", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return map;
//            }
//        };
//        mQueue.add(stringRequest);
//    }
}