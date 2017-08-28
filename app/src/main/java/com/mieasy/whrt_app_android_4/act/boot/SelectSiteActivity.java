package com.mieasy.whrt_app_android_4.act.boot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.adapter.SiteSelectAdapter;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.pinyinview.ContactSortModel;
import com.mieasy.whrt_app_android_4.pinyinview.EditTextWithDel;
import com.mieasy.whrt_app_android_4.pinyinview.PinyinComparator;
import com.mieasy.whrt_app_android_4.pinyinview.PinyinUtils;
import com.mieasy.whrt_app_android_4.pinyinview.SideBar;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectSiteActivity extends Activity implements View.OnClickListener {

    ListView lv_site;
    LiteOrm liteOrm;
    ImageView iv_top_left_back;


    private SideBar sideBar;
    private TextView dialog;
    private EditTextWithDel mEtSearchName;

    SiteSelectAdapter adapter;
    List<ContactSortModel> sourceDateList;

    int resultCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectsite);
        liteOrm = ContentApplication.getInstance().liteOrm;

        initView();
        ArrayList<String> selectSiteName = LiteOrmServices.getSelectSiteName(liteOrm);
        Log.e("LIST","line:" +selectSiteName.get(2));
        sourceDateList = filledData(selectSiteName);
       // sourceDateList.get(1);
        Log.e("LIST","line:" +sourceDateList.get(1));
        initDatas();
        initEvents();

        Collections.sort(sourceDateList, new PinyinComparator());
        adapter = new SiteSelectAdapter(this, sourceDateList);
        lv_site.setAdapter(adapter);
    }

    private void initView() {
        lv_site = (ListView) findViewById(R.id.lv_site);
        iv_top_left_back = (ImageView) findViewById(R.id.iv_top_left_back);

        mEtSearchName = (EditTextWithDel) findViewById(R.id.et_search);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        iv_top_left_back.setOnClickListener(this);
    }

    private void initDatas() {
        sideBar.setTextView(dialog);
    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lv_site.setSelection(position + 1);
                }
            }
        });

        //ListView的点击事件
        lv_site.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplication(), ((ContactSortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();

                Intent mIntent = new Intent();
                mIntent.putExtra("name", ((ContactSortModel) adapter.getItem(position)).getName());
                // 设置结果，并进行传送
                setResult(resultCode, mIntent);
                finish();

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

    private List<ContactSortModel> filledData(ArrayList<String> date) {
        List<ContactSortModel> mSortList = new ArrayList<ContactSortModel>();
        ArrayList<String> indexString = new ArrayList<String>();
        Log.e("LIST","line:" +date);
        for (String station:date) {
            String stationName = station;
            Integer stationLineID = LiteOrmServices.getStationLineID(liteOrm, stationName);
            Log.e("LIST","lines:" +stationLineID);
        }
        for (int i = 0; i < date.size(); i++) {
            ContactSortModel sortModel = new ContactSortModel();
            sortModel.setName(date.get(i));
            Log.e("LIST","line:" +date.get(i));

            String pinyin = PinyinUtils.getPingYin(date.get(i));
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            resultCode = -1;
            setResult(resultCode);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_top_left_back:
                resultCode = -1;
                setResult(resultCode);
                this.finish();
                break;
        }
    }
}
