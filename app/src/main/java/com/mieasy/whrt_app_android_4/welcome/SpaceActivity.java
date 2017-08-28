package com.mieasy.whrt_app_android_4.welcome;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.main.MainActivity;
import com.mieasy.whrt_app_android_4.util.NetWorkManager;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 欢迎页
 * @author Administrator
 */
public class SpaceActivity extends Activity{
	private ImageView imgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		setContentView(R.layout.layout_space);
		initValue();
		imgView = (ImageView) findViewById(R.id.img_space);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					staAct();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void initValue() {
		NumUtil.FILE_PATH = this.getFilesDir().getPath().toString();		//file文件夹目录地址
		NumUtil.CACHE_PATH = this.getCacheDir().getPath().toString();	//cache文件夹目录地址
	}

	public void staAct(){
		Intent intent = new Intent(SpaceActivity.this,MainActivity.class);
//		this.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);    //向左切换
		startActivity(intent);
		SpaceActivity.this.finish();
	}
}