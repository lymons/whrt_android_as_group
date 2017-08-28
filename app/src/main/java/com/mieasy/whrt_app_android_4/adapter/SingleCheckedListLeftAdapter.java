package com.mieasy.whrt_app_android_4.adapter;

import com.mieasy.whrt_app_android_4.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleCheckedListLeftAdapter extends BaseAdapter{
	
	private String[] mData;
	private Context mContext;
	
	/**
	 * the View checked
	 */
	ViewHolder holder = null;
//	private TextView mLastCheckedView = null;
//	private LinearLayout mLastCheckedView = null;
	private View mLastCheckedView = null;
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
		setViewSelected(mLastCheckedView, false,mCheckedPosition);	
//		TextView textView = (TextView)checkedView;
//		setViewSelected(textView, true);		
//		LinearLayout linView = (LinearLayout)checkedView;
		View view = (LinearLayout)checkedView;
		setViewSelected(view, true,mCheckedPosition);	
	}	
	
	private void setViewSelected(View view, boolean selected,int item){
		if(view != null){
			if(selected){
//				linView.setBackgroundColor(mContext.getResources().getColor(R.color.line_list));
				ImageView imageView = (ImageView) view.findViewById(R.id.item_left_iv);
				imageView.setImageResource(R.drawable.line_no01);
				mLastCheckedView = view;
//				setLinStyle(item,selected,view);
			}else{
				view.setBackgroundColor(mContext.getResources().getColor(R.color.activity_bg_color_while));
//				setLinStyle(item,selected,view);
			}
		}
	}
	
	public void setLinStyle(int item,Boolean bool,View view){
		ViewHolder viewHolder = null;
		if(bool){
			holder.imageView.setVisibility(View.VISIBLE);
			holder.view.setVisibility(View.VISIBLE);
			viewHolder.textView = (TextView) view.findViewById(R.id.item_left_tv);
			viewHolder.imageView = (ImageView) view.findViewById(R.id.item_left_iv);
			viewHolder.view = (View) view.findViewById(R.id.item_left_view);

			if(item == 0){
				viewHolder.imageView.setImageResource(R.drawable.line_no01);
			}else if(item == 1){
				viewHolder.imageView.setImageResource(R.drawable.line_no02);
			}else if(item == 2){
				viewHolder.imageView.setImageResource(R.drawable.line_no03);
			}else if(item == 3){
				viewHolder.imageView.setImageResource(R.drawable.line_no04);
			}
		}else{
			viewHolder.imageView.setVisibility(View.GONE);
			viewHolder.view.setVisibility(View.GONE);
		}
	}

	public SingleCheckedListLeftAdapter(Context context, String[] data) {
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_left_card_number, null);
			holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.item_left_lin);
			holder.textView = (TextView) convertView.findViewById(R.id.item_left_tv);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_left_iv);
			holder.view = (View) convertView.findViewById(R.id.item_left_view);
			convertView.setTag(holder);

		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
//		setViewSelected(holder.linearLayout, mCheckedPosition == position,position);
//		setViewSelected(holder.linearLayout, false,position);
		setViewSelected(convertView, false,position);
		holder.textView.setText(mData[position]);
		return convertView;
	}	

	class ViewHolder{
		LinearLayout linearLayout;
		TextView textView;
		ImageView imageView;
		View view;
	}	
}
