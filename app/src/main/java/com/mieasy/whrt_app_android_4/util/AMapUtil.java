package com.mieasy.whrt_app_android_4.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AMapUtil {
	public static String sHA1(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

			byte[] cert = info.signatures[0].toByteArray();

			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] publicKey = md.digest(cert);
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < publicKey.length; i++) {
				String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
				if (appendString.length() == 1)
					hexString.append("0");
				hexString.append(appendString);
				hexString.append(":");
			}
			return hexString.toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
