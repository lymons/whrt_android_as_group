package com.mieasy.whrt_app_android_4.act.set;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 字体大小
 * @author arvin
 */
public class TextSizeFragment extends Fragment implements OnClickListener {
	private View view;
	private TableRow mRow21,mRow20,mRow19,mRow18,mRow17,mRow16;
	private ImageView mImg21,mImg20,mImg19,mImg18,mImg17,mImg16;
	
	private SharedPreferences perPreferences;
	private int isTextSize;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.set_main_info_textsize, null);
		initView();
		initSetting();
		return view;
	}

	private void initView() {
		mRow21 = (TableRow) view.findViewById(R.id.table_row_21);mRow21.setOnClickListener(this);
		mRow20 = (TableRow) view.findViewById(R.id.table_row_20);mRow20.setOnClickListener(this);
		mRow19 = (TableRow) view.findViewById(R.id.table_row_19);mRow19.setOnClickListener(this);
		mRow18 = (TableRow) view.findViewById(R.id.table_row_18);mRow18.setOnClickListener(this);
		mRow17 = (TableRow) view.findViewById(R.id.table_row_17);mRow17.setOnClickListener(this);
		mRow16 = (TableRow) view.findViewById(R.id.table_row_16);mRow16.setOnClickListener(this);
		mImg21 = (ImageView) view.findViewById(R.id.text_21_img);
		mImg20 = (ImageView) view.findViewById(R.id.text_20_img);
		mImg19 = (ImageView) view.findViewById(R.id.text_19_img);
		mImg18 = (ImageView) view.findViewById(R.id.text_18_img);
		mImg17 = (ImageView) view.findViewById(R.id.text_17_img);
		mImg16 = (ImageView) view.findViewById(R.id.text_16_img);
	}

	private void initSetting() {
		perPreferences = view.getContext().getSharedPreferences(NumUtil.SHAREPRE_WHRTONCE, getActivity().MODE_PRIVATE);
		isTextSize = perPreferences.getInt(NumUtil.isTextSize, 20);
		switch (isTextSize) {
		case 16:
			mImg16.setVisibility(View.VISIBLE);
			break;
		case 17:
			mImg17.setVisibility(View.VISIBLE);
			break;
		case 18:
			mImg18.setVisibility(View.VISIBLE);
			break;
		case 19:
			mImg19.setVisibility(View.VISIBLE);
			break;
		case 20:
			mImg20.setVisibility(View.VISIBLE);
			break;
		case 21:
			mImg21.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	
	public void resetImg(){
		mImg21.setVisibility(View.GONE);
		mImg20.setVisibility(View.GONE);
		mImg19.setVisibility(View.GONE);
		mImg18.setVisibility(View.GONE);
		mImg17.setVisibility(View.GONE);
		mImg16.setVisibility(View.GONE);
	}
	
	public  void setChangeView(ImageView imgVIew,int textSize){
		imgVIew.setVisibility(View.VISIBLE);
		Editor editor = perPreferences.edit();
		editor.putInt(NumUtil.isTextSize, textSize);
		editor.commit();
	}

	@Override
	public void onClick(View v) {
		resetImg();
		switch (v.getId()) {
		case R.id.table_row_21:
			setChangeView(mImg21,21);
			break;
		case R.id.table_row_20:
			setChangeView(mImg20,20);
			break;
		case R.id.table_row_19:
			setChangeView(mImg19,19);
			break;
		case R.id.table_row_18:
			setChangeView(mImg18,18);
			break;
		case R.id.table_row_17:
			setChangeView(mImg17,17);
			break;
		case R.id.table_row_16:
			setChangeView(mImg16,16);
			break;

		default:
			break;
		}
	}
}
