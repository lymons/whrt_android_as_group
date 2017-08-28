package com.mieasy.whrt_app_android_4.act.boot;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.mieasy.whrt_app_android_4.R;

/**
 * Created by alan on 17-1-5.
 */
public class SelectSiteLineActivity extends FragmentActivity {
    private RadioButton mLineButton;
    private RadioButton mLetterButton;
    private ImageButton mImageButton;

    /**
     * Fragment管理器
     */
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private SelectSiteFragmentLine mLineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.site_select_line_activity);
        /**
         * 默认站点为主页面
         */
        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mLineFragment = new SelectSiteFragmentLine();
        mTransaction.replace(R.id.siteselectfragment, mLineFragment);
        mTransaction.commitAllowingStateLoss();
        intiview();
    }

    private void intiview() {
        mImageButton = (ImageButton) findViewById(R.id.backbutn_line);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectSiteLineActivity.this.finish();
            }
        });

        mLineButton = (RadioButton) findViewById(R.id.sitebtn_line);
        mLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getFragmentManager();
                mTransaction = mFragmentManager.beginTransaction();
                mLineFragment = new SelectSiteFragmentLine();
                mTransaction.replace(R.id.sitecollectionfragment, mLineFragment);
                mTransaction.commit();
            }
        });
        mLetterButton = (RadioButton) findViewById(R.id.linebtn_line);
        mLetterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getFragmentManager();
                mTransaction = mFragmentManager.beginTransaction();
                mLineFragment = new SelectSiteFragmentLine();
                mTransaction.replace(R.id.sitecollectionfragment, mLineFragment);
                mTransaction.commit();
            }
        });

    }


}
