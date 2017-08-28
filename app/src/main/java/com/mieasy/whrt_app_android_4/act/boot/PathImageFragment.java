package com.mieasy.whrt_app_android_4.act.boot;

import java.util.List;

import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.Point;
import com.mieasy.whrt_app_android_4.entity.Map;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.entity.change.PathInfoChange;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.services.PathInfoChangeManager;
import com.mieasy.whrt_app_android_4.util.BitmapUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.view.DragImageView;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Message;


public class PathImageFragment extends Fragment{
	private ViewTreeObserver viewTreeObserver;
	private int window_width, window_height;// 控件宽度
	private DragImageView dragImageView;// 自定义控件
	private LinearLayout mLinearLayout;
	private int state_height;// 状态栏的高度
	private Bitmap bmp,bmpStart,bmpStop,bmpExchange;
	
	private Stations startStation,stopStation;
	private LiteOrm liteOrm;
	private Gson gson;
	private View view;

	private Handler hander = new Handler(){
		public void handleMessage(Message msg) {
//			dragImageView.setMapLatLon();  			//设置数据到dragImageView中
			if(msg.what!=0){		//获取到了数据
				dragImageView.setPathInfo((List<PathInfoChange>)msg.obj);
				dragImageView.setStartPoint(getPointByStations(startStation));
				dragImageView.setStopPoint(getPointByStations(stopStation));
				dragImageView.test();
			}else{					//没有获取到数据
				Toast.makeText(view.getContext(), "获取线路失败，请重新选择起点终点！", 3000).show();
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.map_boot, container,false);
		WindowManager manager = getActivity().getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();
		getActivity().getWindow().setLayout(window_width,window_height-10);
		getStartStopPoint();
		initDragImageView();
		setPointDot();
		return view;
	}
	
	//设置点和线
	private void setPointDot() {
		liteOrm = ContentApplication.getInstance().liteOrm;
		gson = new Gson();
		new Thread(new mRunnable()).start();
	}
	
	/**
	 * 异步获取数据库中的数据
	 * @author Administrator
	 */
	class mRunnable implements Runnable{
		public void run() {
			Message msg = Message.obtain();
			try {
				PathInfoChangeManager.setColorMap(liteOrm);
				List<PathInfoChange> pathInfoChangeList= PathInfoChangeManager.getAllPathInfo(liteOrm, startStation, stopStation);
				if(pathInfoChangeList.size()>0){
					//数据转化成功
					msg.what = 1;
					msg.obj = pathInfoChangeList;
				}else{
					msg.what = 0;
				}
				hander.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//获取站点信息
	private void getStartStopPoint() {
		Bundle bundle = getArguments();
		if(bundle!=null){
			startStation = bundle.getParcelable(NumUtil.STATIONS_START);
			stopStation = bundle.getParcelable(NumUtil.STATIONS_STOP);
		}
	}
	
	/**
	 * 初始化Image
	 */
	public void initDragImageView(){
		mLinearLayout =(LinearLayout) view.findViewById(R.id.lin_main_top_title);
		mLinearLayout.setVisibility(View.GONE);

		LinearLayout ll_selectorsite =(LinearLayout)view.findViewById(R.id.ll_selectorsite);
		ImageView img_latelySite = (ImageView) view.findViewById(R.id.img_latelySite);

		ll_selectorsite.setVisibility(View.INVISIBLE);
		img_latelySite.setVisibility(View.INVISIBLE);
		/** 获取可見区域高度 **/
		WindowManager manager = getActivity().getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();

		dragImageView = (DragImageView) view.findViewById(R.id.div_main);
		if(bmp==null){
			bmp = BitmapUtil.ReadBitmapByAssetFileName(view.getContext(), "map_gray.jpg",2000, 2000);
			if(bmpStart==null){
				bmpStart = BitmapUtil.ReadBitmapById(view.getContext(), R.drawable.icon_st,96,96);
				if(bmpStop==null){
					bmpStop = BitmapUtil.ReadBitmapById(view.getContext(), R.drawable.icon_en,96,96);
					if(bmpExchange==null){
						bmpExchange = BitmapUtil.ReadBitmapById(view.getContext(), R.drawable.huanchengbiaoji,96,96);
					}
				}
			}
		}
		// 设置图片
		dragImageView.setStopImageBitmap(bmpStop);				//终点站
		dragImageView.setStartImageBitmap(bmpStart);			//起点站
		dragImageView.setExchangeImageBitmap(bmpExchange);		//中转站Image
		dragImageView.setImageBitmap(bmp);						//主图

		/** 测量状态栏高度 **/
		viewTreeObserver = dragImageView.getViewTreeObserver();
		viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				if (state_height ==0) {
					// 获取状况栏高度
					Rect frame = new Rect();
					getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
					state_height = frame.top;
					dragImageView.setScreen_H(window_height-state_height-700);
					dragImageView.setScreen_W(window_width);
				}
			}
		});
	}

	/**
	 * 根据站点详细信息获取站点的横纵坐标
	 * @param stations
	 * @return
	 */
	private Point getPointByStations(Stations stations){
		List<Map> mapList = LiteOrmServices.getMapByStation(liteOrm, stations);
		Point point = new Point();
		if(mapList.size()!=0){
			point.setPointX((int)(mapList.get(0).getAmapX()+0));
			point.setPointY((int)(mapList.get(0).getAmapY()+0));
		}
		return point;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(bmp!=null||bmpStart!=null||bmpStop!=null){
			if(bmp.isRecycled())
				bmp.recycle();
			if(bmpStart.isRecycled())
				bmpStart.recycle();
			if(bmpStop.isRecycled())
				bmpStop.recycle();
		}
	}
}