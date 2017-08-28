package com.mieasy.whrt_app_android_4.main;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.ask.AskActivity;
import com.mieasy.whrt_app_android_4.act.boot.MapNavActivity;
import com.mieasy.whrt_app_android_4.act.boot.NearbyStationActivity;
import com.mieasy.whrt_app_android_4.act.nav.NavActivity;
import com.mieasy.whrt_app_android_4.act.news.NewsActivity;
import com.mieasy.whrt_app_android_4.act.pro.SiteCollectionActivity;

import java.util.ArrayList;
import java.util.List;


public class BlockImgFragment extends Fragment implements OnClickListener {
    private View view;
    private LinearLayout.LayoutParams linParams;
    private LinearLayout linTop,linBottom;		//上、下网格
    private ImageButton imgSet,imgNews,imgPro,imgBoot,imgNav,imgAsk;	 //网格ImageButton
    private int currentW,currentH;		//屏幕宽、高
    private List<ImageButton> imageButtonList;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	view = inflater.inflate(R.layout.layout_block_img, null);
		intView();
		return view;
    }

	private void intView() {
		DisplayMetrics outMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		currentW = outMetrics.widthPixels;
		currentH = outMetrics.heightPixels-60;

		imageButtonList = new ArrayList<ImageButton>();
		linTop = (LinearLayout) view.findViewById(R.id.lin_block_first);
		linBottom = (LinearLayout) view.findViewById(R.id.lin_block_second);
		linParams = new LayoutParams(currentW,(int)(currentH/5));		//设置上方网格的大小
		linParams.setMargins(0, currentH/80, 0, 0);						//设置上方网格的上下间距
		linTop.setLayoutParams(linParams);
		linTop.setPadding(currentW/24, 0, currentW/24, 0);				//设置上方网格的左右间隙
		linParams = new LayoutParams(currentW,(int)(currentH/5));		//设置下方网格的大小
		linBottom.setLayoutParams(linParams);
		linBottom.setPadding(currentW/24, 0, currentW/24, 0);			//设置下方网格的左右间隙
		
		imgSet = (ImageButton) view.findViewById(R.id.ib_img_set);	imageButtonList.add(imgSet);
		imgNews = (ImageButton) view.findViewById(R.id.ib_img_news);	imageButtonList.add(imgNews);
		imgPro = (ImageButton) view.findViewById(R.id.ib_img_pros);     imageButtonList.add(imgPro);
		imgBoot = (ImageButton) view.findViewById(R.id.ib_img_boot);		imageButtonList.add(imgBoot);
		imgNav = (ImageButton) view.findViewById(R.id.ib_img_nav);		imageButtonList.add(imgNav);
		imgAsk = (ImageButton) view.findViewById(R.id.ib_img_ask);	imageButtonList.add(imgAsk);
		
		linParams = new LayoutParams((int)(currentW/4.6),(int)(currentW/4.6));
		linParams.setMargins(currentW/18, 0, currentW/18, currentH/80);
		for(int i=0;i<imageButtonList.size();i++){
			imageButtonList.get(i).setLayoutParams(linParams);
			imageButtonList.get(i).setOnClickListener(this);
		}
		Bundle bundle = getArguments();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_img_set://通知
			startAct(NearbyStationActivity.class);
			break;
		case R.id.ib_img_news://新闻
			startAct(NewsActivity.class);
			break;
        case R.id.ib_img_pros://收藏
			startAct(SiteCollectionActivity.class);
			break;
		case R.id.ib_img_boot://引导
			startAct(MapNavActivity.class);
			break;
		case R.id.ib_img_nav://导航
			startAct(NavActivity.class);
			break;
		case R.id.ib_img_ask://问答
			startAct(AskActivity.class);
			break;
		default:
			break;
		}
	}

	//跳转
	public void startAct(Class cls){
		Intent intent = new Intent(getActivity(),cls);
		startActivity(intent);
	}
}