package com.mieasy.whrt_app_android_4.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mieasy.whrt_app_android_4.app.ContentApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MachineInfoManager {
	private static Context context;
	private String TAG = "MachineInfo";

	private String versionSystem = "";
	private String versionModel = "";
	private String versionSDK = "";
	private String versionName = "";
	private String versionUid = "";
	private static UUID uuid;

	public MachineInfoManager(Context context) {
		super();
		this.context = context;
	}

	public void checkWork() {
		//		String url = NumUtil.APP_URL_SERVER_2+"status";
		//if (versionUid==null || versionUid.length()==0) return;
		String url = NumUtil.STATUS;
		RequestQueue requestQueue = ContentApplication.getInstance().mQueue;
		StringRequest request = new StringRequest(Request.Method.POST,url, new  Listener<String>(){

			@Override
			public void onResponse(String s) {
				//				Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
			}

		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				//				Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("appversion",versionName);
				map.put("os","Android "+versionSystem);
				map.put("machine",versionModel);
				map.put("uid",versionUid);
				return map;
			}
		}; 
		request.setTag("volleystatuspost");
		requestQueue.add(request);
		requestQueue.start();
	}

	//初始化数据
	public void initInfo(){
		try {
			versionSystem = android.os.Build.VERSION.RELEASE;  //系统版本
			versionModel = android.os.Build.MODEL; 			//获取手机型号
			versionSDK = android.os.Build.VERSION.SDK;		//SDK版本
			versionName = context.getPackageManager().getPackageInfo("com.mieasy.whrt_app_android_4", 0).versionName;
			versionUid = getId();
			Log.e(TAG,"versionUid:"+versionUid);
			checkWork();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getId(){
//		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//		String c = "";
//		try {
//			c = wifi.getConnectionInfo().getMacAddress();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//			return wifi.getConnectionInfo().getMacAddress();
//		}
//		return wifi.getConnectionInfo().getMacAddress();
		//TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String info = " ";
		if (android.os.Build.SERIAL != null) {
			Log.e(TAG,"seril:"+android.os.Build.SERIAL);
			return android.os.Build.SERIAL;
		} else {
		 return getMac();
		}
	}

    //得到MAC地址的方法
	public static String getMac() {
		String macSerial = "";
		try {
			Process pp = Runtime.getRuntime().exec(
					"cat /sys/class/net/wlan0/address");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			String line;
			while ((line = input.readLine()) != null) {
				macSerial += line.trim();
			}

			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return macSerial;
	}

	public String getDeviceId() {
		StringBuilder deviceId = new StringBuilder();
		// 渠道标志
		deviceId.append("a");

		try {
			//wifi mac地址
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			String wifiMac = info.getMacAddress();
			if(wifiMac.length()!=0){
				deviceId.append("wifi");
				deviceId.append(wifiMac);
				return deviceId.toString();
			}

			//IMEI（imei）
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tm.getDeviceId();
			if(imei.length()!=0){
				deviceId.append("imei");
				deviceId.append(imei);
				return deviceId.toString();
			}

			//序列号（sn）
			String sn = tm.getSimSerialNumber();
			if(sn.length()!=0){
				deviceId.append("sn");
				deviceId.append(sn);
				return deviceId.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceId.toString();

	}


	/**
	 * 得到全局唯一UUID
	 */
	//	public String getUUID(){
	//		SharedPreferences mShare = getSysShare(context, "sysCacheMap");
	//		if(mShare != null){
	//			uuid = mShare.getString("uuid", "");
	//		}
	//
	//		if(isEmpty(uuid)){
	//			uuid = UUID.randomUUID().toString();
	//			saveSysMap(context, "sysCacheMap", "uuid", uuid);
	//		}
	//
	//		PALog.e(tag, "getUUID : " + uuid);
	//		return uuid;
	//	}
}