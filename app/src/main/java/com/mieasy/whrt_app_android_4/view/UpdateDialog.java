package com.mieasy.whrt_app_android_4.view;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.R.id;
import com.mieasy.whrt_app_android_4.view.ConfirmDialog.ClickListenerInterface;
import com.mieasy.whrt_app_android_4.view.UpdateDialog.ClickDialogUpdateInterface;
import com.mieasy.whrt_app_android_4.view.UpdateDialog.UpdateClickListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class UpdateDialog extends Dialog{
	private Context context;
	private TextView tvContent;
	private Button upCancel,upIgnore;
	private String upDetail;
	
	public UpdateDialog(Context context,String upDetail) {
		super(context);
		this.context = context;
		this.upDetail = upDetail;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDialog();
	}
	
	public void initDialog(){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.update_dialog, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		
		tvContent = (TextView) view.findViewById(R.id.update_content);
		tvContent.setText(upDetail);
		upCancel = (Button) view.findViewById(id.update_id_ok);
		upCancel.setOnClickListener(new UpdateClickListener());
		upIgnore = (Button) view.findViewById(id.update_id_cancel);
		upIgnore.setOnClickListener(new UpdateClickListener());
	}

	private ClickDialogUpdateInterface clickDialogUpdateInterface;

	public interface ClickDialogUpdateInterface {
		public void doUpdate();
		public void doCancel();
	}
	
	public void setClicklistener(ClickDialogUpdateInterface clickDialogUpdateInterface) {
		this.clickDialogUpdateInterface = clickDialogUpdateInterface;
	}
	
	public class UpdateClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.update_id_ok:
				clickDialogUpdateInterface.doUpdate();
				break;
			case R.id.update_id_cancel:
				clickDialogUpdateInterface.doCancel();
				break;
			default:
				break;
			}
		}
	}
}
