package com.mieasy.whrt_app_android_4.act.pro;

import android.content.Context;
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
 * Created by alan on 16-11-28.
 * 在收藏线路里面的适配器来适应
 */
public class LineCollectFragmentAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<SiteCollect> mLineCollectList;

    public LineCollectFragmentAdapter(Context context, List<SiteCollect> linecollection) {
        this.mInflater = LayoutInflater.from(context);
        this.mLineCollectList=linecollection;
    }

    @Override
    public int getCount() {
        return mLineCollectList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLineCollectList.get(position);
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
            view = mInflater.inflate(R.layout.list_itme_linecollecfragment,null);
            viewHolder.tvImg = (ImageView) view.findViewById(R.id.listsite_images);
            viewHolder.mStaring = (TextView) view.findViewById(R.id.linelist_staring);
            viewHolder.mStaringTitle = (TextView) view.findViewById(R.id.list_line_starting);
            viewHolder.mFinal = (TextView) view.findViewById(R.id.linelist_final);
            viewHolder.mFinalTitle = (TextView) view.findViewById(R.id.list_line_final);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) view.getTag();
        }
        viewHolder.tvImg.setImageResource(R.drawable.collect);
        viewHolder.mStaring.setText("起：");
        viewHolder.mStaring.setTextSize(18.0f);
        viewHolder.mStaringTitle.setText(mLineCollectList.get(position).getStationName());
        viewHolder.mStaringTitle.setTextSize(18.0f);
        viewHolder.mFinal.setText("终：");
        viewHolder.mFinal.setTextSize(18.0f);
        viewHolder.mFinalTitle.setText(mLineCollectList.get(position).getStationto());
        viewHolder.mFinalTitle.setTextSize(18.0f);
        return view;
    }

    class ViewHodler{
        private ImageView tvImg;
        private TextView mStaringTitle;
        private TextView mStaring;
        private TextView mFinal;
        private TextView mFinalTitle;
    }
}
