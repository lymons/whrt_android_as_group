package com.mieasy.whrt_app_android_4.util;

public class CharConvert {
	public static String intToStr(int num){
		String numChar[] = {"一","二","三","四"};
		String str = null;
		for(int i=0;i<numChar.length;i++){
			if((i+1)==num){
				str = numChar[i];
			}
		}
		return str;
	}
}