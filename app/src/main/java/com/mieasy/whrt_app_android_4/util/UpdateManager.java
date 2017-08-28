package com.mieasy.whrt_app_android_4.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.UpdateApkInfo;
import com.mieasy.whrt_app_android_4.view.UpdateDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateManager {
	//下载状态
	private boolean mIsCache = false;
	private static final int DOWNLOADING = 1;
	private static final int DOWNLOAD_FINSH = 2;
	//进度条控件
	private ProgressDialog progressDialog;
	//APK的保存路径
	private String mSavePath;
	private int mProgress;
	private Gson gson = new Gson();
	//是否提示
	private boolean flagToast = false;

	private static final String PATH=NumUtil.APP_URL_SERVER_2+NumUtil.APK_UPDATE;
	private UpdateDialog mUpdateDialog;
	private UpdateApkInfo updateApkInfo;		//版本接口信息
	private String localVersionName = "";


	private Context mContext;
	public UpdateManager(Context context,boolean flag) {
		mContext = context;
		flagToast = flag;
	}

	private Handler mGetVersionHandler = new Handler(){
		public void handleMessage(Message msg){
			String jsonStr = (String)msg.obj;
			try {
				updateApkInfo = gson.fromJson(jsonStr, new TypeToken<UpdateApkInfo>(){}.getType());
				if(isUpdate()){
					//Toast.makeText(mContext, "需要更新", Toast.LENGTH_SHORT).show();
					showNoticeDialog();
				}else{
					if(flagToast){
						Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	/*
	 * 检测软件是否需要更新
	 */
	public void checkUpdate(){
		RequestQueue requestQueue = ContentApplication.getInstance().mQueue;
		StringRequest request = new StringRequest(PATH, new  Listener<String>(){

			@Override
			public void onResponse(String jsonStr) {
				Message msg = Message.obtain();
				msg.obj = jsonStr;
				mGetVersionHandler.sendMessage(msg);
			}

		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				//Toast.makeText(mContext, arg0.toString(), Toast.LENGTH_SHORT).show();
			}
		});
		requestQueue.add(request);
		requestQueue.start();
	}

	/*
	 * 与本地版本比较判断需要更新
	 */
	public boolean isUpdate(){
		boolean flag = false;
		String serverVersionInfo = updateApkInfo.getVersion();			//接口获取的版本  1.0.1
//		String[] str = {};
//		String[] loc = {};
		
		try {
			localVersionName = mContext.getPackageManager().getPackageInfo("com.mieasy.whrt_app_android_4", 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(serverVersionInfo.compareTo(localVersionName)>0){
			flag = true;
		}

		return flag;
	}

	/*
	 * 有更新时显示提示框
	 */
	protected void showNoticeDialog() {
		mUpdateDialog = new UpdateDialog(mContext,updateApkInfo.getDesc());
		mUpdateDialog.show();
		mUpdateDialog.setClicklistener(new UpdateDialog.ClickDialogUpdateInterface() {
			@Override
			public void doUpdate() {
				//隐藏当前的对话框
				mUpdateDialog.dismiss();
				//显示下载的对话框
				//showDownloadDialog();
				showNoticeDialogs();
			}

			@Override
			public void doCancel() {
				mUpdateDialog.dismiss();
			}
		});
	}
	/*
	 * 显示正在下载的对话框
	 */
	private void showNoticeDialogs() {
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setTitle("正在下载...");
		progressDialog.setCanceledOnTouchOutside(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		new downloadAsyncTask().execute();
	}

	/**
	 * 下载新版本应用
	 */

	private class downloadAsyncTask extends AsyncTask<Void,Integer,Integer> {
		@Override
		protected void onPreExecute() {
			Log.e("onPreExcute:", "执行至--onPreExecute");
			progressDialog.show();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			URL url;
			HttpURLConnection connection = null;
			InputStream in = null;
			FileOutputStream out = null;
			try{
				url = new URL(NumUtil.APP_URL_SERVER_2+"download/"+updateApkInfo.getFileName());
				connection = (HttpURLConnection)url.openConnection();
				in = connection.getInputStream();
				long fileLength = connection.getContentLength();
				File target= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),updateApkInfo.getFileName());
				out = new FileOutputStream(target,false);
				Log.e("out","out:" +target.toString());
				byte[] buffer = new byte[1024 * 1024];
				int len = 0;
				long readLength = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
					readLength += len;
					int curProgress = (int) (((float) readLength / fileLength) * 100);
					publishProgress(curProgress);
					if (readLength >= fileLength) {
						break;
					}
				}

				out.flush();
				//return DOWNLOADING;

			}catch (Exception e) {
				// TO DO: SHOW TIPS OPEN PERMISSON
                				Log.e("outputfile",e.getMessage());
				e.printStackTrace();
			}finally {

               if (out != null) {
				   try {
					   out.close();
				   }catch (IOException e) {
					   e.printStackTrace();
				   }
			   }
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (connection != null) {
					connection.disconnect();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			Log.e("progressDialog", "异步更新进度接收到的值：" + values[0]);
			progressDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Integer integer) {

			progressDialog.dismiss();//关闭进度条
			//安装应用
			installAPK();
		}
	}


	/*
	 * 下载到本地后执行安装
	 */
	protected void installAPK() {
		File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),updateApkInfo.getFileName());
		if(!apkFile.exists()){
			return;
		}

		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(apkFile);
		Log.e("outputfile",uri.toString());
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}
}  