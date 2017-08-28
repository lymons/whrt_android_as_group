package com.mieasy.whrt_app_android_4.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class InfoViewPager extends ViewPager {

	public InfoViewPager(Context context) {
		super(context);
	}

	public InfoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false; 
	}
	
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}
	
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}
	
	public boolean executeKeyEvent(KeyEvent event) {
		return false;
	}
}
