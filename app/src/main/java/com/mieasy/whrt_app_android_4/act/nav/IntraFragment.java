package com.mieasy.whrt_app_android_4.act.nav;

import java.util.ArrayList;
import java.util.List;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.util.BitmapUtil;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.view.ZoomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 站内
 * @author Administrator
 *
 */
public class IntraFragment extends Fragment{
	private LinearLayout linearLayout;
	private TextView mStationInfo;
	private View view;
//	private ImageView mImageView;
	private ZoomImageView mImageView;
	
	private Stations stations;
	private Bitmap bmp;
	private int window_width,window_height;
	private ViewTreeObserver viewTreeObserver;
	private int state_height;// 状态栏的高度
	
	private String bitUrl;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.map_nav_intra, null);
		getBundleInfo();
		initView();
		return view;
	}
	
	/**
	 * 初始化控件
	 */
	public void initView(){
		DisplayMetrics outMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int currentW = outMetrics.widthPixels;
		int currentH = outMetrics.heightPixels;
		
		linearLayout = (LinearLayout) view.findViewById(R.id.lin_intra_img);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(currentW,currentW));
		mImageView = (ZoomImageView) view.findViewById(R.id.iv_intra_img);
		
//		bmp = BitmapUtil.ReadBitmapByAssetFileName(view.getContext(),stations.getStationId()+".jpg",currentW,currentW);
		//异步获取图片
//		mImageView.setImage(bmp);
		mImageView.setVisibility(View.GONE);
		mStationInfo = (TextView) view.findViewById(R.id.tv_station_info);
		
		if(stations!=null){
			String str=stations.getStationName()+"\n\n";
			int strSize = str.length();
			List<String> lists =  setInterchange();
			for(int i = 0;i<lists.size();i++){
				str += lists.get(i)+"\n";
			}
			ColorStateList redColors = ColorStateList.valueOf(R.color.block_bg_color_black);
			SpannableStringBuilder spanBuilder = new SpannableStringBuilder(str);
			spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 60, redColors, null), 0, strSize, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			mStationInfo.setText(spanBuilder);
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			bmp = (Bitmap) msg.obj;
			mImageView.setImage(bmp);
		};
	};
	
	class ImgThread extends Thread{
		@Override
		public void run() {
			bitUrl = NumUtil.APP_URL_SERVER_2+"image/"+stations.getStationId()+".jpg";
			Bitmap netWorkBmp = ImageLoader.getInstance().loadImageSync(bitUrl);
			BitmapUtil.saveBmpToSd(netWorkBmp, bitUrl, 80);
			Message msg = Message.obtain();
			msg.obj = netWorkBmp;
			handler.sendMessage(msg);
			super.run();
		}
	}
	
	/**
	 * 获取网络图片
	 */
	private void getNetWorkImg(){
		/**
		 * 首先从本地查找图片
		 * 如果A服务器没有找到就找B服务器
		 * 1、没有就从网络上下载、
		 * 		网络上没有，就直接隐藏图片显示控件
		 * 2、有就显示
		 */
	}
	
	/**
	 * 获取Activity传递过来的参数
	 */
	private void getBundleInfo() {
		Bundle bundle =  getArguments();
		if(bundle!=null){
			stations = bundle.getParcelable(NumUtil.JUMP_BUNDLE);
		}
	}
	
	/**
	 * 站点信息
	 * @return
	 */
    public List<String> setInterchange(){
    	List<String> lists = new ArrayList<String>();
    	if("循礼门".equals(stations.getStationName())){
    		lists.add("武汉轨道交通1号线【首末班车时间】");
    		lists.add("汉口北方向");
    		lists.add("工作日  6:00-23:08  双休日  6:36-23:08");
    		lists.add("东吴大道方向");
    		lists.add("工作日  6:03-23:01  双休日  6:36-23:01");
    		
    		lists.add("武汉轨道交通2号线【首末班车时间】");
    		lists.add("光谷广场方向");
    		lists.add("工作日  6:00-22.50  双休日  6:30-22:50");
    		lists.add("金银谭方向");
    		lists.add("工作日  6:02-23:02  双休日  6:32-23:02");
    	}else if("中南路".equals(stations.getStationName())){
    		lists.add("武汉轨道交通2号线【首末班车时间】");
    		lists.add("光谷广场方向");
    		lists.add("工作日  6:00-22.50  双休日  6:30-22:50");
    		lists.add("金银谭方向");
    		lists.add("工作日  6:02-23:02  双休日  6:32-23:02");

    		lists.add("武汉轨道交通4号线【首末班车时间】");
    		lists.add("武汉火车站方向");
    		lists.add("工作日  6:04-22.534  双休日  6:34-22:34");
    		lists.add("武昌火车站方向");
    		lists.add("工作日  6:00-22:57  双休日  6:30-22:57");
    	}else if("洪山广场".equals(stations.getStationName())){
    		lists.add("武汉轨道交通2号线【首末班车时间】");
    		lists.add("光谷广场方向");
    		lists.add("工作日  6:00-22.50  双休日  6:30-22:50");
    		lists.add("金银谭方向");
    		lists.add("工作日  6:02-23:02  双休日  6:32-23:02");
    		
    		lists.add("武汉轨道交通4号线【首末班车时间】");
    		lists.add("武汉火车站方向");
    		lists.add("工作日  6:04-22.534  双休日  6:34-22:34");
    		lists.add("武昌火车站方向");
    		lists.add("工作日  6:00-22:57  双休日  6:30-22:57");
    	}else{
    		if(1==stations.getLineId()){
    			lists.add("武汉轨道交通1号线【首末班车时间】");
        		lists.add("汉口北方向");
        		lists.add("工作日  6:00-23:08  双休日  6:36-23:08");
        		lists.add("东吴大道方向");
        		lists.add("工作日  6:03-23:01  双休日  6:36-23:01");
    		}else if(2==stations.getLineId()){
    			lists.add("武汉轨道交通2号线【首末班车时间】");
        		lists.add("光谷广场方向");
        		lists.add("工作日  6:00-22.50  双休日  6:30-22:50");
        		lists.add("金银谭方向");
        		lists.add("工作日  6:02-23:02  双休日  6:32-23:02");
    		}else if(4==stations.getLineId()){
    			lists.add("武汉轨道交通4号线【首末班车时间】");
        		lists.add("武汉火车站方向");
        		lists.add("工作日  6:04-22.534  双休日  6:34-22:34");
        		lists.add("武昌火车站方向");
        		lists.add("工作日  6:00-22:57  双休日  6:30-22:57");
    		}
    	}
		return lists;
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if(bmp!=null){
    		if(bmp.isRecycled()){
    			bmp.recycle();
    		}
    	}
    }
}