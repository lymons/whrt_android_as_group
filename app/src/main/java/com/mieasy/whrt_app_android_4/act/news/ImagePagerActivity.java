package com.mieasy.whrt_app_android_4.act.news;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.R.id;
import com.mieasy.whrt_app_android_4.view.HackyViewPager;

public class ImagePagerActivity extends FragmentActivity implements OnClickListener {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index"; 
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private Button btnSave;
	private ArrayList<ImageDetailFragment> mFragmentList;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);

		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
		
		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
		indicator.setText(text);
		
		btnSave = (Button) findViewById(id.btn_save);
		btnSave.setOnClickListener(this);
		
		mFragmentList = new ArrayList<ImageDetailFragment>();
		for(int str=0;str<urls.size();str++){
			mFragmentList.add(ImageDetailFragment.newInstance(urls.get(str)));
		}
		
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}
		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {
		public ArrayList<String> fileList;

		public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}
	}

	@Override
	public void onClick(View v) {
		mFragmentList.get(mPager.getCurrentItem()).saveBitmapToSD();
	}
}
