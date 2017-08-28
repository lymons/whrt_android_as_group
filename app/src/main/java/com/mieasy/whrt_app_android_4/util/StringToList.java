package com.mieasy.whrt_app_android_4.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mieasy.whrt_app_android_4.bean.Translation;

import android.content.Context;

public class StringToList {
	/**
	 * 访问URL转化返回为我们需要的数据
	 * @param is
	 * @return
	 */
	public static String readString(Context context,String url,String filename) {
		InputStreamReader isr;
		String result="";
		try {
			String line="";
			isr = new InputStreamReader(new URL(url).openStream(),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while((line=br.readLine())!=null){
				result+=line;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(result.length()>0){
			if(filename!=null){
				FileUtil.saveStorageFile(context,result, filename);
			}
		}
		return result;
	}

	/**
	 * get请求
	 * @param url        URL
	 * @param filename   保存的文件名
	 * @return
	 */
	public static String readStringDoGet(String url,String filename){
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse;
		String result = null;
		try {
			httpResponse = new DefaultHttpClient().execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{
				//第三步，使用getEntity方法活得返回结果
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}