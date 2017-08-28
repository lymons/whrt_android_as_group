package com.mieasy.whrt_app_android_4.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.entity.Options;
import com.mieasy.whrt_app_android_4.util.FileUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class DatabaseService {
	public static SharedPreferences perPreferences;

	public static void checkDBName(Context context){
		if((NumUtil.DB_NAME=="")||(NumUtil.DB_NAME.length()==0)){   //判断数据库名是否为空
			//用SharedPreferences来存储数据库名
			perPreferences = context.getSharedPreferences(NumUtil.SHAREPRE_WHRTONCE, context.MODE_PRIVATE);
			NumUtil.DB_NAME = perPreferences.getString(NumUtil.DATABASE_NAME, "");
			if(NumUtil.DB_NAME.length()==0){ 		//如果SharedPreferences取出的数据库名为空
				//设置默认的数据库名并且写入SharedPreferences
				NumUtil.DB_NAME = NumUtil.DB_NAME_ORIGINAL;
				Editor editor = perPreferences.edit();
				editor.putString(NumUtil.DATABASE_NAME, NumUtil.DB_NAME);
				editor.commit();
			}
		}
	}

	/**
	 * 数据库更新处理
	 * @param context		上下文对象
	 * @param DBNameNew		数据库名  100
	 */
	public static void checkDBUpdate(final Context context){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String filename = NumUtil.indexDateMap.get(NumUtil.ANDROIDDB);		//文件名
				String url = NumUtil.APP_URL_SERVER_2 + filename;					//文件URL
				String oldName = "";
				if(FileUtil.downloadDBFile(context, filename, url)){
					//先copy到database中
					if(FileUtil.copyfileDBToDatabases(context, filename)){			//从files文件夹拷贝到database文件夹下
						File dbFile = new File(context.getDatabasePath(filename).getPath());
						if(dbFile.length()==NumUtil.DB_SIZE){						//判断文件大小
							//用SharedPreferences来存储数据库名
							perPreferences = context.getSharedPreferences(NumUtil.SHAREPRE_WHRTONCE, context.MODE_PRIVATE);
							oldName = NumUtil.DB_NAME;
							NumUtil.DB_NAME = filename;
							Editor editor = perPreferences.edit();
							editor.putString(NumUtil.DATABASE_NAME, NumUtil.DB_NAME);
							editor.commit();
							ContentApplication.getInstance().liteOrm = LiteOrm.newSingleInstance(context,NumUtil.DB_NAME);
							FileUtil.removeOldDBString(new File(context.getDatabasePath(oldName).getPath()));
						}
					}
				}
			}
		}).start();
	}
}