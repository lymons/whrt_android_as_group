package com.mieasy.whrt_app_android_4.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.bean.Colors;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.util.DrawImagePath;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 画出路线详细信息   作废
 * @author Administrator
 *
 */
public class PathInfoAdapter extends BaseAdapter{
	private List<Stations> stationList;
	private HashMap<String,Integer> hashStation; 	//list的ID和方向ID
	private Colors colorsTop;
	private Colors colorBottom;
	private LayoutInflater mInflater;
	private List<Colors> colorsList;

	public PathInfoAdapter(Context context,List<Stations> stationList) {
		super();
		this.stationList = stationList;
		mInflater = LayoutInflater.from(context);
		hashStation = new HashMap<String, Integer>();
		colorsList = new ArrayList<Colors>();
	}

	@Override
	public int getCount() {
		return stationList.size();
	}

	@Override
	public Object getItem(int position) {
		return stationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.pathinfo_listview_item, null);
			viewHolder.tvView = (TextView) convertView.findViewById(R.id.pathinfo_left_txt);
			viewHolder.imgView = (ImageView) convertView.findViewById(R.id.pathinfo_left_img);
			viewHolder.linRight = (LinearLayout) convertView.findViewById(R.id.pathinfo_right_text);
			viewHolder.imgLine = (ImageView) convertView.findViewById(R.id.pathinfo_right_lines);
			viewHolder.sameLine = (ImageView) convertView.findViewById(R.id.pathinfo_same_lines);
			viewHolder.rightDir = (TextView) convertView.findViewById(R.id.pathinfo_right_dir);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		initView(viewHolder);
		if((hashStation.get(position+"")!=null)){//判断当前item是不是首站或中转站					
			viewHolder.tvView.setTextSize(16);
			viewHolder.tvView.setTextColor(convertView.getResources().getColor(R.color.block_bg_color_black));
			//某方向
			String derectionName =NumUtil.directionsMap.get(hashStation.get(position+"")+"");
			String name = "开往"+derectionName;
			viewHolder.rightDir.setText(name);
			Log.e("001","derectionName:"+NumUtil.directionsMap.get(hashStation.get(position+"")+""));
			//设置该站是第几号线
			switch(stationList.get(position).getLineId()){
			case 1:
				viewHolder.imgLine.setImageResource(R.drawable.line_no01);
				break;
			case 2:
				viewHolder.imgLine.setImageResource(R.drawable.line_no02);
				if (stationList.get(position).getStationName().equals("洪山广场") || stationList.get(position).getStationName().equals("中南路")) {
					viewHolder.sameLine.setVisibility(View.VISIBLE);
					viewHolder.sameLine.setImageResource(R.drawable.same_platform_line);
				}
				break;
			case 3:
				viewHolder.imgLine.setImageResource(R.drawable.line_no03);
				break;
			case 4:
				viewHolder.imgLine.setImageResource(R.drawable.line_no04);
				if (stationList.get(position).getStationName().equals("洪山广场") || stationList.get(position).getStationName().equals("中南路")) {
					viewHolder.sameLine.setVisibility(View.VISIBLE);
					viewHolder.sameLine.setImageResource(R.drawable.same_platform_line);
				}
				break;
			case 6:
				viewHolder.imgLine.setImageResource(R.drawable.line_no06);
				break;
			}
			if(position==0){					//判断是首站
				viewHolder.linRight.setVisibility(View.VISIBLE);
				if(colorsList.size()>position+1){//本地有
					colorsTop = colorsList.get(position);
				}else{
					colorsTop = NumUtil.colorsList.get(hashStation.get(position+"")+"");
					colorsList.add(colorsTop);
				}
				viewHolder.imgView.setImageBitmap(DrawImagePath.drawStartPointImage(128, 128,colorsTop));
			}else{								//如果是中转站	
				if(colorsList.size()>(position+1)){//本地有
					colorsTop = colorsList.get(position-1);
					colorBottom = colorsList.get(position);
				}else{
					colorBottom = NumUtil.colorsList.get(hashStation.get(position+"")+"");
					colorsList.add(colorBottom);
				}
				viewHolder.linRight.setVisibility(View.VISIBLE);
				viewHolder.imgView.setImageBitmap(DrawImagePath.drawTransferPointImage(128, 128,colorsTop,colorBottom));
				colorsTop = colorBottom;
			}
		}else{									//设置经过点样式
			if(colorsList.size()>(position+1)){		//本地有
				colorsTop = colorsList.get(position);
			}else{
				colorsList.add(colorsTop);
			}
			viewHolder.tvView.setTextColor(convertView.getResources().getColor(R.color.block_bg_color_black_transparent_90));
			viewHolder.tvView.setTextSize(16);
			viewHolder.imgView.setImageBitmap(DrawImagePath.drawAfterPointImage(128, 128,colorsTop));
			viewHolder.linRight.setVisibility(View.GONE);
		}											//如果是终点站
		if(stationList.size()-1==position){			//终点站
			viewHolder.tvView.setTextSize(16);
			viewHolder.tvView.setTextColor(convertView.getResources().getColor(R.color.block_bg_color_black));
			viewHolder.imgView.setImageBitmap(DrawImagePath.drawStopPointImage(128, 128,colorsTop));
			viewHolder.linRight.setVisibility(View.VISIBLE);
			viewHolder.imgLine.setVisibility(View.GONE);
//			viewHolder.rightDir.setTextColor(convertView.getResources().getColor(R.color.block_bg_color_black));
			viewHolder.rightDir.setTextSize(16);
			viewHolder.rightDir.setText("终点站");
		}
		viewHolder.tvView.setText(stationList.get(position).getStationName());
		return convertView;
	}

	public void initView(ViewHolder viewHolder){
		viewHolder.imgLine.setVisibility(View.VISIBLE);
	}

	class ViewHolder{
		public ImageView imgView,imgLine;
		public ImageView sameLine;
		public LinearLayout linRight;
		public TextView tvView,rightDir;
	}

	/**
	 * 添加方向以及换乘站信息
	 * @param mapStationIDAndDir
	 */
	public void addHuanInfoMap(HashMap<String,Integer> mapStationIDAndDir) {
		hashStation.putAll(mapStationIDAndDir);
	}
}