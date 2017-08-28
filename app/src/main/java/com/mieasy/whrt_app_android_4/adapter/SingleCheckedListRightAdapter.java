package com.mieasy.whrt_app_android_4.adapter;
import com.mieasy.whrt_app_android_4.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SingleCheckedListRightAdapter extends BaseAdapter{
	
	private String[] mData;
	private Context mContext;
	
	/**
	 * the View checked
	 */
	private TextView mLastCheckedView = null;
	/**
	 * the position in the data
	 */
	private int mCheckedPosition = -1;
	
	public void setCheckedPosition(int position){
		mCheckedPosition = position;
	}
		
	public void setData(String[] data){
		mData = data;
		mLastCheckedView = null;
		mCheckedPosition = -1;
		notifyDataSetChanged();		
	}
		
	/**
	 * @param checkedView the checkedView to set
	 */
	public void setCheckedView(View checkedView) {
		setViewSelected(mLastCheckedView, false);	
		TextView textView = (TextView)checkedView;
		setViewSelected(textView, true);		
	}	
	
	private void setViewSelected(TextView view, boolean selected){
		if(view != null){
			if(selected){
				view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_bg_color_gray));
				view.setTextColor(mContext.getResources().getColor(R.color.activity_bg_color_while));
				mLastCheckedView = view;
			}else{
				view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_bg_color_while));
				view.setTextColor(mContext.getResources().getColor(R.color.activity_bg_color_gray));
			}
		}
	}

	public SingleCheckedListRightAdapter(Context context, String[] data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.length;
	}

	@Override
	public Object getItem(int position) {
		return mData[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_right_card_number, null);			
			holder.name = (TextView) convertView.findViewById(R.id.textView1);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		setViewSelected(holder.name, mCheckedPosition == position);
		holder.name.setText(mData[position]);
		return convertView;
	}	

	class ViewHolder{
		TextView name;
	}	
}
