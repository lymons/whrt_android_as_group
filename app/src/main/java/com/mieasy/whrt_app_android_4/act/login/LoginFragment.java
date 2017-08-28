package com.mieasy.whrt_app_android_4.act.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mieasy.whrt_app_android_4.bean.UrlConstance;
import com.mieasy.whrt_app_android_4.bean.UserBaseInfo;
import com.mieasy.whrt_app_android_4.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment implements OnClickListener {
    private View view;
    private ImageView mTopImg;
    private Button mNoUser;

    private EditText etUser, etPass;
    private Button mBtnSubmit;
    private RequestQueue mQueue;
    private String resultCode;
    private TextView mPassword;
    public static final String DATABASE = "UserDatabase";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login_frg, null);
        initView();
        mQueue = Volley.newRequestQueue(getContext());
        mQueue.start();
        return view;
    }

    private void initView() {
        mTopImg = (ImageView) view.findViewById(R.id.iv_top_img);
//		ImageLoader.getInstance().displayImage("assets://userlogin.jpg", mTopImg);

        etUser = (EditText) view.findViewById(R.id.tv_user_name);
        etPass = (EditText) view.findViewById(R.id.tv_user_pass);
        mNoUser = (Button) view.findViewById(R.id.tv_no_user);
        mNoUser.setOnClickListener(this);

        mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);

        mPassword = (TextView) view.findViewById(R.id.update_password);
        mPassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String account = etUser.getText().toString();//账号
                String password = etPass.getText().toString();//密码
//                 判断输入是否为空
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)
                        ) {
//				将输入框中的账号密码以键值对的形式放入map中
                    HashMap map = new HashMap<String, String>();
                    map.put("UserName", account);
                    map.put("UserPass", password);
//				调用loginRequest方法发送请求
                    loginRequest(Request.Method.POST, UrlConstance.LOGIN_URL2, map);
                } else {
                    Toast.makeText(getContext(), "账号或者密码有误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_no_user:
                Intent intent = new Intent(getActivity(), ValidateActivity.class);
                startActivity(intent);
//			跳转到登陆界面 销毁这个activity
                break;
            case R.id.update_password:
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginFragment.this.getActivity());
                builder.setTitle("客户端暂时不支持找回密码");
                builder.setMessage("是否前往武汉地铁官网找回密码");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        startActivity(intent);
                    }
                });

                builder.create().show();

                break;
            default:
                break;
        }
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
                                SharedPreferences sp = getActivity().getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
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
                                Intent intent = new Intent(getActivity(), MainActivity.class);
//								放入一个布尔值进行判断是否登陆
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "参数为空", Toast.LENGTH_LONG).show();
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