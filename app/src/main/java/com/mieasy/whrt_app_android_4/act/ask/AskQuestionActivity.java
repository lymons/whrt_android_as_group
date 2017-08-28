package com.mieasy.whrt_app_android_4.act.ask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.app.ContentApplication;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 16-11-29.
 */
public class AskQuestionActivity extends Activity {
    private static final String TAG = "AskQuestionActivity";
    private EditText mEditTitle;
    private EditText mEditContent;
    private TextView mCancle;
    private TextView mSubmit;
    private RequestQueue mQueue;
    private Boolean IsNetworkStatu;//是否有网络
    private Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.askquestios_activity);

        IsNetworkStatu = ContentApplication.getInstance().IsNetWorkAvailable;
        Log.e(TAG, "IsNetworkStatu is collectionFragment" + "-----" + IsNetworkStatu);
        mQueue = ContentApplication.getInstance().mQueue;

        mEditTitle = (EditText) findViewById(R.id.ask_question_edit);
        mEditContent = (EditText) findViewById(R.id.ask_ques_nr);
        mCancle = (TextView) findViewById(R.id.ask_question_qx);
        //取消不保存
        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskQuestionActivity.this.finish();
            }
        });
        mSubmit = (TextView) findViewById(R.id.ask_question_tj);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mEditTitle.getText().toString();
                String content = mEditContent.getText().toString();
                if (title.equals("")) {
                    Toast.makeText(getApplicationContext(), "标题不能为空！", Toast.LENGTH_SHORT).show();
                } else if (content.equals("")){
                    Toast.makeText(getApplicationContext(),"内容不能为空！",Toast.LENGTH_SHORT).show();
                } else {
                    map = new HashMap<String, String>();
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDatabase", Context.MODE_PRIVATE);
                    String titles = mEditTitle.getText().toString();
                    String contents = mEditContent.getText().toString();
                    String name = sharedPreferences.getString("name","");
                    String phone = sharedPreferences.getString("phone","");
                    String mail = sharedPreferences.getString("email","");

                    map.put("mode","AddConsulting");//标题
                    map.put("QuestTitl",titles);//标题
                    map.put("QuestionContent",contents);//内容
                    map.put("Client",name);//登录名
                    map.put("Phone",phone);//手机号
                    map.put("Mail",mail);//邮箱
                    map.put("Type","1");//类型
                    AskQuestionRequest(Request.Method.GET, NumUtil.URL_ONLINE_CONSULTING,map);
                }
            }
        });
    }
    private void AskQuestionRequest(int method, String url, final Map<String, String> map) {
        StringRequest stringRequest = new StringRequest(
                method,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG,"s:"+s);
                       if (s.equals("")) {
                           Toast.makeText(AskQuestionActivity.this,"已提交成功！",Toast.LENGTH_SHORT).show();
                           finish();
                       }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                  Log.e(TAG,"volleyError:"+volleyError);
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
