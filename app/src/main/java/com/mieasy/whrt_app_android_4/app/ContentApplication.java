package com.mieasy.whrt_app_android_4.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.assit.SQLiteHelper;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.util.FileUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import java.io.File;

import cn.jpush.android.api.JPushInterface;


public class ContentApplication extends Application{
	private static ContentApplication mAppApplication;

	public static LiteOrm liteOrm;
	public static DisplayImageOptions options1,options2,options3,options4;	//轮播
	public static boolean isnotfirstcometo = false;
	public static boolean lineisnotfirstcometo = false;
	public static String pushInfo = null;
	public ImageLoader mImageLoader;
	private Application application;
	public static boolean IsNetWorkAvailable;
	public static RequestQueue mQueue;
	public int dbVersion;
	public SQLiteHelper.OnUpdateListener onUpdateListener;
	public static LiteOrm liteOrm_add;

	/** 获取Application */
	public static ContentApplication getInstance() {
		return mAppApplication;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		isNetworkAvailable(getApplicationContext());
		initLiteOrm(getApplicationContext());
		//分享AppId和Appkey
		PlatformConfig.setQQZone("1105879778","BkW5X29ln1UgUsMU");//公司
		PlatformConfig.setSinaWeibo("2867158862","17d2e9b28464a82acb8755d720812afb");
		PlatformConfig.setWeixin("wxc1b6cd112b3e4d77","b519618c7af031919cfaa4f9eb3b4666");
		Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
		//消息推送AppKey=3653752ecbf6db5f96dbd1c1 SECRET =  73b7853903ba0846a38fb71c
        //消息推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
        //创建数据库
		createDB(this);
	}

	//设置LiteOrm对象
	private void initLiteOrm(Context context) {
		File files = new File("/data/data/com.mieasy.whrt_app_android_4/databases");
		if(!files.exists()){
			if(FileUtil.copyDBToDatabases(context,files)){
				Log.i("NavItemFragment", "数据库复制完成");
			}else{
				Log.i("NavItemFragment", "数据库复制失败");
			};
		}
		liteOrm = LiteOrm.newSingleInstance(context,NumUtil.DB_NAME);
		liteOrm.setDebugged(true);
	}

	/**
	 * imageloader 初始化
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		mQueue = Volley.newRequestQueue(context); mQueue.start();
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.threadPoolSize(5);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(500 * 1024 * 1024); // 100 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		// config.writeDebugLogs(); // Remove for release app
		ImageLoader.getInstance().init(config.build());
		
		//首页轮播
		options1 = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_loading_error)
				.showImageOnFail(R.drawable.img_loading_error)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		//集团快讯
		options2 = new DisplayImageOptions.Builder()  
				.showImageOnLoading(R.drawable.jtkx)
				.showStubImage(R.drawable.jtkx) // 在ImageView加载过程中显示图片  
				.showImageForEmptyUri(R.drawable.jtkx) // image连接地址为空时  
				.showImageOnFail(R.drawable.jtkx) // image加载失败  
				.cacheInMemory(true) // 加载图片时会在内存中加载缓存  
				.cacheOnDisc(true) // 加载图片时会在磁盘中加载缓存  
				.build();  
		//地铁运营
		options3 = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.dtyy)
				.showImageForEmptyUri(R.drawable.dtyy) // image连接地址为空时 
				.showImageOnFail(R.drawable.dtyy)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		//新闻详细
		options4 = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.newsdetail_placeholder)
				.showImageForEmptyUri(R.drawable.newsdetail_placeholder) // image连接地址为空时 
				.showImageOnFail(R.drawable.newsdetail_placeholder)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}


	/**
	 * 判断当前是否有网络
	 * @param context
	 * @return true:有网络
	 * @return false：无网络
	 */
	public static void isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			IsNetWorkAvailable = false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						IsNetWorkAvailable = true;
						break;
					}
				}
			}
		}
	}

	//创建数据库
	private void createDB(Context context) {
		DataBaseConfig config = new DataBaseConfig(context,"whrts.db");
		config.dbVersion = 1;
		config.onUpdateListener = null;
		liteOrm_add = LiteOrm.newCascadeInstance(config);
		liteOrm_add.setDebugged(false);
	}
}