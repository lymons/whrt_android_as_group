package com.mieasy.whrt_app_android_4.act.set;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.nfc.CardManager;

/**
 * Created by arvin on 16-7-9.
 */
public class NfcqueryActivity extends Activity {

    private static final String TAG = "NfcqueryActivity";
    private ImageView img_background;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private Resources res;

    private TextView tv_money;
    private TextView tv_touch;
    private TextView tv_touch_info;
    private LinearLayout ll_nfc_info;
    private TextView tv_card_number;
    private TextView tv_date;
    private LinearLayout ll_one;
    private LinearLayout ll_two;

    private TextView textView1;
    private TextView textView2;
    private int times = 0;
    private LinearLayout lin_top_left_home;
    private ImageButton mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_main_info_nfc);
        final Resources res = getResources();
        this.res = res;

        initView();
        //显示动画
        animator();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e(TAG, "123456");
        ll_one.removeAllViews();
        ll_two.removeAllViews();
        final Parcelable p = intent.getParcelableExtra(nfcAdapter.EXTRA_TAG);
        Log.e(TAG, "p:" + p);
        if (p == null) {
            //获取的对象为空
        } else {
            showData(CardManager.load(p, res));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(times!=0){
//            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
//                    getClass()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
//            onNewIntent(getIntent());
//        }
        if (nfcAdapter != null) {
            Log.e(TAG, "nfcAdapter不为空");
            nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                    CardManager.FILTERS, CardManager.TECHLISTS);
        }
        refreshStatus();
    }

    //判断机器是否有nfc功能
    private void refreshStatus() {
        final Resources r = this.res;
        final String tip;
        if (nfcAdapter == null) {
            tip = r.getString(R.string.tip_nfc_notfound);  //没有nfc硬件
            tv_touch.setText(tip);
            tv_touch_info.setVisibility(View.INVISIBLE);
        } else if (nfcAdapter.isEnabled()) {
            tip = r.getString(R.string.tip_nfc_enabled);//NFC已启用
//            tv_touch = (TextView) findViewById(R.id.tv_touch);
//            tv_touch_info = (TextView) findViewById(R.id.tv_touch_info);
//            tv_touch.setText(R.string.call_whrt_nfc);
//            tv_touch_info.setVisibility(View.VISIBLE);

        } else {
            tip = r.getString(R.string.tip_nfc_disabled);// nfc已禁用
            SpannableString spannableString = new SpannableString(tip);
            spannableString.setSpan(new Clickable(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    //跳转设置界面
                    Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    startActivity(intent);
                    times++;
                }

            }), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_touch.setText(spannableString);

            tv_touch.setMovementMethod(LinkMovementMethod.getInstance());
            tv_touch_info.setVisibility(View.INVISIBLE);
        }
    }


    //显示信息
    private void showData(String data) {
        Log.e(TAG, "data:" + data);
        if (data == null || data.length() == 0) {
            Log.e(TAG, "数据为空");
            //获取的数据为空  提示用户重新查询
            showHint();
            return;
        }
        //数据不为空
        showInfo(data);
    }

    //显示获取的数据
    private void showInfo(String data) {
        // 切割数据
        String[] strings = data.split("<br/>");
        for (String str : strings) {
            Log.e(TAG, "str:" + str);
        }
        // 余额
//        String cash = strings[5];
//        Log.e(TAG, "余额:" + cash);
//        // 有效日期
//        String date = strings[4];
//        Log.e(TAG, "有效日期:" + date);
//        // 卡号
//        String serl = strings[2];
//        Log.e(TAG, "卡号:" + serl);
//        // 消费记录
        if (strings.length > 6) {
            String strlog = strings[6];
            Log.e(TAG, "消费记录:" + strlog);

            if (strlog != null) {
                //切割字符串
                String[] stringsLog = strlog.split("<br />");
                for (String str : stringsLog) {
                    Log.e(TAG, "strlog:" + str);
                }
                String[] newStr = new String[stringsLog.length];

                String[] timeStr = new String[stringsLog.length];
                String[] moneryStr = new String[stringsLog.length];

                for (int i = 1; i < newStr.length; i++) {
                    String str = stringsLog[i];
                    Log.e(TAG, "str:" + str);
                    String[] strLog = str.split("\\[");
                    newStr[i - 1] = strLog[0];
                    Log.e(TAG, "转换后str:" + str);
                    String[] split = newStr[i - 1].split("\\   ");
                    Log.e(TAG, "time:" + split[0]);
                    timeStr[i - 1] = split[0];
                    Log.e(TAG, "monery:" + split[1]);
                    moneryStr[i - 1] = split[1];
                }

                //显示消费的时间
                for (int j = 0; j < timeStr.length - 1; j++) {
                    Log.e(TAG, "timeStr[j]" + timeStr[j]);
                    textView1 = new TextView(this);
                    textView1.setText(timeStr[j]);
                    textView1.setTextSize(18);
                    //设置字体样式
                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    textView1.setGravity(Gravity.CENTER);
                    ll_one.addView(textView1);
                }
                //显示每次消费的金额
                for (int j = 0; j < moneryStr.length - 1; j++) {
                    Log.e(TAG, "moneryStr[j]" + moneryStr[j]);
                    textView2 = new TextView(this);
                    textView2.setText(moneryStr[j]);
                    textView2.setTextSize(18);
                    //设置字体样式
                    textView2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    textView2.setGravity(Gravity.CENTER);
                    ll_two.addView(textView2);
                }
            }
        }

        img_background.setVisibility(View.GONE);
        tv_touch.setVisibility(View.GONE);
        tv_touch_info.setVisibility(View.GONE);
        ll_nfc_info.setVisibility(View.VISIBLE);
        String[] split = strings[5].split("<br />");
        tv_money.setText("余额:" + split[0]);
        tv_card_number.setText("卡号:" + strings[2]);
        tv_date.setText("有效期:" + strings[4]);
    }

    private void showHint() {
        ll_nfc_info.setVisibility(View.GONE);
        tv_touch.setVisibility(View.VISIBLE);
        img_background.setVisibility(View.VISIBLE);
        tv_touch.setText("暂不支持该卡查询...");
        tv_touch_info.setVisibility(View.INVISIBLE);

    }


    private void animator() {
        // 通过逐帧动画的资源文件获得AnimationDrawable示例
        AnimationDrawable drawable = (AnimationDrawable) getResources()
                .getDrawable(R.drawable.img_anim);
        // 把AnimationDrawable设置为ImageView的背景
        img_background.setBackgroundDrawable(drawable);
        drawable.start();
    }

    private void initView() {
        img_background = (ImageView) findViewById(R.id.img_background);
        tv_touch = (TextView) findViewById(R.id.tv_touch);
        tv_touch_info = (TextView) findViewById(R.id.tv_touch_info);
        //查询结果
        ll_nfc_info = (LinearLayout) findViewById(R.id.ll_nfc_info);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_card_number = (TextView) findViewById(R.id.tv_card_number);
        tv_date = (TextView) findViewById(R.id.tv_date);

        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_two);

        mImageButton = (ImageButton) findViewById(R.id.iv_top_left_back);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NfcqueryActivity.this.finish();
            }
        });
//        lin_top_left_home = (LinearLayout) findViewById(R.id.lin_top_left_home);
//        lin_top_left_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }

    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
