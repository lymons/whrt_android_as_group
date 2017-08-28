package com.mieasy.whrt_app_android_4.pinyinview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mieasy.whrt_app_android_4.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alan on 17-1-5.
 */
public class SideBarLine extends View {

    public static String[] INDEX_STRING = {"1号线","2号线","3号线","４号线","６号线"};
    //public static String[] INDEX_STRING = {"1","2","3","４","６"};
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private List<String> letterList;
    private int choose = -1;
    private Paint paint = new Paint();
    private TextView mTextDialog;

    public SideBarLine(Context context) {
        super(context,null);
    }
    public SideBarLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SideBarLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        setBackgroundColor(Color.parseColor("#FFFFFF"));
        letterList = Arrays.asList(INDEX_STRING);
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();// 获取对应高度
        int width = getWidth();// 获取对应宽度
        int singleHeight = height / letterList.size();// 获取每一个字母的高度
        for (int i = 0; i < letterList.size(); i++) {
            paint.setColor(Color.parseColor("#606060"));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(20);
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#4F41FD"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(letterList.get(i)) / 2;
            float yPos = singleHeight * i + singleHeight / 2;
            canvas.drawText(letterList.get(i), xPos, yPos, paint);
            paint.reset();// 重置画笔
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * letterList.size());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.parseColor("#FFFFFF"));
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.GONE);
                }
                break;
            default:
                setBackgroundResource(R.drawable.bg_sidebar);
                if (oldChoose != c) {
                    if (c >= 0 && c < letterList.size()) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(letterList.get(c));
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(letterList.get(c));
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setIndexText(ArrayList<String> indexStrings) {
        this.letterList = indexStrings;
        invalidate();
    }

    /**
     * 为SideBarLine设置显示当前按下的线路的TextView
     *
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListeners(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = (OnTouchingLetterChangedListener) onTouchingLetterChangedListener;
    }

    /**
     * 接口
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}
