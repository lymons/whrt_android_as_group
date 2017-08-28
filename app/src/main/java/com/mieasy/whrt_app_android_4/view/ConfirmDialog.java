package com.mieasy.whrt_app_android_4.view;

import com.mieasy.whrt_app_android_4.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ConfirmDialog extends Dialog {

	private Context context;

	private LayoutParams params;
	private int ScreenW,ScreenH; 
	private String standName,startPoint,endPoint;
	
	private TextView tvStationName,tvStationStart,tvStationStop,tvStationOutside,tvStationIntra;
	private int[] lineNum;

	private ClickListenerInterface clickListenerInterface;

	public interface ClickListenerInterface {
		public void doStart();
		public void doStop();
		public void doOutside();
		public void doIntra();
	}

	public ConfirmDialog(Context context, String standName, String startPoint, String endPoint,int[] lineNum) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.standName = standName;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.lineNum = lineNum;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.map_boot_dialog, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);

		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		ScreenW = d.widthPixels;
		ScreenH = d.heightPixels;

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = ScreenW/2; // 高度设置为屏幕的0.6
		lp.height = ScreenH/2;
		dialogWindow.setAttributes(lp);
		
		tvStationName = (TextView) findViewById(R.id.station_name);
		tvStationName.setText(standName);
		tvStationStart =(TextView) findViewById(R.id.start_station);
		tvStationStop = (TextView) findViewById(R.id.stop_station);
		tvStationOutside = (TextView) findViewById(R.id.station_outside_info);
		tvStationIntra = (TextView) findViewById(R.id.station_intra_info);
		
		tvStationStart.setOnClickListener(new clickListener());
		tvStationStop.setOnClickListener(new clickListener());
		tvStationOutside.setOnClickListener(new clickListener());
		tvStationIntra.setOnClickListener(new clickListener());
	}
	
	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.start_station:		//设置起点
				clickListenerInterface.doStart();
				break;
			case R.id.stop_station:			//设置终点
				clickListenerInterface.doStop();
				break;
			case R.id.station_outside_info://出口信息
				clickListenerInterface.doOutside();
				break;
			case R.id.station_intra_info:  //站外信息
				clickListenerInterface.doIntra();
				break;
			default:
				break;
			}
		}
	}
}
