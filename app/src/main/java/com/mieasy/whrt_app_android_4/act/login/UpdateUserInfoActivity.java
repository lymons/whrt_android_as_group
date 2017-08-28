package com.mieasy.whrt_app_android_4.act.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.mieasy.whrt_app_android_4.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mieasy.whrt_app_android_4.act.login.LoginFragment.DATABASE;

/**
 * Created by alan on 16-12-26.
 */
public class UpdateUserInfoActivity extends NoBarActivity implements View.OnClickListener {
    private static final String TAG = "UpdateUserInfoActivity";
    private RequestQueue mQueue;
    private TextView tv_top_left_title;
    private ImageButton iv_top_left_back;
    private EditText update_name, update_phone, update_email, update_address;
    private String resultCode;
    private Button btn_update;
    private SharedPreferences sp;
    LinearLayout change_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_info);
        mQueue = Volley.newRequestQueue(this);
        mQueue.start();
        tv_top_left_title = (TextView) findViewById(R.id.tv_top_left_title);
        tv_top_left_title.setText("修改");
        iv_top_left_back = (ImageButton) findViewById(R.id.iv_top_left_back);
        iv_top_left_back.setOnClickListener(this);
        update_name = (EditText) findViewById(R.id.update_name);
        SharedPreferences sp = getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
        String name = sp.getString("name", "");
        update_name.setText(name);
        update_name.setSelection(name.length());
        update_name.setOnClickListener(this);

        update_phone = (EditText) findViewById(R.id.update_phone);
        String phone = sp.getString("phone", "");
        update_phone.setText(phone);
        update_phone.setSelection(phone.length());
        update_phone.setOnClickListener(this);

//        update_password = (EditText) findViewById(R.id.update_password);
//        String password = sp.getString("PassWord", "");
//        update_password.setText(password);
//        update_password.setSelection(password.length());
//        update_password.setOnClickListener(this);

        update_email = (EditText) findViewById(R.id.update_email);
        String email = sp.getString("email", "");
        update_email.setText(email);
        update_email.setSelection(email.length());
        update_email.setOnClickListener(this);

        update_address = (EditText) findViewById(R.id.update_address);
        String address = sp.getString("add", "");
        update_address.setText(address);
        update_address.setSelection(address.length());
        update_address.setOnClickListener(this);

        btn_update = (Button) findViewById(R.id.update_user_info_btn);
        btn_update.setOnClickListener(this);

        change_password = (LinearLayout) findViewById(R.id.change_password);
        change_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_top_left_back:
                finish();
                break;
            case R.id.update_user_info_btn:
                UpdateInfo();
                break;
            case R.id.change_password:
                Intent intentpassword = new Intent(UpdateUserInfoActivity.this,Re_Password_Activity.class);
                startActivity(intentpassword);
                break;
            default:
                break;
        }
    }

    private void UpdateInfo() {
        String ass_name = update_name.getText().toString();
        sp = getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
        String loginname = sp.getString("Loginame", "");
        String password = sp.getString("PassWord", "");
        if (!TextUtils.isEmpty(ass_name)) {
            Map map = new HashMap<String, String>();
            map.put("Loginame", loginname);
            map.put("PassWord", password);
            map.put("Name", update_name.getText().toString());
            map.put("Mobile", update_phone.getText().toString());
            map.put("Email", update_email.getText().toString());
            map.put("Address", update_address.getText().toString());
            UpdateInfoRequest(Request.Method.POST, UrlConstance.REVISE_URL, map);

        }
    }

    private void UpdateInfoRequest(int post, String reviseUrl, final Map<String, String> map) {
        StringRequest stringRequest = new StringRequest(
                post,
                reviseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (map != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                resultCode = jsonObject.getString("code");
                                Log.e(TAG, "code" + "-----------" + resultCode);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (resultCode.equals("1")) {
                                UserBaseInfo info = new Gson().fromJson(s, new TypeToken<UserBaseInfo>() {
                                }.getType());
                                String name = update_name.getText().toString();
                                String phone = update_phone.getText().toString();
                                String email = update_email.getText().toString();
                                String add = update_address.getText().toString();
                                sp = getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("name",name);
                                editor.putString("phone",phone);
                                editor.putString("email",email);
                                editor.putString("add",add);
                                editor.commit();
                               // editor.apply();
                                Intent intent = new Intent(UpdateUserInfoActivity.this, ReviseActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        mQueue.add(stringRequest);

    }

    private void loginRequest(int post, final String appUrl, final HashMap map) {
        StringRequest stringRequest = new StringRequest(
                post,
                appUrl,
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
//                              如果code==1 则 用Gson去解析数据
                            if (resultCode.equals("1")) {
//								解析请求回来的数据
                                UserBaseInfo info = new Gson().fromJson(s, new TypeToken<UserBaseInfo>() {
                                }.getType());
//								 用变量保存
                                String name = info.getMsg().getNickname();
                                String phone = info.getMsg().getPhone();
                                String email = info.getMsg().getEmail();
                                String address = info.getMsg().getAddress();
                                String Loginame = info.getMsg().getLoginname();
                                String PassWord = info.getMsg().getPassword();
//								第一个参数是preferece的名称(比如：MyPref),第二个参数是打开的方式（一般选择private方式）
                                SharedPreferences sp = getApplicationContext().getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
// 								获取Editor对象
//								保存用户的相关信息
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("Loginame", Loginame);
                                editor.putString("PassWord", PassWord);
                                editor.putString("name", name);
                                editor.putString("phone", phone);
                                editor.putString("email", email);
                                editor.putString("add", address);
                                editor.putBoolean("islogin", true);
//								提交
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//								放入一个布尔值进行判断是否登陆
                                startActivity(intent);
                                //getActivity().finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
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