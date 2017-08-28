package com.mieasy.whrt_app_android_4.act.nav;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.litesuits.orm.LiteOrm;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.bean.ExitInfos;
import com.mieasy.whrt_app_android_4.entity.Stations;
import com.mieasy.whrt_app_android_4.services.LiteOrmServices;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExitInfosFragment extends Fragment {

    private static final String TAG = ExitInfosFragment.class.getSimpleName();
    private View view;
    private Stations stations;
    private ListView lv_exitinfo;
    private List<ExitInfos> exitInfo;

    private LiteOrm liteOrm;
    private ScrollView scrollview;
    private TextView tv_showInfo;
    private ImageView mImageView;

    LinearLayout ll_one;
    HashMap<String, List<ExitInfos>> exitListtwo = new HashMap<String, List<ExitInfos>>();
    List<ExitInfos> list = new ArrayList<ExitInfos>();
    List<String> code = new ArrayList<String>();
    List<String> code_new = new ArrayList<String>();
    List<String> codeSize = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exitinfo_fragment, null);
        liteOrm = ContentApplication.getInstance().liteOrm;
        getBundleInfo();
        getData();
        initView();
        return view;
    }

    private void getData() {
        Log.e(TAG, "stations.getStationId():" + stations.getStationId());
        exitInfo = LiteOrmServices.getExitInfoByStationID(liteOrm, stations.getStationId());
    }

    private void initView() {

        ll_one = (LinearLayout) view.findViewById(R.id.ll_one);
        scrollview = (ScrollView) view.findViewById(R.id.scrollview);
        tv_showInfo = (TextView) view.findViewById(R.id.tv_showInfo);
        mImageView = (ImageView) view.findViewById(R.id.tv_imageview);

        if (exitInfo.size() == 0) {
            scrollview.setVisibility(View.GONE);
            tv_showInfo.setVisibility(View.VISIBLE);
            mImageView.setVisibility(view.VISIBLE);
        } else {
            scrollview.setVisibility(View.VISIBLE);
            tv_showInfo.setVisibility(View.GONE);
            mImageView.setVisibility(View.GONE);

            for (int i = 0; i < exitInfo.size(); i++) {
                Log.e(TAG,"exitInfo.get(i).getExitCode()):"+exitInfo.get(i).getExitCode());
                String exitCode = exitInfo.get(i).getExitCode();
                String exitCodeInfo = exitInfo.get(i).getExitCode() + "出口-" + exitInfo.get(i).getInfoRoadName();

                code.add(exitCodeInfo);
            }
            Log.e(TAG, "code.size():" + code.size());

            for (int z = 0; z < code.size(); z++) {
                String str = code.get(z);
                if (!code_new.contains(str)) {
                    //添加数据
                    code_new.add(str);
                }
            }

            for (int i = 0; i < code_new.size(); i++) {
                String data = code_new.get(i);
                codeSize.add(data);
                Log.e(TAG, "data:" + data);
                for (int j = 0; j < exitInfo.size(); j++) {
                    if (data.equals(exitInfo.get(j).getExitCode() + "出口-" + exitInfo.get(j).getInfoRoadName())) {
                        Log.e(TAG,"codes:" +exitInfo.get(j).getInfoRoadName());
                        list.add(exitInfo.get(j));
                    }
                }
                exitListtwo.put(data, list);
                list = null;
                list = new ArrayList<ExitInfos>();
            }

            for (int m = 0; m < codeSize.size(); m++) {
                TextView textView1 = new TextView(getActivity());
                textView1.setText(codeSize.get(m) + "出口");
                Log.e(TAG,"code:" +codeSize.get(m));
                textView1.setTextSize(15);
                textView1.setTextColor(Color.parseColor("#ff000000"));
                textView1.setPadding(40, 20, 0, 20);
                textView1.setBackgroundColor(Color.parseColor("#F0F0F0"));
                //设置字体样式
                textView1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textView1.setGravity(Gravity.LEFT);
                ll_one.addView(textView1);
                List<ExitInfos> list = exitListtwo.get(codeSize.get(m));
                for (int n = 0; n < list.size(); n++) {
                    TextView textView2 = new TextView(getActivity());
                    if (list.get(n).getInfoDetialName().equals("0")) {
                       //当有的位置有０的时候把ｉｎｆｏ１的值赋给它
                        textView2.setText(exitInfo.get(n).getInfoRoadName());
                        Log.e(TAG,"name0:"+exitInfo.get(n).getInfoRoadName());
                    }else {
                        textView2.setText(list.get(n).getInfoDetialName());
                    }
                    textView2.setTextSize(15);
                    textView2.setTextColor(Color.parseColor("#ff000000"));
                    textView2.setPadding(60, 20, 0, 20);
                    textView2.setBackgroundColor(Color.parseColor("#ffffff"));
                    //设置字体样式
                    textView2.setGravity(Gravity.LEFT);
                    ll_one.addView(textView2);
                }
            }
        }

    }

    /**
     * 获取Activity传递过来的参数
     */
    private void getBundleInfo() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            stations = bundle.getParcelable(NumUtil.JUMP_BUNDLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exitInfo = null;
        ll_one.removeAllViews();
    }
}
