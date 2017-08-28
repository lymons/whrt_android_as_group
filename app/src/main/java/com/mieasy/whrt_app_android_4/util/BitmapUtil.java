package com.mieasy.whrt_app_android_4.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class BitmapUtil {
	/**
	 * 读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/***
	 * 根据资源文件获取Bitmap
	 * 
	 * @param context
	 * @param drawableId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int drawableId,
			int screenWidth, int screenHight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;//设置画布为白色
		options.inInputShareable = true;
		/*
		 * *  
		 * 如果 inPurgeable 设为True的话表示使用BitmapFactory创建的Bitmap 
		 * 用于存储Pixel的内存空间在系统内存不足时可以被回收， 
		 * 在应用需要再次访问Bitmap的Pixel时（如绘制Bitmap或是调用getPixel）， 
		 * 系统会再次调用BitmapFactory decoder重新生成Bitmap的Pixel数组。  
		 * 为了能够重新解码图像，bitmap要能够访问存储Bitmap的原始数据。 
		 **/
		options.inPurgeable = true;
		InputStream stream = context.getResources().openRawResource(drawableId);
		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		return getBitmap(bitmap, screenWidth, screenHight);
	}

	/***
	 * 根据资源文件名获取Assets文件夹下Bitmap
	 * 
	 * @param context
	 * @return
	 */
	public static Bitmap ReadBitmapByAssetFileName(Context context, String fileName,int screenWidth, int screenHight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;//设置画布为白色
		options.inInputShareable = true;
		options.inPurgeable = true;
		InputStream stream = null;
		try {
			stream = context.getResources().getAssets().open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		return getBitmap(bitmap, screenWidth, screenHight);
	}

	/***
	 * 等比例压缩图片
	 * 
	 * @param bitmap
	 * @param screenWidth
	 * @param screenHight
	 * @return
	 */
	public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,int screenHight) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Log.e("图片尺寸", "图片宽度screenWidth=" + w + ",screenWidth=" + screenWidth);
		Matrix matrix = new Matrix();
		float scale = (float) screenWidth / (float)w;
		float scale2 = (float) screenHight / (float)h;
		// scale = scale < scale2 ? scale : scale2;

		// 保证图片不变形.
		matrix.postScale(scale, scale);
		// w,h是原图的属性.
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	/***
	 * 保存图片至SD卡
	 * 
	 * @param bm
	 * @param url
	 * @param quantity
	 */
	private static int FREE_SD_SPACE_NEEDED_TO_CACHE = 1;
	private static int MB = 1024 * 1024;
	public final static String DIR = NumUtil.FILE_PATH;

	public static void saveBmpToSd(Bitmap bm, String url, int quantity){
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			return;
		}
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
			return;
		String filename = string2MD5(url);

		// 目录不存在就创建
		File dirPath = new File(DIR);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		File file = new File(DIR + "/" + filename);
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, quantity, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 获取SD卡图片
	 * 
	 * @param url			图片URL
	 * @param quantity		图片质量
	 * @return
	 */
	public static Bitmap GetBitmap(String url, int quantity) {
		InputStream inputStream = null;
		String filename = "";
		Bitmap map = null;
		URL url_Image = null;
		String LOCALURL = "";
		if (url == null)
			return null;
		try {
			filename = string2MD5(url);
		} catch (Exception err) {
		}
		//		LOCALURL = URLEncoder.encode(filename);
		String pathStr = DIR+ "/" + filename;
		File file = new File(pathStr);
		if (file.exists()) {
			map = BitmapFactory.decodeFile(DIR + "/" + filename);
		} else {
			try {
				url_Image = new URL(url);
				inputStream = url_Image.openStream();
				map = BitmapFactory.decodeStream(inputStream);
				// url = URLEncoder.encode(url, "UTF-8");
				if (map != null) {
					saveBmpToSd(map, LOCALURL, quantity);
				}
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return map;
	}

	/***
	 * 判断图片是存在
	 * 
	 * @param url
	 * @return
	 */
	public static boolean Exist(String url) {
		File file = new File(DIR + url);
		return file.exists();
	}

	/** * 计算sdcard上的剩余空间 * @return */
	private static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}


	//图片url加密
	public static String string2MD5(String inStr){  
		MessageDigest md5 = null;  
		try{  
			md5 = MessageDigest.getInstance("MD5");  
		}catch (Exception e){  
			System.out.println(e.toString());  
			e.printStackTrace();  
			return "";  
		}  
		char[] charArray = inStr.toCharArray();  
		byte[] byteArray = new byte[charArray.length];  

		for (int i = 0; i < charArray.length; i++)  
			byteArray[i] = (byte) charArray[i];  
		byte[] md5Bytes = md5.digest(byteArray);  
		StringBuffer hexValue = new StringBuffer();  
		for (int i = 0; i < md5Bytes.length; i++){  
			int val = ((int) md5Bytes[i]) & 0xff;  
			if (val < 16)  
				hexValue.append("0");  
			hexValue.append(Integer.toHexString(val));  
		}  
		return hexValue.toString();  
	}  


	/**
	 * 从Assets中读取图片
	 */
	public static Bitmap getImageFromAssetsFile(Context context,String fileName)
	{
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try
		{
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * @param context
	 *@param resId
	 * @return
	 */  
	public static Bitmap readBitMap(Context context, int resId,String filename){  
		BitmapFactory.Options opt = new BitmapFactory.Options();  
		opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		opt.inPurgeable = true;  
		opt.inInputShareable = true;  
		//获取资源图片  
		//		InputStream is = context.getResources().openRawResource(resId);  
		InputStream is = null;
		try {
			is = context.getResources().getAssets().open(filename);
			return BitmapFactory.decodeStream(is,null,opt);  
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 保存图片文件到
	 * @param context	对象
	 * @param bm		Bitmap
	 * @param picName	文件名
	 */
	public static void saveBitmap(Context context,Bitmap bm,String picName) { 
		Log.e("Bitmap", "保存图片"); 
		File f = new File(context.getFilesDir()+"/"+picName); 
		if (f.exists()) { 
			f.delete(); 
		} 
		try { 
			FileOutputStream out = new FileOutputStream(f); 
			bm.compress(Bitmap.CompressFormat.PNG, 90, out); 
			out.flush(); 
			out.close(); 
			Log.i("Bitmap", "已经保存"); 
		} catch (FileNotFoundException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
	}
	
	/**
	 * 根据图片URL获取图片
	 * @param strUrl
	 * @return
	 */
	public static Bitmap getBitmapFromNetwork(String strUrl) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			URL url = new URL(strUrl);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(6000);		//设置超时
			conn.setDoInput(true);
			conn.setUseCaches(false);			//不缓存
			conn.connect();
			is = conn.getInputStream();			//获得图片的数据流
			bitmap = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bitmap!=null){
				try {
					is.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return bitmap;
	}
	
	/**
	 * 图片保存到系统图库
	 * @param context
	 * @param bmp
	 */
	public static void saveImageToGallery(Context context, Bitmap bmp) {
	    // 首先保存图片
	    File appDir = new File(Environment.getExternalStorageDirectory(), "Whrt");
	    if (!appDir.exists()) {
	        appDir.mkdir();
	    }
	    String fileName = System.currentTimeMillis() + ".jpg";
	    File file = new File(appDir, fileName);
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        bmp.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
		}
	    
	    // 其次把文件插入到系统图库
	    try {
	        MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
	        Toast.makeText(context, "图片已保存到"+file.getPath().toString(), Toast.LENGTH_SHORT).show();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    // 最后通知图库更新
	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://Whrt/")));
	    //通知系统扫描图片
	    MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + Uri.parse("file://"+ Environment.getExternalStorageDirectory())},null,null);
	}
}