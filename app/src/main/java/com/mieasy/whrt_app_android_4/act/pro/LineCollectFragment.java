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
import com.mieasy.whrt_app_android_4.act.boot.PathActivity;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.SiteCollect;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16-12-14.
 */
public class LineCollectFragment extends Fragment{
    private static final String TAG = "LineCollectionFragment";
    private View view;
    private Bundle bundle;
    private ListView mListView;
    private LiteOrm liteOrm_add,liteOrm;
    private ArrayList<SiteCollect> list_stationname;
    private ArrayList<SiteCollect> query_line;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        liteOrm_add = ContentApplication.getInstance().liteOrm_add;
        liteOrm = ContentApplication.getInstance().liteOrm;
        if (view == null) {
            view = inflater.inflate(R.layout.right_fragment, container, false);
        }
        mListView = (ListView) view.findViewById(R.id.lines_constur);
        initview();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        initview();
    }
    private void initview() {
        if (ContentApplication.lineisnotfirstcometo) {
            getDatatwo();
        } else {
            ArrayList<SiteCollect> query = liteOrm_add.query(SiteCollect.class);
             query_line = new ArrayList<SiteCollect>();
            if (query.size() > 0) {
                list_stationname = new ArrayList<SiteCollect>();
                for (SiteCollect data : query) {
                    String type = data.getType();
                    if (type.equals("1")) {
                        query_line.add(data);
                    }
                }
                if(query_line.size()>0){
                    //有线路数据  显示数据
                    getData(query_line);
                }else{
                    //没有线路数据  插入数据后显示数据
                    add();
                    getData(query_line);
                }
            }else{
                //直接插入数据显示
                add();
                getData(query_line);
            }
            ContentApplication.lineisnotfirstcometo = true;
        }
    }

    private void getDatatwo() {
        final List<SiteCollect> siteCollect = liteOrm_add.query(SiteCollect.class);
        list_stationname = new ArrayList<SiteCollect>();
        for (SiteCollect data : siteCollect){
            String type = data.getType();
            if (type.equals("1")) {
                list_stationname.add(data);
            }
        }
        getData(list_stationname);
    }

    //查找数据
    public void getData(final ArrayList<SiteCollect> query_line) {
        LineCollectFragmentAdapter lineCollectFragmentAdapter = new LineCollectFragmentAdapter(getActivity(),list_stationname);
        mListView.setAdapter(lineCollectFragmentAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SiteCollect siteCollect1 = query_line.get(position);
                String stationId = siteCollect1.getStationId();
                String stationtoId = siteCollect1.getStationtoId();
                Stations stationStart = LiteOrmServices.getStationByStationID(liteOrm, Integer.parseInt(stationId));
                Stations stationStop =  LiteOrmServices.getStationByStationID(liteOrm, Integer.parseInt(stationtoId));
                findPathByPoint(stationStart,stationStop);
            }
        });
    }

    public void add() {
        list_stationname = new ArrayList<SiteCollect>();
        final SiteCollect site = new SiteCollect();
        site.setStationId("29");
        site.setStationName("汉口北");
        site.setStationtoId("33");
        site.setStationto("汉口火车站");
        site.setType("1");

        SiteCollect sites = new SiteCollect();
        sites.setStationId("33");
        sites.setStationName("汉口火车站");
        sites.setStationtoId("29");
        sites.setStationto("汉口北");
        sites.setType("1");
        list_stationname.add(site);
        list_stationname.add(sites);
        liteOrm_add.cascade().insert(list_stationname);
    }

    //查找路线
    public void findPathByPoint(Stations stationStart, Stations stationStop){
        if(stationStart!=null&&stationStop!=null){
            Intent intent = new Intent(getActivity(),PathActivity.class);
            bundle = new Bundle();
            bundle.putParcelable(NumUtil.STATIONS_START, stationStart);
            bundle.putParcelable(NumUtil.STATIONS_STOP, stationStop);
//            bundle.putBoolean("addsite",true);
//            bundle.putString("id", String.valueOf(id));
            intent.putExtra(NumUtil.STATIONS, bundle);
            startActivity(intent);
        }
    }
}
