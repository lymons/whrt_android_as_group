package com.mieasy.whrt_app_android_4.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.act.login.LoginActivity;
import com.mieasy.whrt_app_android_4.act.login.ReviseActivity;
import com.mieasy.whrt_app_android_4.act.pro.NoBarActivity;
import com.mieasy.whrt_app_android_4.act.set.NfcqueryActivity;
import com.mieasy.whrt_app_android_4.act.set.SettingInfoActivity;
import com.mieasy.whrt_app_android_4.util.MachineInfoManager;
import com.mieasy.whrt_app_android_4.util.NetWorkManager;
import com.mieasy.whrt_app_android_4.util.NumUtil;
import com.mieasy.whrt_app_android_4.util.UpdateManager;
import com.mieasy.whrt_app_android_4.welcome.WelcomeActivity;
import com.mieasy.whrt_app_android_4.widget.DragLayout;
import com.mieasy.whrt_app_android_4.widget.RingsImgageView;
import com.nineoldandroids.view.ViewHelper;

import java.util.Calendar;

import static com.mieasy.whrt_app_android_4.act.login.LoginFragment.DATABASE;

public class MainActivity extends NoBarActivity implements OnClickListener {
    private ImageView mImgMy, mImgLogo;
    private Fragment slideFragment, blockFragment;
    private TextView txtBottomView;
    private static final int REQUEST_PERM = 151;
    private long[] mHints = new long[3];//初始全部为0
    private DragLayout dl;
    private LinearLayout linearLayout;
    private Intent intent;
    private ImageView ivIcon;
    private TextView mQueryNcf, mTextSize, mTextWelcome, mTextShare, mTextUpdate, mTextCall, mTextAbout, nickename;
    private RingsImgageView login_btn;
    private boolean islogin ,islog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//		缩进隐藏状态栏
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.ACCESSIBILITY_LIVE_REGION_NONE);
        setContentView(R.layout.activity_main2);
        initiew();
        new NetWorkManager(this).checkIndex();
        new UpdateManager(this, false).checkUpdate();    //配置更新
        new MachineInfoManager(this).initInfo();//统计
        initDragLayout();
        initView();
        nickename = (TextView) findViewById(R.id.nickname);


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
        islogin = sp.getBoolean("islogin",false);
        String name =sp.getString("name","");
        nickename.setText(name);
    }

    private void initDragLayout() {
        dl = (DragLayout) findViewById(R.id.dl);
        linearLayout = (LinearLayout) findViewById(R.id.aaa);
        dl.setDragListener(new DragLayout.DragListener() {
            //界面打开的时候
            @Override
            public void onOpen() {
            }

            //界面关闭的时候
            @Override
            public void onClose() {
            }

            //界面滑动的时候
            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(ivIcon, 1 - percent);
            }
        });
    }


    private void initView() {
//		initTitle();
        mQueryNcf = (TextView) findViewById(R.id.query_nfc);
        mTextSize = (TextView) findViewById(R.id.text_size);
        mTextWelcome = (TextView) findViewById(R.id.page_welcome);
        mTextShare = (TextView) findViewById(R.id.share_app);
        mTextUpdate = (TextView) findViewById(R.id.check_update);
        mTextCall = (TextView) findViewById(R.id.callme);
        mTextAbout = (TextView) findViewById(R.id.about);
        login_btn = (RingsImgageView) findViewById(R.id.login_btn);
        mQueryNcf = (TextView) findViewById(R.id.query_nfc);
        mTextSize.setOnClickListener(this);
        mTextWelcome.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                super.onNoDoubleClick(v);
                startArt(WelcomeActivity.class, 2, 0);
            }
        });
        mTextShare.setOnClickListener(this);
        mTextUpdate.setOnClickListener(this);
        mTextCall.setOnClickListener(this);
        mTextAbout.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        mQueryNcf.setOnClickListener(this);
    }

    //代码2
    public abstract class NoDoubleClickListener implements OnClickListener {
        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(v);
            }
        }
        public void onNoDoubleClick(View v) {

        }
    }


    public void startArt(Class clazz, int item, int rid) {
        intent = new Intent(this, clazz);
        intent.putExtra(NumUtil.JUMP_INTENT, item);
        intent.putExtra(NumUtil.JUMP_TYPE, rid);
        startActivity(intent);
    }


    /**
     * 初始化轮播  板块布局
     */
    private void initiew() {
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        txtBottomView = (TextView) findViewById(R.id.activity_bottom_text);
        txtBottomView.setOnClickListener(this);

        mImgMy = (ImageView) findViewById(R.id.iv_top_right_myinfo);
        mImgMy.setOnClickListener(this);
        mImgLogo = (ImageView) findViewById(R.id.iv_top_main_logo);
        mImgLogo.setOnClickListener(this);

        Intent intent = getIntent();
        slideFragment = new SlideFragment();
        Bundle bundle = new Bundle();
        slideFragment.setArguments(bundle);

        blockFragment = new BlockImgFragment();
        blockFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frg_main_silde, slideFragment).add(R.id.frg_main_block, blockFragment).commit();

        ivIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dl.open();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_bottom_text:                //底栏的三次点击
                //将mHints数组内的所有元素左移一个位置
                System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
                //获得当前系统已经启动的时间
                mHints[mHints.length - 1] = SystemClock.uptimeMillis();
                if (SystemClock.uptimeMillis() - mHints[0] <= 500) {
                    //            	Toast.makeText(this, "你点击了三次", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, WelcomeActivity.class);
                    intent.putExtra(NumUtil.JUMP_INTENT, 6);
                    startActivity(intent);
                }
                break;
            case R.id.text_size:               //字体大小
                startArt(SettingInfoActivity.class, 0, R.string.set_textsize);

                break;
            case R.id.share_app:               //推荐
//                startArt(SettingInfoActivity.class, 1, R.string.set_share);
                Intent intshare = new Intent(this,ShareAppActivity.class);
                startActivity(intshare);
                break;
            case R.id.callme:               //联系我们
                startArt(SettingInfoActivity.class, 2, R.string.set_call);
                break;
            case R.id.about:                    //关于
                startArt(SettingInfoActivity.class, 3, R.string.set_about);
                break;
            case R.id.query_nfc:               //武汉通查询
//			startArt(SettingInfoActivity.class,4,R.string.set_querynfc);
                startActivity(new Intent(this, NfcqueryActivity.class));
                break;
            case R.id.page_welcome:            //欢迎页
//				startArt(WelcomeActivity.class,2,0);
                break;
            case R.id.check_update:            //app更新
                new UpdateManager(this, true).checkUpdate();
                break;
            case R.id.login_btn:
//                如果没有登陆 这跳转到登陆界面
                if (!islogin){
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else {
//                    如果登陆
//                        跳转到修改页面
//                    Intent ia = getIntent();
//                    islog =ia.getBooleanExtra("islog",false);
                        if (nickename.equals("")){
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(MainActivity.this, ReviseActivity.class);
                            startActivity(intent);
                        }
                }
                    break;
                    default:
                        break;
                }
        }


        private boolean mIsExit;
        @Override
        /**
         * 双击返回键退出
         */
        public boolean onKeyDown ( int keyCode, KeyEvent event){

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mIsExit) {
                    this.finish();

                } else {
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    mIsExit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIsExit = false;
                        }
                    }, 2000);
                }
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }


    }