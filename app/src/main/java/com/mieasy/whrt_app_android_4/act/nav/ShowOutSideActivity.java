package com.mieasy.whrt_app_android_4.act.nav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.ExitInfos;
import com.mieasy.whrt_app_android_4.bean.SiteCollect;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.view.InfoViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 站内站外Activity
 * @author Administrator
 *
 */
public class ShowOutSideActivity extends FragmentActivity implements OnClickListener, OnTouchListener, OnPageChangeListener {
	private static final String TAG = "ShowOutSideActivity";
	private ImageButton mImageButton, mOutside, mIntra, ib_exitinfo;
	private TextView mTvOutSide, mTvIntra, tv_exitinfo, tv_top_left_title;
	private InfoViewPager mViewPager;
	private Fragment outsideFrag, intraFrag, exitinfoFrag;
	private FragmentPagerAdapter mAdatper;
	private List<Fragment> fragments;
	private ImageButton mImageAdd;

	private Stations stations;
	private int item;
	private Bundle bundle;
	private RequestQueue mQueue;
	private boolean isAddrecordSuccess = false;
	private Map<String, String> map;
	private Integer id;//收藏的id
	private List<SiteCollect> list_stationname;
	private boolean addsite;//图片显示已收藏
	private LiteOrm liteOrm_add;
	private List<ExitInfos> exitInfo;
	private LiteOrm liteOrm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_nav);
		mQueue = ContentApplication.getInstance().mQueue;
		liteOrm_add = ContentApplication.getInstance().liteOrm_add;
		liteOrm = ContentApplication.getInstance().liteOrm;
		getIntentInfo();
		initView();
		initViewPager();
	}

	//获取Intent传值
	private void getIntentInfo() {

		Intent intent = getIntent();
		bundle = intent.getBundleExtra(NumUtil.STATIONS);
//		bundle = intent.getBundleExtra(NumUtil.STANDLINE);
		bundle = intent.getBundleExtra(NumUtil.JUMP_INTENT);
		item = intent.getIntExtra(NumUtil.STATION_ITEM, 0);


		stations = bundle.getParcelable(NumUtil.JUMP_BUNDLE);
	}

	//初始化viewpager
	private void initViewPager() {
		mOutside.setOnClickListener(this);
		mIntra.setOnClickListener(this);
		ib_exitinfo.setOnClickListener(this);

		mAdatper = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public Fragment getItem(int position) {
				return fragments.get(position);
			}
		};
		mViewPager.setAdapter(mAdatper);
		setViewPagerItem(item);
		mViewPager.setOnTouchListener(this);
		mViewPager.setOnPageChangeListener(this);
	}

	//初始化view
	private void initView() {
		mImageButton = (ImageButton) findViewById(R.id.iv_top_left_back);
		mImageButton.setOnClickListener(this);
		mViewPager = (InfoViewPager) findViewById(R.id.lv_viewpager);
		mViewPager.setOffscreenPageLimit(3);

		mImageAdd = (ImageButton) findViewById(R.id.add_site);//添加收藏
		mImageAdd.setOnClickListener(this);

		mOutside = (ImageButton) findViewById(R.id.ib_outside);
		mIntra = (ImageButton) findViewById(R.id.ib_intra);
		ib_exitinfo = (ImageButton) findViewById(R.id.ib_exitinfo);

		mTvOutSide = (TextView) findViewById(R.id.tv_outside);
		mTvIntra = (TextView) findViewById(R.id.tv_intra);
		tv_exitinfo = (TextView) findViewById(R.id.tv_exitinfo);

		tv_top_left_title = (TextView) findViewById(R.id.tv_top_left_title);
		tv_top_left_title.setText(stations.getStationName());

		exitInfo = LiteOrmServices.getExitInfoByStationID(liteOrm, stations.getStationId());
		fragments = new ArrayList<Fragment>();
		//站外
		outsideFrag = new OutsideAMapFragment();
		intraFrag = new InstationFragment();
		exitinfoFrag = new ExitInfosFragment();
		if (exitInfo.size() == 0) {
			ib_exitinfo.setVisibility(mViewPager.GONE);
			tv_exitinfo.setVisibility(mViewPager.GONE);
			//站内
			outsideFrag.setArguments(bundle);
			intraFrag.setArguments(bundle);
			fragments.add(outsideFrag);
			fragments.add(intraFrag);
		} else {
			ib_exitinfo.setVisibility(mViewPager.VISIBLE);
			tv_exitinfo.setVisibility(mViewPager.VISIBLE);

			//站内
			outsideFrag.setArguments(bundle);
			intraFrag.setArguments(bundle);
			exitinfoFrag.setArguments(bundle);

			fragments.add(outsideFrag);
			fragments.add(intraFrag);
			fragments.add(exitinfoFrag);
		}

		selectStationName();
	}

	//站内站外的图片背景设置
	private void imgDarken() {
		mOutside.setImageResource(R.drawable.navi_inside_deselect);
		mIntra.setImageResource(R.drawable.navi_exit_deselect);
		ib_exitinfo.setImageResource(R.drawable.exitinfo_defualt);
		mTvOutSide.setTextColor(getResources().getColor(R.color.activity_bg_color_gray));
		mTvIntra.setTextColor(getResources().getColor(R.color.activity_bg_color_gray));
		tv_exitinfo.setTextColor(getResources().getColor(R.color.activity_bg_color_gray));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ib_outside:
				setViewPagerItem(0);
				break;
			case R.id.ib_intra:
				setViewPagerItem(1);
				break;
			case R.id.ib_exitinfo:
				setViewPagerItem(2);
				break;
			case R.id.iv_top_left_back:
				this.finish();
				break;
			case R.id.add_site:
				if (isAddrecordSuccess) {
					DeleteSite();
				} else {
					AddStie();
				}
				break;
			default:
				break;
		}
	}

	//设置点击站内站外按钮的事件
	public void setViewPagerItem(int itemID) {
		imgDarken();
		if (itemID == 0) {
			mOutside.setImageResource(R.drawable.navi_inside_select);
			mTvOutSide.setTextColor(getResources().getColor(R.color.activity_bg_color_blue));
			mViewPager.setCurrentItem(itemID);
		} else if (itemID == 1) {
			mIntra.setImageResource(R.drawable.navi_exit_select);
			mTvIntra.setTextColor(getResources().getColor(R.color.activity_bg_color_blue));
			mViewPager.setCurrentItem(itemID);
		} else if (itemID == 2) {
			ib_exitinfo.setImageResource(R.drawable.exitinfo_onclick);
			tv_exitinfo.setTextColor(getResources().getColor(R.color.activity_bg_color_blue));
			mViewPager.setCurrentItem(itemID);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	}

	int itemId = -1;
	int itemIdNew = -1;

	//查询表里是否有该站点
	private void selectStationName() {
		List<SiteCollect> siteCollect = LiteOrmServices.getSiteCollectInfo(liteOrm_add);
		for (int i = 0; i < siteCollect.size(); i++) {
			String type = siteCollect.get(i).getType();
			if (type.equals("0")) {
				String stationName = siteCollect.get(i).getStationName();
				String stationName2 = stations.getStationName();
				if (stationName.equals(stationName2)) {
					id = siteCollect.get(i).getId();
					itemId = i;
				} else {
					itemIdNew = -1;
				}
			}
		}
		if (itemId != -1 && itemIdNew == -1) {
			isAddrecordSuccess = true;
			mImageAdd.setBackgroundResource(R.drawable.navi_collect_high);
		} else {
			isAddrecordSuccess = false;
			mImageAdd.setBackgroundResource(R.drawable.navi_collect_add);
		}
	}

	//添加收藏
	public void AddStie() {
		selectStationName();
		list_stationname = new ArrayList<SiteCollect>();
		final SiteCollect site = new SiteCollect();
		site.setStationId(String.valueOf(stations.getStationId()));
		site.setStationName(stations.getStationName());
		site.setType("0");
		list_stationname.add(site);
		liteOrm_add.cascade().insert(list_stationname);
		isAddrecordSuccess = true;
		mImageAdd.setBackgroundResource(R.drawable.navi_collect_high);
		Toast.makeText(getApplicationContext(), "收藏成功！", Toast.LENGTH_SHORT).show();
	}

	//取消收藏
	public void DeleteSite() {
		selectStationName();
		list_stationname = new ArrayList<SiteCollect>();
		final SiteCollect site = new SiteCollect();
		site.setId(id);
		site.setStationId(String.valueOf(stations.getStationId()));
		site.setStationName(stations.getStationName());
		site.setType("0");
		list_stationname.add(site);
		//SiteCollect siteCollectBySiteCollectId = LiteOrmServices.getSiteCollectBySiteCollectId(liteOrm_add, id);
		liteOrm_add.cascade().delete(list_stationname);
		isAddrecordSuccess = false;
		mImageAdd.setBackgroundResource(R.drawable.navi_collect_add);
		Toast.makeText(getApplicationContext(), "取消成功！", Toast.LENGTH_SHORT).show();
	}
}
//	public void AddStation() {
//		//判断是否登陆
//		SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserDatabase", Context.MODE_PRIVATE);
//		final String userName = sharedPreferences.getString("Loginame", "");
//		final String password = sharedPreferences.getString("PassWord", "");
//		if (userName.equals("")) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(ShowOutSideActivity.this);
//			builder.setTitle("请登录！");
//			builder.setMessage("确认是否登录？");
//			builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					dialog.cancel();
//				}
//			}).create().show();
//			builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					Intent intent = new Intent(ShowOutSideActivity.this, LoginActivity.class);
//					startActivity(intent);
//				}
//			}).create().show();
//		}else if (userName != null && password != null) {
//			if (isAddrecordSuccess) {
//				mImageAdd.setBackgroundResource(R.drawable.navi_collect_high);
//				map = new HashMap<String, String>();
//				map.put("loginname", userName);
//				map.put("password", password);
//				map.put("id", id);
//				CollectionRequest(Request.Method.POST, NumUtil.URL_COLLECTION_DELETE, map,true);
//			} else {
//				map = new HashMap<String, String>();
//				map.put("loginname", userName);
//				map.put("password", password);
//				map.put("type", "1");
//				map.put("rank", "1");
//				map.put("stationfrom",stations.getStationId()+"");
//				map.put("stationto","0");
//				CollectionRequest(Request.Method.POST, NumUtil.URL_COLLECTION_ADD, map,false);
//			}
//		}
//	}
//	//请求
//	private void CollectionRequest(int method, String url, final Map<String, String> map, final boolean isCancle) {
//		StringRequest stringRequest = new StringRequest(
//				method,
//				url,
//				new Response.Listener<String>() {
//					@Override
//					public void onResponse(String s) {
//						JSONObject jsonObject = null;
//						try {
//							jsonObject = new JSONObject(s);
//							String code = jsonObject.getString("code");
//							Log.e(TAG, "code" + "++++++++++" + code);
//							if (code.equals("1")) {
//								if(isCancle){
//									isAddrecordSuccess = false;
//									mImageAdd.setBackgroundResource(R.drawable.navi_collect_add);
//									Toast.makeText(getApplicationContext(), "取消成功！", Toast.LENGTH_LONG).show();
//								}else{
//									isAddrecordSuccess = true;
//									mImageAdd.setBackgroundResource(R.drawable.navi_collect_high);
//									Toast.makeText(getApplicationContext(), "收藏成功！", Toast.LENGTH_LONG).show();
//								}
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				}, new Response.ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError volleyError) {
//
//			}
//		}
//		) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				return map;
//			}
//		};
//		mQueue.add(stringRequest);
//	}