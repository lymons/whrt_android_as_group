package com.mieasy.whrt_app_android_4.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FromatDateUtil {
	/**
	 * 日期差
	 * @param str1	当前获取的时间戳
	 * @param str2	本地读取的时间戳
	 * @return
	 */
	public static boolean getDateDifference(String str1,String str2){
		boolean flag = false;
		SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date d1 = df.parse(str1);
			Date d2 = df.parse(str2);
			if(Math.abs(((d1.getTime() - d2.getTime()))) >=0) {
				flag = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}
}