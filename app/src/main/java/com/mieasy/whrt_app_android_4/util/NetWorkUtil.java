package com.mieasy.whrt_app_android_4.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class NetWorkUtil {
	/**
	 * 检测网络资源是否存在　
	 * 
	 * @param strUrl
	 * @return
	 */
	public static boolean isNetFileAvailable(String strUrl) {
		InputStream netFileInputStream = null;
		try {
			URL url = new URL(strUrl);
			URLConnection urlConn = url.openConnection();
			netFileInputStream = urlConn.getInputStream();
			if (null != netFileInputStream) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (netFileInputStream != null)
					netFileInputStream.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 判断链接是否有效
	 * 输入链接
	 * 返回true或者false
	 */
	public static boolean isValid(String strLink) {
		URL url;
		try {
			url = new URL("http://whrt.office.mieasy.com/apk.json");
			HttpURLConnection connt = (HttpURLConnection)url.openConnection();
			connt.setRequestMethod("HEAD");
			String strMessage = connt.getResponseMessage();
			if (strMessage.compareTo("Not Found") == 0) {
				return false;
			}
			connt.disconnect();
		} catch (Exception e) {
			return false;
		}
		return true;
	} 
}