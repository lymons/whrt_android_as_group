package com.mieasy.whrt_app_android_4.act.nav;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.R.id;
import com.mieasy.whrt_app_android_4.R.string;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.entity.Directions;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.entity.Timetable;
import com.mieasy.whrt_app_android_4.event.ImageTouch;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.BitmapUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.util.OnTouchUtil;
import com.mieasy.whrt_app_android_4.util.VolleySingleton;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 站内
 * @author Administrator
 *
 */
public class InstationInfoFragment extends Fragment{
	private TextView tvStationName,tvLineOne,tvDirOfStartOne,tvDirOfStopOne,tvWorkingStartOneOfT,tvSundayStopOneOfT,tvWorkingStartOneOfF,tvSundayStopOneOfF;
	private TextView tvLineTwo,tvDirOfStartTwo,tvDirOfStopTwo,tvWorkingStartTwoOfT,tvSundayStopTwoOfT,tvWorkingStartTwoOfF,tvSundayStopTwoOfF;
	private ImageView imglineNumOne,imglineNumTwo;
	private LinearLayout linOne,linTwo;

	private LinearLayout linearLayout;
	private TextView mStationInfo;
	private View view;
	private ImageView mImageView;
	private DisplayMetrics outMetrics;

	private Stations stations;
	private Bitmap bmp;
	private int currentW;

