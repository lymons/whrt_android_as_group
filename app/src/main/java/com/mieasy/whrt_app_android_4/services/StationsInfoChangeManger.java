package com.mieasy.whrt_app_android_4.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.bean.SiteCollection;
import com.mieasy.whrt_app_android_4.entity.Stations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16-11-28.
 */
public class StationsInfoChangeManger {
    private static Gson gson  = new Gson();

    /**
     * 根据站点信息的到转化的数据
     * @param liteOrm
     * @param stationsNume
     * @return
     */
//    public static List<SiteCollection> getAllStationsNume(LiteOrm liteOrm, Stations stationsNume) {
//        List<SiteCollection> siteCollectionList = new ArrayList<SiteCollection>();
//        Stations stations = LiteOrmServices.getStationsNameByStationsId(liteOrm,stationsNume);//获取所有的站点的名称
//       // List<SiteCollection> siteCollection = new Gson().fromJson(stations,new TypeToken<List<SiteCollection>>(){}.getType());
//        return null;
//    }
}
