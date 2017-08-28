package com.mieasy.whrt_app_android_4.act.pro;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.bean.SiteCollect;

import java.util.List;

/**
 * Created by alan on 16-11-24.
 */
public class SiteFragmentAdepter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<SiteCollect> mStieList;

    public SiteFragmentAdepter(Context context, List<SiteCollect> stieList) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.mStieList = stieList;
    }

    @Override
    public int getCount() {
        return mStieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if(view == null) {
            viewHolder = new ViewHodler();
            view = mInflater.inflate(R.layout.list_itme_sitefragment,null);
            viewHolder.tvImg = (ImageView) view.findViewById(R.id.listsite_image);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.list_site_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) view.getTag();
        }
        viewHolder.tvImg.setImageResource(R.drawable.collect);//设置固定的图片
        viewHolder.tvTitle.setText(mStieList.get(position).getStationName());
        viewHolder.tvTitle.setGravity(Gravity.CENTER_VERTICAL);//textView中的字体垂直居中
        viewHolder.tvTitle.setTextSize(18.0f);//设置字体的大小
        return view;
    }
    class ViewHodler{
        private TextView tvTitle;
        private ImageView tvImg;
    }
}
