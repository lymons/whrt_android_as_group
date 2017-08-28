package com.mieasy.whrt_app_android_4.view;

import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * view缩放
 * 
 * @author Administrator
 * 
 * @param <V>
 */
public abstract class ZoomView<V extends View>
{

	protected V view;

	// -----------------------------------------------
	private static final int NONE = 0;// 空
	private static final int DRAG = 1;// 按下第一个点
	private static final int ZOOM = 2;// 按下第二个点

	/** 屏幕上点的数量 */
	private int mode = NONE;

	/** 记录按下第二个点距第一个点的距离 */
	float oldDist;

	public ZoomView(V view)
	{
		this.view = view;
		setTouchListener();
	}

	private void setTouchListener()
	{
		view.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction() & MotionEvent.ACTION_MASK)
				{
				case MotionEvent.ACTION_DOWN:
					mode = DRAG;
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					if (oldDist > 10f)
					{
						mode = ZOOM;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (mode == ZOOM)
					{
						// 正在移动的点距初始点的距离
						float newDist = spacing(event);

						if (newDist > oldDist)
						{
							zoomOut();
						}
						if (newDist < oldDist)
						{
							zoomIn();
						}

					}
					break;
				}
				return true;
			}

			/**
			 * 求出2个触点间的 距离
			 * 
			 * @param event
			 * @return
			 */
			private float spacing(MotionEvent event)
			{
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				return FloatMath.sqrt(x * x + y * y);
			}
		});
	}

	protected abstract void zoomIn();

	protected abstract void zoomOut();
}