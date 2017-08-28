package com.mieasy.whrt_app_android_4.act.pro;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.mieasy.whrt_app_android_4.R;

/**
 * Created by alan on 16-11-21.
 * 收藏的Activity主界面
 */
public class SiteCollectionActivity extends FragmentActivity {
    private RadioButton siteButton;
    private RadioButton lineButton;
    private ImageButton mImageButton;

    /**
     * Fragment管理器
     */
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    /**
     * 两个Fragment
     */
    private SiteCollectionFragment mSiteFragment;
    private LineCollectFragment mLineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_site_collection);
        /**
         * 默认站点为主页面
         */
        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mSiteFragment = new SiteCollectionFragment();
        mTransaction.replace(R.id.sitecollectionfragment, mSiteFragment);
        mTransaction.commitAllowingStateLoss();
        intiView();
    }

    private void intiView() {
        mImageButton = (ImageButton) findViewById(R.id.backbutn);
        mImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SiteCollectionActivity.this.finish();
            }
        });

        siteButton = (RadioButton) findViewById(R.id.sitebtn);
        siteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityToLineFragment();
            }
        });

        lineButton = (RadioButton) findViewById(R.id.linebtn);
        lineButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getFragmentManager();
                mTransaction = mFragmentManager.beginTransaction();
                mLineFragment = new LineCollectFragment();
                mTransaction.replace(R.id.sitecollectionfragment, mLineFragment);
                mTransaction.commit();
            }
        });
    }
    private void ActivityToLineFragment() {
        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mSiteFragment = new SiteCollectionFragment();
        mTransaction.replace(R.id.sitecollectionfragment, mSiteFragment);
        mTransaction.commit();
        //mTransaction.commitAllowingStateLoss();
    }
}

