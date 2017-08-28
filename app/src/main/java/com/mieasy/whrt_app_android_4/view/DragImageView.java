package com.mieasy.whrt_app_android_4.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.mieasy.whrt_app_android_4.bean.Colors;
import com.mieasy.whrt_app_android_4.bean.Point;
import com.mieasy.whrt_app_android_4.entity.change.DetailsChange;
import com.mieasy.whrt_app_android_4.entity.change.PathInfoChange;
import com.mieasy.whrt_app_android_4.imp.PointCallBackInterface;
import com.mieasy.whrt_app_android_4.util.NumUtil;

import java.util.ArrayList;
import java.util.List;

/****
 * 这里你要明白几个方法执行的流程： 首先ImageView是继承自View的子类.
 * onLayout方法：是一个回调方法.该方法会在在View中的layout方法中执行，在执行layout方法前面会首先执行setFrame方法.
 * layout方法：
 * setFrame方法：判断我们的View是否发生变化，如果发生变化，那么将最新的l，t，r，b传递给View，然后刷新进行动态更新UI.
 * 并且返回ture.没有变化返回false.
 * <p/>
 * invalidate方法：用于刷新当前控件,
 *
 * @author zhangjia
 */
public class DragImageView extends ImageView {
    private static final String TAG = DragImageView.class.getSimpleName();
    private double kx, ky, kintX, kintY;
    private Bitmap bitmapMap, bitmapStart, bitmapStop, bitmapExchange, bitmapStation;
    private double kX, kY;                //X，Y的比例值
    private double startX, startY, stopX, stopY, stationX, stationY;    //起始点坐标，结束点坐标
    private int screen_W, screen_H;// 可见屏幕的宽高度
    private float bitmap_W, bitmap_H;// 当前图片宽高
    private int MAX_W, MAX_H, MIN_W, MIN_H;// 极限值
    private int current_Top, current_Right, current_Bottom, current_Left;// 当前图片上下左右坐标
    private int start_Top = -1, start_Right = -1, start_Bottom = -1, start_Left = -1;// 初始化默认位置.
    static final int DOUBLE_CLICK_TIME_SPACE = 300;    //双击间隔时间
    long lastClickTime = 0;                        // 单击时间
    private int start_x, start_y, current_x, current_y;// 触摸位置
    private float beforeLenght, afterLenght;// 两触点距离


    private enum MODE {NONE, DRAG, ZOOM}

    ;//模式 NONE：无 DRAG：拖拽. ZOOM:缩放
    private MODE mode = MODE.NONE;// 默认模式
    private boolean isControl_V = false;// 垂直监控
    private boolean isControl_H = false;// 水平监控
    private int width_temp, height_temp;// 水平监控
    private Context context;
    private PointCallBackInterface mCallBack;
    private Paint paint;
    private int paintTimer = 0;
    private List<PathInfoChange> detailsChangeList = new ArrayList<PathInfoChange>();

    public void setPathInfo(List<PathInfoChange> detailsChangeList) {
        if (this.detailsChangeList.size() != 0) {
            this.detailsChangeList.clear();
        }
        this.detailsChangeList = detailsChangeList;
    }

    /**
     * 构造方法
     **/
    public DragImageView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 可见屏幕宽度
     **/
    public void setScreen_W(int screen_W) {
        this.screen_W = screen_W;
    }

    /**
     * 可见屏幕高度
     **/
    public void setScreen_H(int screen_H) {
        this.screen_H = screen_H;
    }

    public void setInitScale(float f) {
        this.setFrame(0, (screen_H - MIN_H) / 2, (int) bitmap_W, ((screen_H - MIN_H) / 2) + MIN_H);
        invalidate();
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /***
     * 设置显示图片
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.bitmapMap = bm;

        /** 获取图片宽高 **/
        bitmap_W = 1080;
        bitmap_H = 1080;

        MAX_W = (int) (bitmap_W * NumUtil.MAP_MAX_SCALE);
        MAX_H = (int) (bitmap_H * NumUtil.MAP_MAX_SCALE);

        MIN_W = 1080;
        MIN_H = 1080;
//        int left = 0, top = 0, right = 0, bottom = 0;
//        setPosition(left,top,right,bottom);
    }

    public void setStartImageBitmap(Bitmap bm) {
        this.bitmapStart = bm;
    }

    public void setStationImageBitmap(Bitmap bm) {
        this.bitmapStation = bm;
    }

