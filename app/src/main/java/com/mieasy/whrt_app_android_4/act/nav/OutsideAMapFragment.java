package com.mieasy.whrt_app_android_4.act.nav;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.entity.Map;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.CharConvert;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 高德地图站外引导
 * @author Administrator
 *
 */
public class OutsideAMapFragment extends Fragment
implements InfoWindowAdapter
{
	private View view;
	private LiteOrm liteOrm;
	private Stations stations;
	private Map map;

	private MapView mapView;
	private AMap aMap;
	private UiSettings uiSetting;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.map_nav_outside, null);
		mapView = (MapView) view.findViewById(R.id.amap_ouside_map);
		mapView.onCreate(savedInstanceState); // 必须要写
		liteOrm = ContentApplication.getInstance().liteOrm;
		getBundleInfo();
		initView();
		return view;
	}

	//获取传值
	public void getBundleInfo(){
		Bundle bundle =  getArguments();
		if(bundle!=null){
			stations = bundle.getParcelable(NumUtil.JUMP_BUNDLE);
			List<Map> mapList = LiteOrmServices.getMapByStation(liteOrm, stations);
			if(mapList.size()!=0){
				map = mapList.get(0);

				Log.i("Map Info1:","La:"+map.getAmapLatitude()+"; Long="+map.getAmapLongitude());

			}
		}
	}

	/**
	 * 地图定位
	 */
	private void initView() {
		if(aMap==null){
			aMap = mapView.getMap();
			aMap.setInfoWindowAdapter(this);		// 设置自定义InfoWindow样式
			uiSetting = aMap.getUiSettings();
			uiSetting.setLogoPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
			uiSetting.setCompassEnabled(true);
		}
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		LatLng ll = null;
		if(map!=null){
			//定位武汉的坐标
			ll = new LatLng(map.getAmapLongitude(),map.getAmapLatitude());
			Log.i("map","La:"+map.getAmapLatitude()+"; Long="+map.getAmapLongitude());
            Log.e("map","map:"+ll);
		}else{
			ll = new LatLng(30.50576,114.397169);		//定位武汉的坐标
		}
		CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(ll, 12, 44, 0));  //倾斜地图以及缩放大小
		aMap.animateCamera(update,500, null);//添加改变动画
//		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 20);  //地图缩放大小
//		aMap.moveCamera(update);
		Message msg = Message.obtain();
		msg.obj = ll;
		handler.sendMessage(msg);
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			LatLng llg = (LatLng) msg.obj;
			initMarker(llg);
		}
	};
	
	/**
	 * 设置标注
	 */
	public void initMarker(LatLng ll){
		MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f)
				.position(ll)
				.title(CharConvert.intToStr(stations.getLineId())+"号线")    //标题
				.snippet(stations.getStationName()+" 地铁站")
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.map_marker)))    //图片
				.draggable(true)
				.period(50);
		
		ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
		markerOptionlst.add(markerOption);
		List<Marker> markerlst = aMap.addMarkers(markerOptionlst, true);
		markerlst.get(0).showInfoWindow();
		markerlst.get(0)
		.setZIndex(0.5f);
//		.setRotateAngle(-30)
		;
		markerlst.get(0).setFlat(false);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public View getInfoContents(Marker marker) {
		View infoContent = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
		render(marker, infoContent);
		return infoContent;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
		render(marker, infoWindow);
		return infoWindow;
	}
	
	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
		//第二个样式
		((ImageView) view.findViewById(R.id.badge)).setImageResource(R.drawable.logo);
		//第三个样式
//		ImageView imageView = (ImageView) view.findViewById(R.id.badge);
//		imageView.setImageResource(R.drawable.badge_wa);
		//设置标题
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			//复合文本
//			SpannableString titleText = new SpannableString(title);
			SpannableString titleText = new SpannableString("轨道交通"+stations.getLineId()+"号线 "+stations.getStationName()+"");
			//设置不同颜色、字体等的文字
//			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,titleText.length(), 0);
//			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
	}
}