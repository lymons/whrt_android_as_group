package com.mieasy.whrt_app_android_4.act.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.pro.NoBarActivity;
import com.mieasy.whrt_app_android_4.bean.UrlConstance;
import com.mieasy.whrt_app_android_4.main.MainActivity;
import com.mieasy.whrt_app_android_4.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends NoBarActivity {

    private EditText loginNick;//用户昵称
    private EditText email;//注册邮箱
    private EditText tv_user_pass, tv_again_password, tv_phone_number;//注册密码
    private ImageButton iv_top_left_backl;
    private Button registBtn;//注册
    private RequestQueue mQueue;
    private String resultCode;
    private TextView mTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        RequestQueue是一个请求队列对象
        mQueue = Volley.newRequestQueue(this);
        mQueue.start();

        initView();
    }

    private void initView() {
        mTextTitle = (TextView) findViewById(R.id.tv_top_left_title);
        mTextTitle.setText("注册");
        loginNick = (EditText) findViewById(R.id.tv_user_name);
        tv_user_pass = (EditText) findViewById(R.id.tv_user_pass);
        tv_again_password = (EditText) findViewById(R.id.tv_again_password);
        tv_phone_number = (EditText) findViewById(R.id.tv_phone_number);
        email = (EditText) findViewById(R.id.tv_email_name);
        iv_top_left_backl = (ImageButton) findViewById(R.id.iv_top_left_back);//lin_top_left_home
        iv_top_left_backl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        registBtn = (Button) findViewById(R.id.btn_reg_submit);
        registBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //获得用户输入的信息
                String nick = loginNick.getText().toString();
                String emailStr = email.getText().toString();
                String passwordStr = tv_user_pass.getText().toString();
                String password_again = tv_again_password.getText().toString();
                String phone = tv_phone_number.getText().toString();
                if (!TextUtils.isEmpty(nick) &&
                        !TextUtils.isEmpty(emailStr)
                        && !TextUtils.isEmpty(passwordStr) && !TextUtils.isEmpty(password_again) && !TextUtils.isEmpty(phone)) {
//                   用户名和密码的长度判断
                    if (2 <= nick.length() && nick.length() <= 20 && 6 <= passwordStr.length() && passwordStr.length() <= 16) {
                        if (Utils.isEmail(emailStr) && Utils.isMobile(phone)) {//验证邮箱和手机号格式是否符合
                            if (password_again.equals(passwordStr)) {
                                HashMap map = new HashMap<String, String>();
                                map.put("Name", nick);
                                map.put("Pass", passwordStr);
                                map.put("OPass", password_again);
                                map.put("TelNum", phone);
                                map.put("Email", emailStr);
                                loginRequest(Request.Method.POST, UrlConstance.REGISTER_URL2, map);
                            } else {
                                Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "输入邮箱或手机号有误", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "账号或密码长度不够", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "输入信息不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginRequest(int post, final String appUrl, final HashMap map) {
//        创建一个StringRequest对象
        StringRequest stringRequest = new StringRequest(
                post,//方法
                appUrl,//请求地址
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (map != null) {
//
                            try {
//                                将s这个服务器返回的字符串转化为json对象 去和对象中的code进行比较
//
                                JSONObject jsonObject = new JSONObject(s);
                                resultCode = jsonObject.getString("code");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (resultCode.equals("1")) {//                                    UserBaseInfo info = new Gson().fromJson(s, new TypeToken<UserBaseInfo>(){
//                                    }.getType());
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();

                            } else if (resultCode.equals("1006")) {
                                Toast.makeText(getApplicationContext(), "用户名被占用", Toast.LENGTH_LONG).show();

                            } else if (resultCode.equals("1007")) {
                                Toast.makeText(getApplicationContext(), "手机号被占用", Toast.LENGTH_LONG).show();

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "参数为空", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        mQueue.add(stringRequest);

    }
}