    public void setStopImageBitmap(Bitmap bm) {
        this.bitmapStop = bm;
    }

    public void setExchangeImageBitmap(Bitmap bm) {
        this.bitmapExchange = bm;
    }

    public DragImageView SetOnClickCallBack(PointCallBackInterface callBack) {
        this.mCallBack = callBack;
        return this;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (start_Top == -1) {
            start_Top = top;
            start_Left = left;
            start_Bottom = bottom;
            start_Right = right;
        }
        paintTimer += 1;
        if (paintTimer <= 2) {
            isControl_V = true;
            isControl_H = true;
            mode = MODE.DRAG;
            this.setFrame((int) (0 - bitmap_W), (int) (0 - (bitmap_W * 3 - screen_H) / 2 - 250), (int) bitmap_W * 3, (int) bitmap_H * 3);
            this.setFrame(0, 0, screen_H, screen_H);
        }
    }

    /***
     * touch 事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            /** 处理单点、多点触摸 **/
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event);
                break;
            // 多点触摸
            case MotionEvent.ACTION_POINTER_DOWN:
                //			Log.e("DragImageView","ACTION_POINTER_DOWN");
                onPointerDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //			Log.e("DragImageView","ACTION_MOVE");
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                //			Log.e("DragImageView","ACTION_UP");
                if (event.getPointerCount() == 1) {
                    //System.out.println("Raw:X坐标："+event.getRawX()+" ;Raw:Y坐标："+event.getRawY());
                    //System.out.println("start:x:"+start_x+";y:"+start_y+" ;current:x:"+current_x+";y:"+current_y);
                    //System.out.println("start:上："+start_Top+"右："+start_Right+"下："+start_Bottom+"左："+start_Left);
                    //System.out.println("current:上："+current_Top+"右："+current_Right+"下："+current_Bottom+"左："+current_Left);
                    //System.out.println("原始图片的宽度:"+bitmap_W+";图片的高度："+bitmap_H);
                    //System.out.println("点击的X坐标："+event.getX()+" ;Y坐标："+event.getY());
                    //System.out.println("当前图片的宽度:"+(float)getWidth()+";图片的高度："+(float)getHeight());
                    //原图片与当前图片宽高的比例
                    kX = (double) (NumUtil.MAP_WIDTH) / (getWidth());
                    kY = (double) (NumUtil.MAP_HEIGHT) / (getHeight());
                    if (this.mCallBack != null) {
                        //比例的大小与（图片上某点的X和Y)
                        this.mCallBack.CallBack(kX * (event.getX()), kY * (event.getY()));
                    }
                    return true;
                }
                mode = MODE.NONE;
                break;

            // 多点松开
            case MotionEvent.ACTION_POINTER_UP:
                //			Log.e("DragImageView","ACTION_POINTER_UP");
                mode = MODE.NONE;
                break;
        }
        return true;
    }

    /**
     * 按下
     **/
    void onTouchDown(MotionEvent event) {
        mode = MODE.DRAG;

        current_x = (int) event.getRawX();        //点击的点相对屏幕的X坐标
        current_y = (int) event.getRawY();        //点击的点相对屏幕的Y坐标
        start_x = (int) event.getX();            //点击的点相对X坐标
        start_y = current_y - this.getTop();    //触摸点距离屏幕的Y高 -
        //		Log.e("DragImageView","ACTION_DOWN》》[getRawX="+current_x+",getRawY="+current_y);
        //		Log.e("DragImageView","ACTION_DOWN》》[getX="+getX()+",getY="+getY());
        //		Log.e("DragImageView","ACTION_DOWN》》[getLeft="+getLeft()+",getTop="+getTop()+",getRight="+getRight()+",getBottom="+getBottom());
        //		Log.e("DragImageView","ACTION_DOWN》》[current_x="+current_x+",current_y="+current_y+",start_x="+start_x+",start_y="+start_y);
        //间隔一定时间，判断为双击
        Log.e("图片设置", "bitmap_W:" + bitmap_W + ";bitmap_H" + bitmap_H);
        if (event.getPointerCount() == 1) {
            if (event.getEventTime() - lastClickTime < DOUBLE_CLICK_TIME_SPACE) {
                if (getWidth() > 3000) {
                    isControl_H = true;
                    isControl_V = true;
                    mode = MODE.DRAG;
                    this.setFrame(0, 0, screen_H, screen_H);
                } else {
                    float imgW = event.getX() / getWidth();
                    float imgH = event.getY() / getHeight();
                    isControl_V = true;
                    isControl_H = true;
                    mode = MODE.DRAG;
                    this.setFrame((int) (0 - bitmap_W), (int) (0 - (bitmap_W * 3 - screen_H) / 2 - 250), (int) bitmap_W * 3, (int) bitmap_H * 3);
//					this.setFrame((int)(imgW*3000-screen_W/2),(int)(imgH*3000-screen_H/2), (int)3000,(int)3000);
                }
            }
            lastClickTime = event.getEventTime();
        }
    }

    /**
     * 两个手指 只能放大缩小
     **/
    void onPointerDown(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            mode = MODE.ZOOM;
            beforeLenght = getDistance(event);// 获取两点的距离
        }
    }

    float scaleInt;

    /**
     * 移动的处理
     **/
    void onTouchMove(MotionEvent event) {
        int left = 0, top = 0, right = 0, bottom = 0;
        /** 处理拖动 **/
        if (mode == MODE.DRAG) {

            /** 在这里要进行判断处理，防止在drag时候越界 **/
            /** 获取相应的l，t,r ,b **/
            left = current_x - start_x;
            right = current_x + this.getWidth() - start_x;
            top = current_y - start_y;
            bottom = current_y - start_y + this.getHeight();

            /** 水平进行判断 **/
            if (isControl_H) {
                if (left >= 0) {
                    left = 0;
                    right = this.getWidth();
                }
                if (right <= screen_W) {
                    left = screen_W - this.getWidth();
                    right = screen_W;
                }
            } else {
                left = this.getLeft();
                right = this.getRight();
            }
            /** 垂直判断 **/
            if (isControl_V) {
                if (top >= 0) {
                    top = 0;
                    bottom = this.getHeight();
                }
                if (bottom <= screen_H) {
                    top = screen_H - this.getHeight();
                    bottom = screen_H;
                }
            } else {
                top = this.getTop();
                bottom = this.getBottom();
            }
            if (isControl_H || isControl_V)
                this.setPosition(left, top, right, bottom);

            current_x = (int) event.getRawX();
            current_y = (int) event.getRawY();
        }
        /** 处理缩放 **/
        else if (mode == MODE.ZOOM) {
            afterLenght = getDistance(event);// 获取两点的距离
            float gapLenght = afterLenght - beforeLenght;// 变化的长度

            if (Math.abs(gapLenght) > 5f) {
                scaleInt = afterLenght / beforeLenght;// 求的缩放的比例
                if (scaleInt > 1f) {
                    this.setScale(scaleInt);
                    beforeLenght = afterLenght;
                } else {
                    this.setScale(scaleInt);
                    beforeLenght = afterLenght;
                }
            }
        }
    }

    public void setStationVisible(Point point) {

        int left = 0, top = 0, right = 0, bottom = 0;
        int x = point.getPointX();
        int y = point.getPointY();

        Log.e(TAG, "current_Right:" + current_Right + ",current_Left:" + current_Left);
        Log.e(TAG, "current_Bottom:" + current_Bottom + ",current_Top:" + current_Top);
        int width = this.bitmapMap.getWidth();
        int height = this.bitmapMap.getHeight();

        int x1 = (int) ((float) x * ((float) (width) / (float) 3000));
        int y1 = (int) ((float) y * ((float) (height) / (float) 3000));

        if ((x1 < screen_W) && (y1 < screen_H)) return;

        if (x1 > screen_W) {
            left = screen_W - x1 - (300);
            right = left + width;
        } else {
            left = 0;
            right = width;
        }

        if (y1 > screen_H) {
            top = screen_H - y1 - (300);
            bottom = top + height;
        } else {
            top = 0;
            bottom = height;
        }
        Log.e(TAG, "x:" + x + ",y:" + y + ",x1:" + x1 + ",y1:" + y1 + ",scaleInt:" + Float.toString(scaleInt) + ",screen_W:" + screen_W + ",screen_H:" + screen_H + ",width:" + width + ",height:" + height);
        this.setPosition(left, top, right, bottom);

        Log.e(TAG, "left:" + left + ",top:" + top + ",right:" + right + ",bottom:" + bottom);


//        /** 在这里要进行判断处理，防止在drag时候越界 **/
//        /** 获取相应的l，t,r ,b **/
//        left = current_x - start_x;
//        right = current_x + this.getWidth() - start_x;
//        top = current_y - start_y;
//        bottom = current_y - start_y + this.getHeight();
//
//        /** 水平进行判断 **/
//        if (isControl_H) {
//            if (left >= 0) {
//                left = 0;
//                right = this.getWidth();
//            }
//            if (right <= screen_W) {
//                left = screen_W - this.getWidth();
//                right = screen_W;
//            }
//        } else {
//            left = this.getLeft();
//            right = this.getRight();
//        }
//        /** 垂直判断 **/
//        if (isControl_V) {
//            if (top >= 0) {
//                top = 0;
//                bottom = this.getHeight();
//            }
//            if (bottom <= screen_H) {
//                top = screen_H - this.getHeight();
//                bottom = screen_H;
//            }
//        } else {
//            top = this.getTop();
//            bottom = this.getBottom();
//        }
////        left:0,top:330,right:1080,bottom:1410
//        if (isControl_H || isControl_V)

    }


    /**
     * 获取两点的距离
     **/
    float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * 实现处理拖动
     **/
    private void setPosition(int left, int top, int right, int bottom) {
        this.layout(left, top, right, bottom);
    }

    /**
     * 处理缩放
     **/
    void setScale(float scale) {
        int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 4;// 获取缩放水平距离
        int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 4;// 获取缩放垂直距离

        // 放大
        if (scale > 1 && this.getWidth() <= MAX_W) {
            current_Left = this.getLeft() - disX;
            current_Top = this.getTop() - disY;
            current_Right = this.getRight() + disX;
            current_Bottom = this.getBottom() + disY;

            this.setFrame(current_Left, current_Top, current_Right, current_Bottom);
            /***
             * 此时因为考虑到对称，所以只做一遍判断就可以了。
             */
            if (current_Top <= 0 && current_Bottom >= screen_H) {
                //		Log.e("jj", "屏幕高度=" + this.getHeight());
                isControl_V = true;                                    // 开启垂直监控
            } else {
                isControl_V = false;
            }
            if (current_Left <= 0 && current_Right >= screen_W) {
                isControl_H = true;// 开启水平监控
            } else {
                isControl_H = false;
            }
        }
        // 缩小
        else if (scale < 1 && this.getWidth() >= MIN_W) {
            current_Left = this.getLeft() + disX;
            current_Top = this.getTop() + disY;
            current_Right = this.getRight() - disX;
            current_Bottom = this.getBottom() - disY;
            /***
             * 在这里要进行缩放处理
             */
            // 上边越界
            if (isControl_V && current_Top > 0) {
                current_Top = 0;
                current_Bottom = this.getBottom() - 2 * disY;
                if (current_Bottom < screen_H) {
                    current_Bottom = screen_H;
                    isControl_V = false;// 关闭垂直监听
                }
            }
            // 下边越界
            if (isControl_V && current_Bottom < screen_H) {
                current_Bottom = screen_H;
                current_Top = this.getTop() + 2 * disY;
                if (current_Top > 0) {
                    current_Top = 0;
                    isControl_V = false;// 关闭垂直监听
                }
            }

            // 左边越界
            if (isControl_H && current_Left >= 0) {
                current_Left = 0;
                current_Right = this.getRight() - 2 * disX;
                if (current_Right <= screen_W) {
                    current_Right = screen_W;
                    isControl_H = false;// 关闭
                }
            }
            // 右边越界
            if (isControl_H && current_Right <= screen_W) {
                current_Right = screen_W;
                current_Left = this.getLeft() + 2 * disX;
                if (current_Left >= 0) {
                    current_Left = 0;
                    isControl_H = false;// 关闭
                }
            }

            if (isControl_H || isControl_V) {
                this.setFrame(current_Left, current_Top, current_Right, current_Bottom);
            } else {
                //this.setFrame(current_Left, current_Top, current_Right,current_Bottom);
                this.setFrame(0, (screen_H - MIN_H) / 2, (int) bitmap_W, ((screen_H - MIN_H) / 2) + MIN_H);
            }
        }
    }

    public void setStartPoint(Point point) {
        startX = point.getPointX();
        startY = point.getPointY();
        invalidate();
        //requestLayout();
    }

    public void setStationPoint(Point point) {
        stationX = point.getPointX();
        stationY = point.getPointY();
        invalidate();
    }

    public void setStopPoint(Point point) {
        stopX = point.getPointX();
        stopY = point.getPointY();
        invalidate();
        //requestLayout();
    }

    public void test() {
        postInvalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (detailsChangeList.size() != 0) {    //画线条
            setOnDrawLineDot(canvas);
        }

        if (startX != 0 || stopX != 0) {            //画起点终点
            setOnDrawStartStop(canvas);
        }
        if (stationX != 0) {
            setOnDrawStartStop(canvas);
        }
    }

    /**
     * 画线和点
     *
     * @param canvas
     */
    public void setOnDrawLineDot(Canvas canvas) {
        kx = (double) NumUtil.MAP_WIDTH / getWidth();
        ky = (double) NumUtil.MAP_HEIGHT / getHeight();
        paint = new Paint();
        //加粗  划线
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //		paint.setColor(getResources().getColor(R.color.green));
        paint.setStrokeWidth(24);
        //加粗  划线

        for (int i = 0; i < detailsChangeList.size(); i++) {                                            //路线方案
            List<DetailsChange> detailsChange = detailsChangeList.get(i).getDetailsChange();
            for (int k = 0; k < detailsChange.size(); k++) {                                            //路线详细
                List<Point> pointList = detailsChange.get(k).getPointList();
                for (int j = 0; j < pointList.size(); j++) {                                            //路线列表
                    Point pointCurrent = pointList.get(j);
                    if (j != 0) {
                        Point pointOn = pointList.get(j - 1);
                        //画线
                        if (pointOn.getPointX() == 1576.0 && pointOn.getPointY() == 1252.0 &&
                                pointCurrent.getPointX() == 1550.0 && pointCurrent.getPointY() == 1448.0) {

                            canvas.drawLine((float) (pointOn.getPointX() / kx), (float) (pointOn.getPointY() / ky), (float) (1649 / kx), (float) (1308 / ky), paint);
                            canvas.drawLine((float) (1649 / kx), (float) (1308 / ky), (float) (pointCurrent.getPointX() / kx), (float) (pointCurrent.getPointY() / ky), paint);
                        } else if (pointOn.getPointX() == 1550.0 && pointOn.getPointY() == 1448.0 &&
                                pointCurrent.getPointX() == 1576.0 && pointCurrent.getPointY() == 1252.0) {
                            canvas.drawLine((float) (pointOn.getPointX() / kx), (float) (pointOn.getPointY() / ky), (float) (1649 / kx), (float) (1308 / ky), paint);
                            canvas.drawLine((float) (1649 / kx), (float) (1308 / ky), (float) (pointCurrent.getPointX() / kx), (float) (pointCurrent.getPointY() / ky), paint);
                        } else {
                            canvas.drawLine((float) (pointOn.getPointX() / kx), (float) (pointOn.getPointY() / ky), (float) (pointCurrent.getPointX() / kx), (float) (pointCurrent.getPointY() / ky), paint);
                        }
                        if (k != 0 && j == 1) {
                            canvas.drawBitmap(bitmapExchange, (int) (pointOn.getPointX() / kx - 48), (int) (pointOn.getPointY() / ky - 48), paint);
                        }
                    } else {
                        Colors colors = NumUtil.colorsList.get(detailsChange.get(k).getDirection_id() + "");
                        paint.setARGB(255, colors.getColorR(), colors.getColorG(), colors.getColorB());
                    }
                    //画圆
                    canvas.drawCircle((float) (pointCurrent.getPointX() / kx),
                            (float) (pointCurrent.getPointY() / ky),
                            25, paint);
                }
            }
            break;
        }
    }

    /**
     * 画起点终点
     *
     * @param canvas
     */
    public void setOnDrawStartStop(Canvas canvas) {
        //判断双指和单指时候指定startStatu，stopStatu
        kx = (double) NumUtil.MAP_WIDTH / getWidth();
        ky = (double) NumUtil.MAP_HEIGHT / getHeight();
        paint = new Paint();
        //加粗
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.setColor(Color.RED);
        if (startX != 0) {
            canvas.drawBitmap(bitmapStart, (int) (startX / kx - 48), (int) (startY / ky - 120), paint);
        }
        if (stopX != 0) {
            canvas.drawBitmap(bitmapStop, (int) (stopX / kx - 48), (int) (stopY / ky - 120), paint);
        }
        if (stationX != 0) {
            canvas.drawBitmap(bitmapStation, (int) (stationX / kx - 48), (int) (stationY / ky - 90), paint);
        }
    }
}