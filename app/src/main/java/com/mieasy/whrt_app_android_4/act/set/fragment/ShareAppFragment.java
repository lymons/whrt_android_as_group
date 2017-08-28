package com.mieasy.whrt_app_android_4.act.set.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.mieasy.whrt_app_android_4.R;

public class ShareAppFragment extends Fragment {
	private View view;
	private ImageView mImageView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.set_main_info_shareapp, null);
		initView();
		return view;
	}

	private void initView() {
		mImageView = (ImageView) view.findViewById(R.id.qrcode);
		
		DisplayMetrics outMetrics = new DisplayMetrics();
		
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int currentW = outMetrics.widthPixels;
		
		LinearLayout.LayoutParams mParam = (LayoutParams) mImageView.getLayoutParams();
		mParam.width = (int)(currentW-currentW*0.2);
		mParam.height = mParam.width;
		mParam.setMargins((int)(currentW*0.1), (int)(currentW*0.1), (int)(currentW*0.1), 0);
		mImageView.setLayoutParams(mParam);
	}

}
