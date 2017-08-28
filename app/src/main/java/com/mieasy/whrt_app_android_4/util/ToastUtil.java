package com.mieasy.whrt_app_android_4.util;

import com.amap.api.services.core.AMapException;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}
	
}