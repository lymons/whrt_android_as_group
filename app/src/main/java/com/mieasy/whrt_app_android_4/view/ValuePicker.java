package com.mieasy.whrt_app_android_4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.adapter.SingleCheckedListLeftBgAdapter;
import com.mieasy.whrt_app_android_4.adapter.SingleCheckedListRightAdapter;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.imp.StationCallBackInterface;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.HashMap;
import java.util.Map;

public class ValuePicker extends LinearLayout{
	private StationCallBackInterface mCallBack;
	
	private Context mContext;
	private LayoutInflater mInflater;
	private ListView lvLeft, lvRight;
			
	private String[] summaries = NumUtil.STATIONLINE_STR;				//所有线路的数组
	private Map<String, String[]> details= new HashMap<String, String[]>();		//ͬ路线数组对应的数组  map<“1号线”，{"A","B","C"}>
	private LiteOrm liteOrm;
	
	private int mPosLeft = -1;	
	private String mCurLeft;
	private int mPosRight = -1;
	private String mCurRight;
	
	public ValuePicker SetOnClickCallBack(StationCallBackInterface mCallBack){
		this.mCallBack = mCallBack;
		return this;
	}
	
	/**
	 * Listener to handle the logic when current city card number is selected
	 */
	public OnClickListener mListener;
	
	public void setStationInfo(Map<String,String[]> map,LiteOrm liteOrm){
		this.details = map;
		this.liteOrm = liteOrm;
	}
	
	/**
	 * @param listener the listener to set
	 */
	public void setButtonOnClickListener(OnClickListener listener) {
		this.mListener = listener;
	}

	public ValuePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public ValuePicker(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		mContext = context;		
	}
	
	public void initialize(){
		initData();		
		initView();
	}
	
	private void initData(){		
		
		int len = summaries.length;
		
		for(int i = 0; i < len; i++){
			
			String summary = summaries[i];
			
			if(summary.equals(mCurLeft)){
				mPosLeft = i;
				break;
			}		
		}
		
		if(mPosLeft >= 0){
			String summary = summaries[mPosLeft];
			String[] right = details.get(summary);
			int lenOfRight = right.length;
			
			for(int j = 0; j < lenOfRight; j++){
				
				String detail = right[j];
				
				if(mCurRight != null && detail.equals(mCurRight)){
					mPosRight = j;
					break;
				}
			}
		}
		
		
		
		Log.v(this.getClass().getName(), "mCurLeft = " + mCurLeft + " | mPosLeft = " + mPosLeft);
		Log.v(this.getClass().getName(), "mCurRight= " + mCurRight + " | mPosRight "+ mPosRight);
	}
	
	private void initView(){
		mInflater = LayoutInflater.from(mContext);
		View parent = mInflater.inflate(R.layout.custom_twofold_listview, this);
		
		lvLeft = (ListView) parent.findViewById(R.id.lvLeft);
		lvRight = (ListView) parent.findViewById(R.id.lvRight);
		
//		final SingleCheckedListLeftAdapter lAdapter = new SingleCheckedListLeftAdapter(mContext, summaries);
		final SingleCheckedListLeftBgAdapter lAdapter = new SingleCheckedListLeftBgAdapter(mContext, summaries);
//		final SingleCheckedListRightAdapter lAdapter = new SingleCheckedListRightAdapter(mContext, summaries);
		lAdapter.setCheckedPosition(mPosLeft);
		lvLeft.setAdapter(lAdapter);
		
		String[] rights = new String[]{};
		if(mPosLeft >= 0 && mPosRight >= 0){
			rights = details.get(summaries[mPosLeft]);
		}
		
		final SingleCheckedListRightAdapter rAdapter = new SingleCheckedListRightAdapter(mContext, rights);		
		rAdapter.setCheckedPosition(mPosRight);		
		lvRight.setAdapter(rAdapter);
		//默认显示１号线
		mCurRight = null;
		lAdapter.setCheckedPosition(0);
		mPosLeft = 0;
		rAdapter.setData(details.get(summaries[mPosLeft]));

		//点击后的显示
		lvLeft.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				
				mCurRight = null;
				lAdapter.setCheckedPosition(position);
				lAdapter.setCheckedView(view);
				mPosLeft = position;
				rAdapter.setData(details.get(summaries[mPosLeft]));
			}
		});
		
		lvRight.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				rAdapter.setCheckedPosition(position);
				rAdapter.setCheckedView(view);
				mCurRight = details.get(summaries[mPosLeft])[position];
				Log.i("ValuePicker","position="+position+"; mPosLeft="+mPosLeft);
				int lineId=mPosLeft+1;
				if (mPosLeft==4) lineId=6;
				Stations s=LiteOrmServices.getStationById(liteOrm,position,lineId);
				if (s!=null) {
					Log.i("ValuePicker", s.toString());

					mCallBack.CallBack(s);
				}
			}
		});		
	}
	
	public String getLeftVaue(){
		if(mPosLeft >= 0){
			return summaries[mPosLeft];
		}else{
			return "";
		}
	}
	
	public void setLeftValue(String leftValue){
		mCurLeft = leftValue;
	}
	
	public String getRightValue(){
		return mCurRight;
	}
	
	public void setRightValue(String rightValue){
		mCurRight  = rightValue;
	}
}
