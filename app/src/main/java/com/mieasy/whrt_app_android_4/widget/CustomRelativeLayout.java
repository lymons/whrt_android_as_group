package com.mieasy.whrt_app_android_4.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by root on 16-11-15.
 */

public class CustomRelativeLayout extends RelativeLayout {
//    private DragLayout dl;
    private DragLayout dl;

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


//
//    public CustomRelativeLayout(Context context) {
//        super(context);
//    }
//
//    public CustomRelativeLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }

//    public void setDragLayout(DragLayout dl) {
//        this.dl = dl;
//    }
//    public  void setDragLayout(DragLayout dl){
//
//    }


    public void setDl(DragLayout dl) {
        this.dl = dl;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (dl.getStatus() != DragLayout.Status.CLOSE) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dl.getStatus() != DragLayout.Status.CLOSE) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                dl.close();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

}
