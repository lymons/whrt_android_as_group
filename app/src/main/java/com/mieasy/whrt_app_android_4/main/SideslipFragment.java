package com.mieasy.whrt_app_android_4.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mieasy.whrt_app_android_4.R;


public class SideslipFragment extends Fragment {
    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView==null){
            mView=inflater.inflate(R.layout.mainfrag_layout,container,false);
        }
        return mView;
    }
}
