package com.mieasy.whrt_app_android_4.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;

public class SingleCheckedListLeftBgAdapter extends BaseAdapter{
	
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
				mLastCheckedView = view;
				if(mCheckedPosition==0){
//					view.setBackgroundResource(R.drawable.line_bg_no1);
					view.setBackgroundResource(R.drawable.ap_01);
				}else if(mCheckedPosition==1){
					view.setBackgroundResource(R.drawable.ap_02);
				}else if(mCheckedPosition==2){
					view.setBackgroundResource(R.drawable.ap_03);
				}else if(mCheckedPosition==3){
					view.setBackgroundResource(R.drawable.ap_04);
				}else if (mCheckedPosition==4){
					view.setBackgroundResource(R.drawable.ap_06);
				}
//				view.setTextColor(mContext.getResources().getColor(R.color.line_list_text));
				view.setTextColor(mContext.getResources().getColor(R.color.block_bg_color_alpha));
			}else{
				view.setBackgroundColor(mContext.getResources().getColor(R.color.line_list));
				view.setTextColor(mContext.getResources().getColor(R.color.line_list_text));
			}
		}
	}

	public SingleCheckedListLeftBgAdapter(Context context, String[] data) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_left_card_bg_number, null);			
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