	private RequestQueue mQueue;
	private LiteOrm liteOrm;
	private Bitmap bitmap;
	private Matrix matrix = new Matrix();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.map_nav_instation_info, null);
		liteOrm = ContentApplication.getInstance().liteOrm;
		mQueue = ContentApplication.getInstance().mQueue;
		getBundleInfo();
		initView();
		initTextView();
		setTextInfo();
		return view;
	}

	/**
	 * 初始化控件
	 */
	public void initView(){
		outMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		currentW = outMetrics.widthPixels;
		mImageView = (ImageView) view.findViewById(R.id.iv_instation_img);
		mImageView.setLayoutParams(new LinearLayout.LayoutParams(currentW,currentW));
		
//		matrix.setScale(0.1f, 0.1f); //开始先缩小
//		mImageView.setImageMatrix(matrix);
		
		mImageView.setOnTouchListener(new OnTouchUtil());
		new Thread(new Runnable() {
			@Override
			public void run() {
				String imageUril = NumUtil.APP_URL_SERVER_2+"image/"+stations.getStationId()+".jpg";
				bitmap = ImageLoader.getInstance().loadImageSync(imageUril,ContentApplication.getInstance().options1);
				Message msg = Message.obtain();
				if(bitmap!=null){
					msg.obj = bitmap;
					msg.what = 0x001;
				}else{
					msg.what = 0x002;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x001:
				bmp = BitmapUtil.getBitmap((Bitmap) msg.obj, currentW*2, currentW*2);
				matrix.setScale(0.5f, 0.5f);
				mImageView.setImageMatrix(matrix);
				mImageView.setImageBitmap(bmp);
				mImageView.setVisibility(View.VISIBLE);
				break;
			case 0x002:
				mImageView.setVisibility(View.GONE);
				break;
			}
		}
	};

	/**
	 * 设置文本信息
	 */
	private void setTextInfo(){
		//判断是否为换乘站
		if(stations.getIsTransfer()!=1){
			//不是换乘站
			tvStationName.setText(stations.getStationName());	
			linTwo.setVisibility(View.GONE);					//隐藏
			imglineNumTwo.setVisibility(View.GONE);
			int i = stations.getLineId();
			checkImageView(imglineNumOne,tvLineOne,i);
			//查找所有叫stationName的站点
			List<Directions> directions =LiteOrmServices.getDirectionsByStations(liteOrm, stations);	
			if(directions.size()!=0){
				//开始站点以及结束站点的名称
				tvDirOfStartOne.setText(directions.get(0).getDirectionName());
				tvDirOfStopOne.setText(directions.get(1).getDirectionName());
				//根据方向表的  站点ID  获取该站的时间表数据
				List<Timetable> timeTable0 = LiteOrmServices.getTimetableByStationID(liteOrm,directions.get(0).getStationId());
				List<Timetable> timeTable1 = LiteOrmServices.getTimetableByStationID(liteOrm,directions.get(1).getStationId());
				tvWorkingStartOneOfT.setText(timeTable0.get(0).getWeekdayFirst()+"-"+timeTable0.get(0).getWeekdayLast());
				tvSundayStopOneOfT.setText(timeTable0.get(0).getWeekendFirst()+"-"+timeTable0.get(0).getWeekendLast());
				tvWorkingStartOneOfF.setText(timeTable1.get(0).getWeekdayFirst()+"-"+timeTable1.get(0).getWeekdayLast());
				tvSundayStopOneOfF.setText(timeTable1.get(0).getWeekendFirst()+"-"+timeTable1.get(0).getWeekendLast());
			}
		}else{
			linTwo.setVisibility(View.VISIBLE);					//隐藏
			imglineNumTwo.setVisibility(View.VISIBLE);
			//是换乘站
			tvStationName.setText(stations.getStationName()+getResources().getString(string.station_huan));
			List<Stations> stationsList = LiteOrmServices.getStationsById(liteOrm,stations.getStationName());
			if(stationsList.size()!=0){
				checkImageView(imglineNumOne,tvLineOne,stationsList.get(0).getLineId());
				checkImageView(imglineNumTwo,tvLineTwo,stationsList.get(1).getLineId());
				List<Directions> directions0 =LiteOrmServices.getDirectionsByStations(liteOrm, stationsList.get(0));
				List<Directions> directions1 =LiteOrmServices.getDirectionsByStations(liteOrm, stationsList.get(1));
				if(directions0.size()!=0){
					tvDirOfStartOne.setText(directions0.get(0).getDirectionName());
					tvDirOfStopOne.setText(directions0.get(1).getDirectionName());
					List<Timetable> timeTable0 = LiteOrmServices.getTimetableByStationID(liteOrm,directions0.get(0).getStationId());
					List<Timetable> timeTable1 = LiteOrmServices.getTimetableByStationID(liteOrm,directions0.get(1).getStationId());
					tvWorkingStartOneOfT.setText(timeTable0.get(0).getWeekdayFirst()+"-"+timeTable0.get(0).getWeekdayLast());
					tvSundayStopOneOfT.setText(timeTable0.get(0).getWeekendFirst()+"-"+timeTable0.get(0).getWeekendLast());
					tvWorkingStartOneOfF.setText(timeTable1.get(0).getWeekdayFirst()+"-"+timeTable1.get(0).getWeekdayLast());
					tvSundayStopOneOfF.setText(timeTable1.get(0).getWeekendFirst()+"-"+timeTable1.get(0).getWeekendLast());
				}
				if(directions1.size()!=0){
					tvDirOfStartTwo.setText(directions1.get(0).getDirectionName());
					tvDirOfStopTwo.setText(directions1.get(1).getDirectionName());
					List<Timetable> timeTable0 = LiteOrmServices.getTimetableByStationID(liteOrm,directions1.get(0).getStationId());
					List<Timetable> timeTable1 = LiteOrmServices.getTimetableByStationID(liteOrm,directions1.get(1).getStationId());
					tvWorkingStartTwoOfT.setText(timeTable0.get(0).getWeekdayFirst()+"-"+timeTable0.get(0).getWeekdayLast());
					tvSundayStopTwoOfT.setText(timeTable0.get(0).getWeekendFirst()+"-"+timeTable0.get(0).getWeekendLast());
					tvWorkingStartTwoOfF.setText(timeTable1.get(0).getWeekdayFirst()+"-"+timeTable1.get(0).getWeekdayLast());
					tvSundayStopTwoOfF.setText(timeTable1.get(0).getWeekendFirst()+"-"+timeTable1.get(0).getWeekendLast());
				}
			}
		}
	}

	/**
	 * 根据站点的ID，设置站点的上方图标以及地铁线路
	 * @param imgView
	 * @param tvTxt
	 * @param i
	 */
	public void checkImageView(ImageView imgView,TextView tvTxt,int i){
		if(i==1){
			imgView.setImageResource(R.drawable.line_no01);
			tvTxt.setText("轨道交通一号线");
		}else if(i==2){
			imgView.setImageResource(R.drawable.line_no02);
			tvTxt.setText("轨道交通二号线");
		}else if(i==3){
			imgView.setImageResource(R.drawable.line_no03);
			tvTxt.setText("轨道交通三号线");
		}else if(i==4){
			imgView.setImageResource(R.drawable.line_no04);
			tvTxt.setText("轨道交通四号线");
		}
	}


	/**
	 * 网络获取图片
	 * @param bitName	图片名称
	 * @param bitUrl	图片URL
	 */
	private void getInitImageView(final String bitName,final String bitUrl) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				mQueue.add(new ImageRequest(bitUrl, 
						new Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap bit) {
						BitmapUtil.saveBitmap(view.getContext(), bit, bitName);
						Bitmap bm = BitmapUtil.getBitmap(BitmapFactory.decodeFile(view.getContext().getFilesDir()+"/"+bitName),currentW,currentW);
						mImageView.setImageBitmap(bm);
						mImageView.setOnTouchListener(new ImageTouch(bm, outMetrics, mImageView));
					}
				}, currentW, currentW, Config.ARGB_8888, 
						new Response.ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError arg0) {
						mImageView.setLayoutParams(new LinearLayout.LayoutParams(0,0));
						mImageView.setVisibility(View.GONE);
					}
				}));
			}
		}).start();
	}


	/**
	 * 获取网络图片
	 */
	private void getNetWorkImg(){
		/**
		 * 首先从本地查找图片
		 * 如果A服务器没有找到就找B服务器
		 * 1、没有就从网络上下载、
		 * 		网络上没有，就直接隐藏图片显示控件
		 * 2、有就显示
		 */
	}

	/**
	 * 获取Activity传递过来的参数
	 */
	private void getBundleInfo() {
		Bundle bundle =  getArguments();
		if(bundle!=null){
			stations = bundle.getParcelable(NumUtil.JUMP_BUNDLE);
		}
	}

	/**
	 * 初始化Text对象
	 */
	public void initTextView(){
		tvStationName = (TextView) view.findViewById(R.id.nav_export_station_name);	//站点名称
		imglineNumOne = (ImageView) view.findViewById(R.id.nav_export_station_transfer_one);  //线路图片1
		imglineNumTwo = (ImageView) view.findViewById(R.id.nav_export_station_transfer_two);  //线路图片2

		linOne = (LinearLayout) view.findViewById(id.nav_export_station_line_info_one);	//线路第一个模块
		tvLineOne = (TextView) view.findViewById(R.id.lin_one_name);							//轨道交通多少号号线
		tvDirOfStartOne = (TextView) view.findViewById(R.id.lin_one_start);							//该线的某方向
		tvDirOfStopOne = (TextView) view.findViewById(R.id.lin_one_stop);							//该线的某方向
		tvWorkingStartOneOfT = (TextView) view.findViewById(R.id.lin_one_start_gzr_sj);				//工作日开始时间
		tvSundayStopOneOfT = (TextView) view.findViewById(R.id.lin_one_start_sxr_sj);					//双休日开始时间
		tvWorkingStartOneOfF = (TextView) view.findViewById(R.id.lin_one_stop_gzr_sj);				//工作日开始时间
		tvSundayStopOneOfF = (TextView) view.findViewById(R.id.lin_one_stop_gzr_sj);					//双休日开始时间

		linTwo = (LinearLayout) view.findViewById(id.nav_export_station_line_info_two);			//线路第二个模块
		tvLineTwo = (TextView) view.findViewById(R.id.lin_two_name);							//轨道交通多少号号线
		tvDirOfStartTwo = (TextView) view.findViewById(R.id.lin_two_start);							//该线的某方向
		tvDirOfStopTwo = (TextView) view.findViewById(R.id.lin_two_stop);							//该线的某方向
		tvWorkingStartTwoOfT = (TextView) view.findViewById(R.id.lin_two_start_gzr_sj);				//工作日开始时间
		tvSundayStopTwoOfT = (TextView) view.findViewById(R.id.lin_two_start_sxr_sj);					//双休日开始时间
		tvWorkingStartTwoOfF = (TextView) view.findViewById(R.id.lin_two_stop_gzr_sj);				//工作日开始时间
		tvSundayStopTwoOfF = (TextView) view.findViewById(R.id.lin_two_stop_sxr_sj);					//双休日开始时间

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(bmp!=null){
			if(bmp.isRecycled()){
				bmp.recycle();
			}
		}
		if(bitmap!=null){
			if(bitmap.isRecycled()){
				bitmap.recycle();
			}
		}
	}
}