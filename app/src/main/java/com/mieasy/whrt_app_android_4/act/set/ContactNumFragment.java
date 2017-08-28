package com.mieasy.whrt_app_android_4.act.set;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.mieasy.whrt_app_android_4.R;

import java.util.Timer;

public class ContactNumFragment extends Fragment implements OnClickListener{
	private View view; 
	private TableRow mTableRowOne,mTableRowTwo,mTableRowThree,mTableRowFour;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.set_main_info_callnumber, null);
		initView();
		return view;
	}

	private void initView() {
		mTableRowOne = (TableRow) view.findViewById(R.id.call_row_one);
		mTableRowOne.setOnClickListener(this);
		mTableRowTwo = (TableRow) view.findViewById(R.id.call_row_two);
		mTableRowTwo.setOnClickListener(this);
//		mTableRowThree = (TableRow) view.findViewById(R.id.call_row_three);
//		mTableRowThree.setOnClickListener(this);
		mTableRowFour = (TableRow) view.findViewById(R.id.call_row_four);
		mTableRowFour.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.call_row_one:
			openWeb(R.string.call_web_httpurl);
			break;
		case R.id.call_row_two:
			CallNumber(R.string.call_server_num_info1);
			break;
//		case R.id.call_row_three:
//			CallNumber(R.string.call_server_num_info2);
//			break;
		case R.id.call_row_four:
			openWeb(R.string.call_weibo_url);
			break;
		default:
			break;
		}
	}

	public void openWeb(int rid){
		final Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(rid)));
		Timer timer = new Timer();  
		startActivity(it); //执行  
//		TimerTask task = new TimerTask(){
//			@Override
//			public void run() {
//			}
//		};
//		timer.schedule(task, 1); //10秒后  
	}

	public void CallNumber(int rid){
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+getResources().getString(rid)));
		startActivity(intent); //执行  
	}
}
