package com.mieasy.whrt_app_android_4.act.info;

import com.mieasy.whrt_app_android_4.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemFragment extends Fragment {
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_info_column, null);
		TextView mTextView = (TextView) view.findViewById(R.id.tv_column);
		String title = getArguments().getString("tit");
		mTextView.setText(title);
		return view;
	}
}
