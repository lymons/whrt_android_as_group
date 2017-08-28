package com.mieasy.whrt_app_android_4.act.news;

import java.util.ArrayList;
import java.util.List;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class NewsActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener {
	private View view;

	private ImageButton mImgBtnBack;
	private TextView mTextViewTitle;
	private HorizontalScrollView horizontalScrollView;

	private TextView mGroup,mOperation,mNotice,mLinescons,mTender,mToronto,mLand,mConstru;
	private ViewPager mViewPager;
	private List<Fragment> fragments;
	private FragmentStatePagerAdapter mAdapter;
	private LayoutParams viewParams;
	//传递类型
	private Bundle bundle;
	private int NUM_TYPE=R.string.group_express;

	private Fragment groupFrag,operationFrag,noticeFrag,TenderFrag,landFrag,torontoFrag;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_main);
		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hor_scollview);
		mTextViewTitle = (TextView) findViewById(R.id.tv_top_left_title);
		mTextViewTitle.setText(R.string.block_news);
		mImgBtnBack = (ImageButton)findViewById(R.id.iv_top_left_back);
		mImgBtnBack.setOnClickListener(this);

		mGroup = (TextView) findViewById(R.id.tv_group);
		mOperation = (TextView) findViewById(R.id.tv_operation);
		mNotice = (TextView) findViewById(R.id.tv_notice);
		mLinescons = (TextView) findViewById(R.id.tv_metro_pic);
		mTender = (TextView) findViewById(R.id.tv_tender);
		mLand = (TextView) findViewById(R.id.tv_land);
		mToronto = (TextView) findViewById(R.id.tv_toronto);
		mConstru = (TextView) findViewById(R.id.tv_pro_constru);
		mGroup.setOnClickListener(this);
		mOperation.setOnClickListener(this);
		mNotice.setOnClickListener(this);
		mLinescons.setOnClickListener(this);
		mTender.setOnClickListener(this);
		mLand.setOnClickListener(this);
		mToronto.setOnClickListener(this);
		mConstru.setOnClickListener(this);

		//设置Activity给Fragment传递的值
		fragments = new ArrayList<Fragment>();
		//集团快讯
		fragments.add(GroupFragment.newsInstance(NumUtil.URL_LOCAL_NEWS_JTKX, R.string.group_express, 2));
		//地铁运营
		fragments.add(GroupFragment.newsInstance(NumUtil.URL_LOCAL_NEWS_DTYY, R.string.metro_operation, 3));
		//通知公告
		fragments.add(TenderFragment.newsInstance(NumUtil.URL_LOCAL_NEWS_TZGG,NumUtil.ITEM_LOGO_NOTICE,R.string.block_notice));
		//在建线路
		fragments.add(CircuitFragment.newsInstance(NumUtil.URL_LOCAL_LINE_ZJXL,NumUtil.ITEM_LOGO_PROLINE,R.string.metro_pic));
		//招标中标
		fragments.add(TenderFragment.newsInstance(NumUtil.URL_LOCAL_NEWS_ZBZB,NumUtil.ITEM_LOGO_BIAO,R.string.invitation_tender));
		//土地开发
		fragments.add(TenderFragment.newsInstance(NumUtil.URL_LOCAL_NEWS_TDKF,NumUtil.ITEM_LOGO_DEVELOP,R.string.pro_land));
		//建设公司
		fragments.add(TenderFragment.newsInstance(NumUtil.URL_LOCAL_NEWS_JSGS,NumUtil.ITEM_LOGO_COMPANY,R.string.pro_toronto));
		//地铁建设
		fragments.add(TenderFragment.newsInstance(NumUtil.URL_LOCAL_NEWS_DTJS,NumUtil.ITEM_LOGO_PRO,R.string.pro_constru));

		mViewPager = (ViewPager) findViewById(R.id.vp_viewpager); 
		mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public Fragment getItem(int item) {
				return fragments.get(item);
			}
		};
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		setItemIndex(0,mGroup);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_group:
			setItemIndex(0,mGroup);
			break;
		case R.id.tv_operation:
			setItemIndex(1,mOperation);
			break;
		case R.id.tv_notice:
			setItemIndex(2,mNotice);
			break;
		case R.id.tv_metro_pic:
			setItemIndex(3,mLinescons);
			break;
		case R.id.tv_tender:
			setItemIndex(4,mTender);
			break;
		case R.id.tv_land:
			setItemIndex(5,mLand);
			break;
		case R.id.tv_toronto:
			setItemIndex(6,mToronto);
			break;
		case R.id.tv_pro_constru:
			setItemIndex(7,mConstru);
			break;
		case R.id.iv_top_left_back:
			NewsActivity.this.finish();
		default:
			break;
		}
	}

	/**
	 * 设置新闻类型点击的后的样式
	 * @param item
	 * @param tv
	 */
	public void setItemIndex(int item,TextView tv){
		reStartTV();
		tv.setTextSize(20.0f);
		tv.setTextColor(getResources().getColor(R.color.activity_bg_color_blue));
		if(item!=0){
			horizontalScrollView.scrollTo((item+1)*120, horizontalScrollView.getHeight());
		}else{
			horizontalScrollView.scrollTo(0, horizontalScrollView.getHeight());
		}
		mViewPager.setCurrentItem(item);
	}

	public void reStartTV(){
		mGroup.setTextSize(16.0f);
		mOperation.setTextSize(16.0f);
		mNotice.setTextSize(16.0f);
		mLinescons.setTextSize(16.0f);
		mTender.setTextSize(16.0f);
		mLand.setTextSize(16.0f);
		mToronto.setTextSize(16.0f);
		mConstru.setTextSize(16.0f);
		mGroup.setTextColor(Color.GRAY);
		mOperation.setTextColor(Color.GRAY);
		mNotice.setTextColor(Color.GRAY);
		mLinescons.setTextColor(Color.GRAY);
		mTender.setTextColor(Color.GRAY);
		mToronto.setTextColor(Color.GRAY);
		mLand.setTextColor(Color.GRAY);
		mConstru.setTextColor(Color.GRAY);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onPageScrollStateChanged(int item) {
		int clickId = mViewPager.getCurrentItem();
		switch (clickId) {
		case 0:
			setItemIndex(0,mGroup);
			break;
		case 1:
			setItemIndex(1,mOperation);
			break;
		case 2:
			setItemIndex(2,mNotice);
			break;
		case 3:
			setItemIndex(3,mLinescons);
			break;
		case 4:
			setItemIndex(4,mTender);
			break;
		case 5:
			setItemIndex(5,mLand);
			break;
		case 6:
			setItemIndex(6,mToronto);
			break;
		case 7:
			setItemIndex(7,mConstru);
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {

	}
}