package com.mieasy.whrt_app_android_4.act.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.pro.NoBarActivity;
import com.mieasy.whrt_app_android_4.bean.UrlConstance;
import com.mieasy.whrt_app_android_4.bean.UserBaseInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mieasy.whrt_app_android_4.act.login.LoginFragment.DATABASE;

/**
 * Created by alan on 16-12-26.
 */
public class Re_Password_Activity extends NoBarActivity {
    private static final String TAG =Re_Password_Activity.class.getSimpleName();
    private EditText old_password, update_password1, update_password2;
    private RequestQueue mQueue;
    private Button assign_btn;
    private String resultCode;
    private ImageButton iv_top_left_back;
    private TextView tv_top_left_title;
    private SharedPreferences sp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_email);
        mQueue = Volley.newRequestQueue(this);
        mQueue.start();

        sp = getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
        old_password = (EditText) findViewById(R.id.update_old_password);

        iv_top_left_back = (ImageButton) findViewById(R.id.iv_top_left_back);
        iv_top_left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_top_left_title = (TextView) findViewById(R.id.tv_top_left_title);
        tv_top_left_title.setText("修改密码");
        update_password1 = (EditText) findViewById(R.id.update1_password);
        update_password2 = (EditText) findViewById(R.id.update2_password);
        assign_btn = (Button) findViewById(R.id.assign);
        assign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cd = sp.getString("PassWord", "");
                String oldpassword = old_password.getText().toString();
                if (oldpassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "旧密码不能为空", Toast.LENGTH_LONG).show();
                } else if (!cd.equals(oldpassword)) {
                    Toast.makeText(getApplicationContext(), "旧密码不正确", Toast.LENGTH_LONG).show();
                }
                String password1 = update_password1.getText().toString();
                String password2 = update_password2.getText().toString();
                if (password1.equals("") || password2.equals("")) {
                    Toast.makeText(getApplicationContext(), "修改密码不能为空", Toast.LENGTH_LONG).show();
                } else if (password1.length()< 6 && password1.length() >16) {
                    Toast.makeText(getApplicationContext(), "密码应该在6到16位", Toast.LENGTH_LONG).show();
                } else if (!password1.equals(password2)) {
                    Toast.makeText(getApplicationContext(), "两次的密码不一致", Toast.LENGTH_LONG).show();
                } else {
                    String loginname = sp.getString("Loginame","");
                    //String password = sps.getString("PassWord","");
                    Map map = new HashMap<String, String>();
                    map.put("Name",loginname);
                    map.put("OldPass",oldpassword);
//                    map.put("Name", "gao123");
//                    map.put("OldPass", "gaorui123");
                    map.put("Pass",password1);
                    loginRequest(Request.Method.POST, UrlConstance.UPDATE_PASSWORD, map);
                }
            }
        });
    }

    private void loginRequest(int post, final String appUrl, final Map<String,String> map) {
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

                            if (resultCode.equals("1")) {
                                UserBaseInfo info = new Gson().fromJson(s, new TypeToken<UserBaseInfo>() {
                                }.getType());
                                String password1 = update_password1.getText().toString();
                                SharedPreferences sp = getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("PassWord",info.getMsg().getPassword());
//                                    切记每次操作记得commit
                                editor.commit();
                                Intent intent = new Intent(Re_Password_Activity.this, UpdateUserInfoActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
                                finish();

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

