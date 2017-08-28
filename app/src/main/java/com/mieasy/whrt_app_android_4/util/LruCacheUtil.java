package com.mieasy.whrt_app_android_4.util;

import java.util.List;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class LruCacheUtil extends LruCache<String, List<Object>>{
	private LruCache<String, List<Object>> mListCache;
	private LruCache<String,List<Bitmap>> mBitmapListCache;
	
	public LruCacheUtil(int maxSize) {
		super(maxSize);
		int size = 80000;  //缓存大小，Byte
		mListCache = new LruCache<String, List<Object>>(1 * 1024 * 1024);
    	mBitmapListCache = new LruCache<String,List<Bitmap>>(8 * 1024 * 1024);
	}
 
    /**
     * 存入缓存列表
     * 
     * @param key
     * @param value
     */
    public void addJsonLruCache(String key, List<Object> value) {
    	mListCache.put(key, value);
    }
 
    public void addBitmapLruCache(String key,List<Bitmap> valueBitmap) {
    	mBitmapListCache.put(key, valueBitmap);
    }
 
    /**
     * 从缓存列表中拿出来
     * 
     * @param key
     * @return
     */
    public List<Object> getJsonLruCache(String key) {
        return mListCache.get(key);
    }
 
    public List<Bitmap> getBitmapLruCache(String key) {
        return mBitmapListCache.get(key);
    }
}