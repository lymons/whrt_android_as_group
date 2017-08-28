package com.mieasy.whrt_app_android_4.act.set;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.util.FileUtil;

import android.app.Fragment;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutAppFragment extends Fragment{
	private View view;
	private TextView mCopyRight,mTerms,mVersion;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.set_main_info_about, null);
		initView();
		return view;
	}

	private void initView() {
		mTerms = (TextView) view.findViewById(R.id.about_terms);
		mCopyRight = (TextView) view.findViewById(R.id.about_copyright);
		mVersion = (TextView) view.findViewById(R.id.about_version);
		
		String localVersionName = "";
		try {
			localVersionName = view.getContext().getPackageManager().getPackageInfo("com.mieasy.whrt_app_android_4", 0).versionName;
			mVersion.setText("版本：v"+localVersionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		String termsstr = FileUtil.readLocalAssetString(view.getContext(),"terms.txt");
		mTerms.setTextSize(17.0f);
		mTerms.setText(Html.fromHtml(termsstr));
		
		String copystr = FileUtil.readLocalAssetString(view.getContext(),"copyright.txt");
		mCopyRight.setTextSize(10.0f);
		mCopyRight.setText(Html.fromHtml(copystr));
	}
}
