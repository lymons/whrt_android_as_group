package com.mieasy.whrt_app_android_4.act.boot;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.adapter.SiteSelectLineAdapter;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.pinyinview.ContactSortModel;
import com.mieasy.whrt_app_android_4.pinyinview.EditTextWithDel;
import com.mieasy.whrt_app_android_4.pinyinview.PinyinComparator;
import com.mieasy.whrt_app_android_4.pinyinview.PinyinUtils;
import com.mieasy.whrt_app_android_4.pinyinview.SideBarLine;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alan on 17-1-5.
 */
public class SelectSiteFragmentLine extends Fragment {

    private ListView lv_site;
    private LiteOrm liteOrm;
    private ImageView iv_top_left_back;

    private SideBarLine sideBarLine;
    private TextView dialog;
    private EditTextWithDel mEtSearchName;

    private SiteSelectLineAdapter adapter;
    private List<ContactSortModel> sourceDateList;
    private Map<String, String[]> details= new HashMap<String, String[]>();
    private View view;
    int resultCode = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_selectsite_line, container, false);
        liteOrm = ContentApplication.getInstance().liteOrm;
        initView();
        //查询所有的站点名
        ArrayList<String> selectSiteName = LiteOrmServices.getSelectSiteName(liteOrm);
        sourceDateList = filledData(selectSiteName);
//        HashMap<String, String[]> stationName = LiteOrmServices.getStationName(liteOrm);
//        details = filledDates(stationName);
        initDatas();
        initEvents();

        //Collections.sort(sourceDateList, new PinyinComparator());
        adapter = new SiteSelectLineAdapter(getActivity(), sourceDateList);
        lv_site.setAdapter(adapter);
        return view;
    }

    private void initView() {
        lv_site = (ListView) view.findViewById(R.id.lv_site_line);

        mEtSearchName = (EditTextWithDel) view.findViewById(R.id.et_searchs);
        sideBarLine = (SideBarLine) view.findViewById(R.id.sidrbars);
        dialog = (TextView) view.findViewById(R.id.dialog_line);
    }
    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ContactSortModel> mSortList = new ArrayList<ContactSortModel>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = sourceDateList;
        } else {
            mSortList.clear();
            for (ContactSortModel sortModel : sourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }




//    private HashMap<String, String[]> filledDates(HashMap<String, String[]> stationName) {
//        List<ContactSortLine> mLineList = new ArrayList<ContactSortLine>();
//        ArrayList<String> indexString = new ArrayList<String>();
//        ContactSortLine lineComparator = new ContactSortLine();
//        for (int j = 0;j<stationName.size();j++) {
//            lineComparator.setName(stationName.get(j));
//            mLineList.add(lineComparator);
//        }
//        return (HashMap<String, String[]>) mLineList;
//    }


    private List<ContactSortModel> filledData(ArrayList<String> selectSiteName) {
        List<ContactSortModel> mSortList = new ArrayList<ContactSortModel>();
        ContactSortModel sortModels = new ContactSortModel();
        ArrayList<String> indexString = new ArrayList<String>();
        for (String station:selectSiteName) {
            String stationName = station;
            Integer stationLineID = LiteOrmServices.getStationLineID(liteOrm, stationName);
            String line_id = String.valueOf(stationLineID);
            Log.e("ADD","lineid:"+line_id);
            String sortString = line_id.substring(0, 1).toUpperCase();
            if (sortString.matches("1号线,2号线,3号线,4号线,6号线")) {
                sortModels.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModels);
        }
//        for (int i = 0; i < selectSiteName.size(); i++) {
//            ContactSortModel sortModel = new ContactSortModel();
//            sortModel.setName(selectSiteName.get(i));
//            String pinyin = PinyinUtils.getPingYin(selectSiteName.get(i));
//            String sortString = pinyin.substring(0, 1).toUpperCase();
//            if (sortString.matches("1号线,2号线,3号线,4号线,6号线")) {
//                sortModel.setSortLetters(sortString.toUpperCase());
//                if (!indexString.contains(sortString)) {
//                    indexString.add(sortString);
//                }
//            }
//            mSortList.add(sortModel);
//        }
        Collections.sort(indexString);
        sideBarLine.setIndexText(indexString);
        return mSortList;
    }

    private void initDatas() {
        sideBarLine.setTextView(dialog);
    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBarLine.setOnTouchingLetterChangedListeners(new SideBarLine.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lv_site.setSelection(- + 1);
                }

            }
        });
        //ListView的点击事件
        lv_site.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), ((ContactSortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();

                Intent mIntent = new Intent();
                mIntent.putExtra("name", ((ContactSortModel) adapter.getItem(position)).getName());
                // 设置结果，并进行传送
                getActivity().setResult(resultCode, mIntent);
                getActivity().finish();

            }
        });

        //根据输入框输入值的改变来过滤搜索
        mEtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
