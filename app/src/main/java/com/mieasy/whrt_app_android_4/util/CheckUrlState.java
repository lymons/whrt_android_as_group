package com.mieasy.whrt_app_android_4.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckUrlState {
	private static String TAG="NetWork";
	
	public static boolean checkNetWorkStatus(Context context){
		boolean result;
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if ( netinfo !=null && netinfo.isConnected() ) {
			result=true;
			Log.i(TAG, "The net was connected" );
		}else{
			result=false;
			Log.i(TAG, "The net was bad!");
		}
		return result;
	}

	/**
	 * 判断url链接是否存在
	 * @param url
	 * @return true 链接存在；flase 链接不存在
	 */
	public static boolean checkURL(String url){
		boolean value=false;
		try {
			HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
			int code=conn.getResponseCode();
			System.out.println(">>>>>>>>>>>>>>>> "+code+" <<<<<<<<<<<<<<<<<<");
			if(code!=200){
				value=false;
			}else{
				value=true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 
	 * @param bool			数据是否有更新    true：有   ; flase：没有
	 * @param centerUrl		文件名前缀    比如：new/
	 * @param filename		文件名	      比如：1.json
	 * @return
	 */
	public static String checkServerURL(Context context,Boolean bool,String centerUrl,String filename){
		String str = "";
		if(bool){
			//网络获取数据到本地
			str = StringToList.readString(context,NumUtil.APP_URL_SERVER_2+centerUrl+filename, filename);
			if(!(str.length()>0)){
				str = StringToList.readString(context,NumUtil.APP_URL_SERVER_1+centerUrl+filename, filename);
			}
		}else{
			//从本地获取数据
			str = FileUtil.readLocalFileString(centerUrl,filename);
		}
		return str;
	}
}