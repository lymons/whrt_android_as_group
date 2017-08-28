package com.mieasy.whrt_app_android_4.act.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.pro.NoBarActivity;
import com.mieasy.whrt_app_android_4.main.MainActivity;

public class LoginActivity extends NoBarActivity implements OnClickListener {
    private EditText mUser, mPass;

    private TextView mTextViewTitle;
    private ImageButton mImgBtnBack;

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mTextViewTitle = (TextView) findViewById(R.id.tv_top_left_title);
        mTextViewTitle.setText("登录");
        mImgBtnBack = (ImageButton) findViewById(R.id.iv_top_left_back);
        mImgBtnBack.setOnClickListener(this);
//		改用activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.view_content, new LoginFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_left_back:            //返回按钮
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
                break;
            default:
                break;
        }
    }
}
