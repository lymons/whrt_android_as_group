package com.mieasy.whrt_app_android_4.act.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.pro.NoBarActivity;

import static com.mieasy.whrt_app_android_4.act.login.LoginFragment.DATABASE;

public class ReviseActivity extends NoBarActivity implements View.OnClickListener {

    private TextView tv_top_left_title, re_name, re_phone, re_email, re_address,re_password,user_nickname;
    private LinearLayout lin_name, lin_phone, lin_email, lin_address,lin_password;
    private ImageButton iv_top_left_back;
    private Button btn_exit_user,btn_exit_update;

    LinearLayout change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise);
        initview();
        get_mage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_revise);
        initview();
        get_mage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_revise);
        initview();
        get_mage();
    }

    private void initview() {
        tv_top_left_title = (TextView) findViewById(R.id.tv_top_left_title);
        tv_top_left_title.setText("用户中心");
        re_name = (TextView) findViewById(R.id.re_name);
        re_phone = (TextView) findViewById(R.id.re_phone);
        re_password = (TextView) findViewById(R.id.repassword);
        re_email = (TextView) findViewById(R.id.re_email);
        re_address = (TextView) findViewById(R.id.re_address);
        lin_name = (LinearLayout) findViewById(R.id.lin_nickname);
        lin_phone = (LinearLayout) findViewById(R.id.lin_phone);
        lin_password = (LinearLayout) findViewById(R.id.lin_password);
        lin_email = (LinearLayout) findViewById(R.id.lin_email);
        lin_address = (LinearLayout) findViewById(R.id.lin_address);
        iv_top_left_back = (ImageButton) findViewById(R.id.iv_top_left_back);
        btn_exit_user = (Button) findViewById(R.id.btn_exit_user);
        btn_exit_update = (Button) findViewById(R.id.btn_exit_update);


        lin_name.setOnClickListener(this);
        lin_phone.setOnClickListener(this);
        lin_password.setOnClickListener(this);
        lin_email.setOnClickListener(this);
        lin_address.setOnClickListener(this);
        iv_top_left_back.setOnClickListener(this);
        btn_exit_user.setOnClickListener(this);
        btn_exit_update.setOnClickListener(this);


        user_nickname = (TextView)findViewById(R.id.user_nickname);
    }

    private void get_mage() {
        SharedPreferences sp = getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
        String r_name = sp.getString("name", "");
        String r_phone = sp.getString("phone", "");
        String r_password = sp.getString("PassWord","");
        re_name.setText(r_name);
        re_phone.setText(r_phone);
        re_password.setText(r_password);
        re_email.setText(sp.getString("email", ""));
        re_address.setText(sp.getString("add", ""));
        user_nickname.setText(sp.getString("Loginame",""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_top_left_back:
                finish();
                break;
            case R.id.lin_password:
//                Intent intentpassword = new Intent(ReviseActivity.this,Re_Password_Activity.class);
//                startActivity(intentpassword);
                break;
            case R.id.btn_exit_update:
                Intent itentupdate = new Intent(ReviseActivity.this,UpdateUserInfoActivity.class);
                startActivity(itentupdate);
                break;
            case R.id.btn_exit_user:
                SharedPreferences sp = getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Intent ia = new Intent(ReviseActivity.this, LoginActivity.class);
                ia.putExtra("islog", true);
                startActivity(ia);
                finish();
                break;
            default:
                break;
        }
    }
}
