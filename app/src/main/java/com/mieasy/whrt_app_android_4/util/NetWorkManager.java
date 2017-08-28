package com.mieasy.whrt_app_android_4.util;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.CheckUpdateInfo;
import com.mieasy.whrt_app_android_4.services.CheckUpdateInfoProcessing;

import android.content.Context;

public class NetWorkManager {
	private String ServerUrl = NumUtil.APP_URL_SERVER_2;
	private String strUrl = ServerUrl+NumUtil.INDEX_JSON;
	private Gson gson = new Gson();
	private Context context;
	
	public NetWorkManager(Context context) {
		super();
		this.context = context;
	}

	/*
	 * 检测软件是否需要更新
	 */
	public void checkIndex(){
		RequestQueue requestQueue = ContentApplication.getInstance().mQueue;
		StringRequest request = new StringRequest(strUrl, new  Listener<String>(){

			@Override
			public void onResponse(String jsonStr) {
				contrastJson(jsonStr);
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				//Toast.makeText(mContext, arg0.toString(), Toast.LENGTH_SHORT).show();
			}
		});
		requestQueue.add(request);
		requestQueue.start();
	}

	/**
	 * 网络返回与本地解析后对比
	 * @param indexPage		网络返回的
	 * @param strJson		网络返回的jsonObect
	 */
	private void contrastJson(String strJson){
		String fileStr = FileUtil.readStorageFileString(NumUtil.INDEX_JSON);
		if(fileStr.length()!=0){//有文件  true
			//读取到本地数据，然后比对
			if((strJson.compareTo(fileStr))!=0){   //字符对比不相同
				//需要更新
				CheckUpdateInfo newCheckUpdateInfo = gson.fromJson(strJson, new TypeToken<CheckUpdateInfo>(){}.getType());
				CheckUpdateInfo oldCheckUpdateInfo = gson.fromJson(fileStr, new TypeToken<CheckUpdateInfo>(){}.getType());
				CheckUpdateInfoProcessing.checkUpdateInfoCheck(context,oldCheckUpdateInfo, newCheckUpdateInfo);
			}
		}else{ //没文件 flase   第一次访问的时候
			CheckUpdateInfo newCheckUpdateInfo = gson.fromJson(strJson, new TypeToken<CheckUpdateInfo>(){}.getType());
			CheckUpdateInfoProcessing.checkUpdateInfoCheck(context,newCheckUpdateInfo);
		}
	};
}