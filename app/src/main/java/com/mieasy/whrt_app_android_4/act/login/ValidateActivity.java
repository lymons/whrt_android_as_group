package com.mieasy.whrt_app_android_4.act.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.util.HashMap;
import java.util.Map;

public class ValidateActivity extends NoBarActivity {
    private Button sc, yanzhen;
    private EditText phone, shuru;
    private TimeCount time;
    private TextView tv_top_left_title;
    //    存储时的名称
    public static final String DATABASE = "Database";
    private RequestQueue mQueue;
    private ImageButton iv_top_left_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);
        mQueue = Volley.newRequestQueue(this);
        mQueue.start();
        tv_top_left_title = (TextView) findViewById(R.id.tv_top_left_title);
        tv_top_left_title.setText("注册");
        sc = (Button) findViewById(R.id.sc);
        yanzhen = (Button) findViewById(R.id.yanzhen);
        phone = (EditText) findViewById(R.id.phone);
        shuru = (EditText) findViewById(R.id.shuru);
        iv_top_left_back = (ImageButton) findViewById(R.id.iv_top_left_back);
        iv_top_left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //        时间
        time = new TimeCount(60000, 1000);
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone1 = phone.getText().toString();
                if (phone1.length() == 0) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (phone1.length() != 11) {
                    Toast.makeText(getApplicationContext(), "请输入正确的号码！", Toast.LENGTH_SHORT).show();
                } else {
                    time.start();// 开始计时
                    String code = createRandom(true, 4);
                    //  存储时的名称 第一个参数是存储时的名称，第二个参数则是文件的打开方式.一般选择private方式~
                    SharedPreferences sp = getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
                    // 获取Editor对象
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("code", code);
                    editor.commit();
                    HashMap map = new HashMap<String, String>();
                    map.put("TelNum", phone1);
                    map.put("Code", code);
                    loginRequest(Request.Method.POST, UrlConstance.APP_URL_SMS, map);
                }
            }
        });

        yanzhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shuru1 = shuru.getText().toString();
                SharedPreferences sp = getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
                String cd = sp.getString("code", "");

                if (!TextUtils.isEmpty(shuru1)) {
                    if (shuru1.equals(cd)) {
                        Intent intent = new Intent(ValidateActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "验证成功,请注册！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "验证失败,请重新验证!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    private void loginRequest(int get, final String appUrl, final HashMap map) {
        StringRequest stringRequest = new StringRequest(
                get,
                appUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
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

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            sc.setText("获取验证码");
            sc.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            sc.setClickable(false);//防止重复点击
            sc.setText(millisUntilFinished / 1000 + "s后重发");
        }
    }
}
