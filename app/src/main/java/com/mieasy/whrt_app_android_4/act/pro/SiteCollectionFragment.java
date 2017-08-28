package com.mieasy.whrt_app_android_4.act.pro;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.nav.ShowOutSideActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.SiteCollect;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16-12-13.
 */
public class SiteCollectionFragment extends Fragment {
    private static final String TAG = "SiteCollectionFragment";
    private ListView mListView;
    private View view;
    private LiteOrm liteOrm_add,liteOrm;
    private List<SiteCollect> list_stationname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         liteOrm_add = ContentApplication.getInstance().liteOrm_add;
         liteOrm = ContentApplication.getInstance().liteOrm;
        if (view == null) {
            view = inflater.inflate(R.layout.sitefragment, container, false);
        }
        mListView = (ListView) view.findViewById(R.id.site_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SiteCollect siteCollect1 = list_stationname.get(position);
                String stationId = siteCollect1.getStationId();
                Stations stationByStationID = LiteOrmServices.getStationByStationID(liteOrm, Integer.parseInt(stationId));
                jumpActivity(stationByStationID);
            }
        });
        initview();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        initview();
    }
    public void initview() {
        if (ContentApplication.isnotfirstcometo) {
            //查找数据
            getData();
        } else {
            ArrayList<SiteCollect> query = liteOrm_add.query(SiteCollect.class);
            if (query.size() > 0) {
                //不插入数据
            } else {
                //插入数据
                list_stationname = new ArrayList<SiteCollect>();
                final SiteCollect site = new SiteCollect();
                site.setStationId("29");
                site.setStationName("汉口北");
                site.setType("0");

                SiteCollect sites = new SiteCollect();
                sites.setStationId("33");
                sites.setStationName("汉口火车站");
                sites.setType("0");
                list_stationname.add(site);
                list_stationname.add(sites);
                liteOrm_add.cascade().insert(list_stationname);
                getData();
            }
            ContentApplication.isnotfirstcometo = true;
        }
    }

    public void getData() {
        final List<SiteCollect> siteCollect = liteOrm_add.query(SiteCollect.class);
        list_stationname = new ArrayList<SiteCollect>();
        for (SiteCollect data : siteCollect){
            String type = data.getType();
            if (type.equals("0")) {
                list_stationname.add(data);
            }
        }
        SiteFragmentAdepter siteFragmentAdepter = new SiteFragmentAdepter(getActivity(), list_stationname);
        mListView.setAdapter(siteFragmentAdepter);

    }

    /**
     * 界面跳转
     * @param siteCollect

     */
    public void jumpActivity(Stations siteCollect){
        if(siteCollect!=null){
            Intent intent = new Intent(getActivity(),ShowOutSideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(NumUtil.JUMP_BUNDLE, siteCollect);
            intent.putExtra(NumUtil.JUMP_INTENT, bundle);
            startActivity(intent);
        }
    }
}
