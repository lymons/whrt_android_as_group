package com.mieasy.whrt_app_android_4.act.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.SlideBackActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.BuildingLine;
import com.mieasy.whrt_app_android_4.util.BitmapUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.util.OnTouchUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 查看路线图界面
 * @author arvin
 *
 */
public class ShowCircuitActivity extends SlideBackActivity {
	private Bitmap bmp;
	private ImageView mImageView;
	private ImageButton mImageButton;
	private TextView mTextViewTitle;

	private Boolean IsNetWork;
	private int currentW,currentH;
	private String url;
	private int titleTxt;
	private int position;
	private String picUrl;
	private Matrix matrix;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pro_showinfo_circuit);
		initValue();
		initView();
	}

	private void initValue() {
		IsNetWork = ContentApplication.getInstance().IsNetWorkAvailable;
		
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		currentW = outMetrics.widthPixels;
		currentH = outMetrics.heightPixels;

		Intent intent = getIntent();
		BuildingLine mBuildingLine = intent.getParcelableExtra(NumUtil.JUMP_INTENT);
		picUrl = NumUtil.APP_URL_SERVER_2+"image/"+mBuildingLine.getPic();
	}

	private void initView() {
		mTextViewTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTextViewTitle.setText(R.string.prolinepic);
		mImageView = (ImageView) findViewById(R.id.div_main);
		mImageButton = (ImageButton) findViewById(R.id.iv_top_left_back);
		mImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowCircuitActivity.this.finish();
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = ImageLoader.getInstance().loadImageSync(picUrl, ContentApplication.getInstance().options1);
				Message msg = Message.obtain();
				if(bitmap!=null){
					msg.obj = bitmap;
					msg.what = 0x001;
				}else{
					msg.what = 0x002;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			bmp = (Bitmap) msg.obj;
			bmp = BitmapUtil.getBitmap((Bitmap) msg.obj, currentW*2, currentH*2);
			matrix = new Matrix();
			matrix.setScale(0.5f, 0.5f);
			matrix.postTranslate(0, (float)(currentH*0.2));
			mImageView.setImageMatrix(matrix);
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x001:
				mImageView.setImageBitmap(bmp);
				mImageView.setVisibility(View.VISIBLE);
				mImageView.setOnTouchListener(new OnTouchUtil());
				break;
			case 0x002:
				mImageView.setVisibility(View.GONE);
				break;
			}
		}
	};
}
