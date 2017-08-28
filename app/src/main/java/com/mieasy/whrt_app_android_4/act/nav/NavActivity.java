package com.mieasy.whrt_app_android_4.act.nav;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mieasy.whrt_app_android_4.act.login.LoginActivity;
import com.mieasy.whrt_app_android_4.view.ValuePicker;

import java.util.HashMap;
import java.util.Map;

import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.imp.StationCallBackInterface;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

//站内站外
public class NavActivity extends FragmentActivity{
	public static final String SELECTED_LEFT = "SELECTED_LEFT";
	public static final String SELECTED_RIGHT = "SELECTED_RIGHT";

	private ImageButton mImageButton;
	private TextView mTitle;
	private LiteOrm liteOrm;
	private Map<String, String[]> details;
	private ValuePicker vpTest;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_main);
		liteOrm = ContentApplication.getInstance().liteOrm;
		mTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTitle.setText(getResources().getString(R.string.block_nav));
		vpTest = (ValuePicker) findViewById(R.id.vpTest);
		vpTest.setStationInfo(LiteOrmServices.getStationName(liteOrm),liteOrm);

		Intent intent = getIntent();
		String leftValue = intent.getStringExtra(SELECTED_LEFT);
		String rightValue = intent.getStringExtra(SELECTED_RIGHT);

		vpTest.setLeftValue(leftValue);
		vpTest.setRightValue(rightValue);

		vpTest.initialize();
		vpTest.SetOnClickCallBack(new StationCallBackInterface() {

			@Override
			public void CallBack(Stations stations) {
				jumpActivity(stations);
			}
		});
		
		mImageButton = (ImageButton) findViewById(R.id.iv_top_left_back);
		mImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NavActivity.this.finish();
			}
		});
	}

	/**
	 * 界面跳转
	 * @param stations
	 */
	public void jumpActivity(Stations stations){
		if(stations!=null){
			Intent intent = new Intent(this,ShowOutSideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(NumUtil.JUMP_BUNDLE, stations);
			intent.putExtra(NumUtil.JUMP_INTENT, bundle);
			startActivity(intent);
		}
	}
}
