package com.mieasy.whrt_app_android_4.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileUtil {
	/**
	 * 读取本地Asset文件
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String readLocalAssetString(Context context,String fileName){
		try { 
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName)); 
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 下载文件
	 * @param context		上下文对象
	 * @param filename		文件名
	 * @param _urlStr		网络的URL
	 * @return
	 */
	public static boolean downloadDBFile(Context context,String filename,String _urlStr){
		boolean flag = false;
		File file = new File(context.getFilesDir()+"/"+filename);
		if(file.exists())
		{
			file.delete();
		}
		try {
			// 构造URL   
			URL url = new URL(_urlStr);
			// 打开连接   
			URLConnection con = url.openConnection();
			//获得文件的长度
			int contentLength = con.getContentLength();
			System.out.println("长度 :"+contentLength);
			// 输入流   
			InputStream is = con.getInputStream();  
			// 1K的数据缓冲   
			byte[] bs = new byte[1024];
			// 读取到的数据长度   
			int len;   
			// 输出的文件流   
			OutputStream os = new FileOutputStream(file);
			// 开始读取   
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}  
			// 完毕，关闭所有链接   
			os.close();  
			is.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 写入文件
	 *
	 * ConstParams.LOCALFILE为要写入的文件地址
	 */
	public static void saveFile(Context context,String log,String filename) {
		//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		//		Date date = new Date();
		//		String formateDate = format.format(date);
		String remotefilename = NumUtil.FILE_PATH+"/"+filename;
		File files = new File(remotefilename);
		try {
			if(!files.exists()){
				files.mkdirs();
			}
//			OutputStream os = new FileOutputStream(remotefilename);
			String[] str = remotefilename.split("/");
			OutputStream os =context.openFileOutput(str[str.length-1], context.MODE_PRIVATE);
			os.write(log.getBytes());
			os.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 保存文件到外部储存设备
	 * @param context
	 * @param log
	 * @param filename
	 */
	public static void saveStorageFile(Context context,String log,String filename){
		String remotefilename = Environment.getExternalStorageDirectory() +"/Whrt/cache/"+filename;
		File files = new File(remotefilename);
	    if (!files.exists()) {
	    	files.mkdir();
	    }
	    try {
	    	String[] str = remotefilename.split("/");
			OutputStream os =context.openFileOutput(str[str.length-1], context.MODE_PRIVATE);
			os.write(log.getBytes());
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取本地存储目录下的文件
	 * @param remoteFile
	 * @return
	 */
	public static String readStorageFileString(String remoteFile){
		String log = "";
		String dir = Environment.getExternalStorageDirectory() +"/Whrt/cache/"+remoteFile;
		try {
			File file = new File(dir);
			FileInputStream is = new FileInputStream(dir);
			int size = (int) file.length();
			byte[] bytes = getBytes(is,size);
			String content = new String(bytes, "UTF-8");
			log = content;
			is.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return log;
	}
	
	/**
	 * 读取应用程序的文件目录下的文件
	 * @param remoteFile  文件名
	 * @return
	 */
	public static String readLocalFileString(String remoteFile){
		String log = "";
		String dir = NumUtil.FILE_PATH+"/"+remoteFile;
		try {
			File file = new File(dir);
			FileInputStream is = new FileInputStream(dir);
			int size = (int) file.length();
			byte[] bytes = getBytes(is,size);
			String content = new String(bytes, "UTF-8");
			log = content;
			is.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return log;
	}

	/**
	 * 读取应用程序的文件目录下子文件目录下的文件
	 * @param centerUrl		文件名前缀     new/
	 * @param remoteFile	文件名	    1.json
	 * @return
	 */
	public static String readLocalFileString(String centerUrl,String remoteFile){
		String log = "";
		String dir = NumUtil.FILE_PATH+"/"+centerUrl+remoteFile;
		try {
			File file = new File(dir);
			FileInputStream is = new FileInputStream(dir);
			int size = (int) file.length();
			byte[] bytes = getBytes(is,size);
			String content = new String(bytes, "UTF-8");
			log = content;
			is.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return log;
	}


	/**
	 * 删除应用程序的文件目录下的文件
	 * @param remoteFile  文件名
	 * @return
	 */
	public static boolean removeLocalFileString(String remoteFile){
		boolean log = false;
		String dir = NumUtil.FILE_PATH+"/"+remoteFile;
		File file = new File(dir);
		if(!file.exists()){
			log = file.delete();
		}else{
			log = true;
		}
		return log;
	}

	/**
	 * 删除应用程序database的文件目录下的文件
	 * @return
	 */
	public static boolean removeOldDBString(File file){
		boolean log = false;
		if(!file.exists()){
			log = file.delete();
		}else{
			log = true;
		}
		return log;
	}


	private static byte[] getBytes(InputStream inputStream,int size) {
		byte[] bytes = new byte[size];
		try {
			int readBytes = inputStream.read(bytes);
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 复制assets下数据库到data/data/packagename/databases
	 * @param context
	 * @throws IOException
	 */
	public static Boolean copyDBToDatabases(Context context,File files){
		String outStr = files.getPath()+"/"+NumUtil.DB_NAME;
		InputStream myInput = null;
		OutputStream myOutput = null;
		if(!files.exists()){
			try {
				files.mkdirs();
				myInput = context.getAssets().open(NumUtil.DB_NAME);
				myOutput = new FileOutputStream(outStr);

				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}finally {
				try {
					myOutput.flush();
					myOutput.close();
					myInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		File f=new File(outStr);
		Log.i("DB","DB filename="+outStr+"; size="+f.length());
		return true;
	}
	
	/**
	 * 复制files下数据库到data/data/packagename/databases
	 * @param context		上下文对象
	 * @param filename		新数据库文件名
	 * @return
	 */
	public static Boolean copyfileDBToDatabases(Context context,String filename){
		File dbfiles = new File("/data/data/com.mieasy.whrt_app_android_4/databases");
		String outStr = dbfiles.getPath()+"/"+filename;			//database目录下的.db文件
		String filePath = context.getFilesDir()+"/"+ filename;	//数据库文件filse目录下的路径
		InputStream myInput = null;								//输入流
		OutputStream myOutput = null;							//输出流
		if(dbfiles.exists()){
			try {
				dbfiles.mkdirs();
				myInput = new FileInputStream(filePath);
				myOutput = new FileOutputStream(outStr);
				
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}finally {
				try {
					myOutput.flush();
					myOutput.close();
					myInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 判断文件的路径是否存在
	 * @param context
	 * @param str
	 * @return  true是就是存在，false就是不存在
	 */
	public static boolean checkFilePathIsExist(Context context,String str){
		boolean isExist = true;
		File file = new File(context.getFilesDir()+"/"+str);
		if(!file.exists()){
			//不存在
			isExist = false;
		}
		return isExist;
	}
}