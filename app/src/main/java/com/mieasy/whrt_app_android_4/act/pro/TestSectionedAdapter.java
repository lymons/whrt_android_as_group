package com.mieasy.whrt_app_android_4.act.pro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;

import java.util.List;
import java.util.Map;

/**
 * Created by alan on 16-11-22.
 * 在使用的时候  可以使用hashmap 把list存起来
 * 通过section 和 position 进行数据展示
 * 也可以单独对section进行判断 展示不同的header\
 *
 */
public class TestSectionedAdapter extends SectionedBaseAdapter {
    private String[] mDatas;
    // 下方集合
    private List<String> mSections;
    // 将dates和list放到map中
    private Map<String, List<String>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;


//    public TestSectionedAdapter(Context context,String[] datas,List<String> sections) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        mDatas = datas;
//        mSections = sections;
//
//    }

    //section: header的位置数  ,position:普通item的位置数
    @Override
    public Object getItem(int section, int position) {
        return mDatas[position];
    }
    //获取item的id  第几个section的 position
    @Override
    public long getItemId(int section, int position) {
        return 0;
    }
    //header的总数量
    @Override
    public int getSectionCount() {
        return 1;
    }
    /*普通 item 的总数量
     *传参数过来
    */

    @Override
    public int getCountForSection(int section) {

        return section;
    }

    /**
     *标题下面的itme的显示17
     */
    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.list_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItems)).setText(" Item ");
        return layout;
    }

    /**
     *标题的显示
     */
    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
       ((TextView) layout.findViewById(R.id.textItem)).setText("收藏站点");

        return layout;
    }

}
//    getItemView(int section, int position, View convertView, ViewGroup parent);
//    // 普通item 加载view的方法 类似adapter中getview() 方法
//    getSectionHeaderView(int section, View convertView, ViewGroup parent);
//// header  item  加载view的方法 也类似adapter中getview() 方法