package com.mieasy.whrt_app_android_4.util;

import com.mieasy.whrt_app_android_4.bean.Colors;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

/**
 * 画起点终点坐标的
 * @author Administrator
 *
 */
public class DrawImagePath {
	private static Bitmap mbmpTest;
	private static Paint paintBigCircle,paintSmallCircle,paintShaderCircle,paintLine;

	/**
	 * 画起点
	 * @param w				宽
	 * @param h				高
	 * @param colors		颜色
	 * @return
	 */
	public static Bitmap drawStartPointImage(int w,int h,Colors colors){
		mbmpTest = Bitmap.createBitmap(w,h, Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(mbmpTest);
		canvasTemp.drawCircle(w/2, w/2, w/5, getPaintBigCircle(colors));
		canvasTemp.drawLine(w/2, h/2+w/5, w/2,h, getPaintLine(colors));
		return mbmpTest;
	}
	
	/**
	 * 画经过点
	 * @param w				宽
	 * @param h				高
	 * @param colors		颜色
	 * @return
	 */
	public static Bitmap drawAfterPointImage(int w,int h,Colors colors){
		mbmpTest = Bitmap.createBitmap(w,h, Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(mbmpTest);
		canvasTemp.drawLine(w/2, h/2-w/8, w/2,0, getPaintLine(colors));
		canvasTemp.drawCircle(w/2, w/2, w/8, getPaintSmallCircle(colors));
		canvasTemp.drawLine(w/2, h/2+w/8, w/2,h, getPaintLine(colors));
		return mbmpTest;
	}

	/**
	 * 画终点
	 * @param w				宽
	 * @param h				高
	 * @param colors		颜色
	 * @return
	 */
	public static Bitmap drawStopPointImage(int w,int h,Colors colors){
		mbmpTest = Bitmap.createBitmap(w,h, Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(mbmpTest);
		canvasTemp.drawCircle(w/2, w/2, w/5, getPaintBigCircle(colors));
		canvasTemp.drawLine(w/2, h/2-w/5, w/2,0, getPaintLine(colors));
		return mbmpTest;
	}
	
	/**
	 * 非渐变中转站
	 * @param w				
	 * @param h
	 * @param colorsTop
	 * @param colorsBottom
	 * @return
	 */
	public static Bitmap drawTransferPointImage(int w,int h,Colors colorsTop,Colors colorsBottom){
		mbmpTest = Bitmap.createBitmap(w,h, Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(mbmpTest);
		canvasTemp.drawLine(w/2, h/2-w/5, w/2,0, getPaintLine(colorsTop));
		canvasTemp.drawLine(w/2, h/2+w/5, w/2,h, getPaintLine(colorsBottom));
		canvasTemp.drawCircle(w/2, w/2, w/5, getPaintBigCircle(colorsTop));
		return mbmpTest;
	}

	/**
	 * 渐变中转站
	 * @param w				
	 * @param h
	 * @param colorsTop
	 * @param colorsBottom
	 * @return
	 */
	public static Bitmap drawExchangePointImage(int w,int h,Colors colorsTop,Colors colorsBottom){
		mbmpTest = Bitmap.createBitmap(w,h, Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(mbmpTest);
		canvasTemp.drawLine(w/2, h/2-w/5, w/2,0, getPaintLine(colorsTop));
		canvasTemp.drawCircle(w/2, w/2, w/5, getPaintBigShaderCircle(colorsTop,colorsBottom,w,h));
		canvasTemp.drawLine(w/2, h/2+w/5, w/2,h, getPaintLine(colorsBottom));
		return mbmpTest;
	}

	//渐变空心圆画笔
	public static Paint getPaintBigShaderCircle(Colors colorsTop,Colors colorsBottom,int w,int h){
		paintShaderCircle = new Paint();
		paintShaderCircle.setStrokeWidth(10);			//画笔宽度
		paintShaderCircle.setAntiAlias(true);			//消除锯齿
		paintShaderCircle.setStyle(Paint.Style.STROKE); //画空心圆
		Color color = new Color();
		int[] i = new int[] {
				Color.argb(255, colorsTop.getColorR(), colorsTop.getColorG(), colorsTop.getColorB()),
				Color.argb(255,colorsBottom.getColorR(),colorsBottom.getColorG(), colorsBottom.getColorB()),Color.BLUE};
		Shader mShader = new LinearGradient(0,0,w,h,i,null,Shader.TileMode.REPEAT);
		//新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。
		//连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。
		//下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变  
		paintShaderCircle.setShader(mShader);
		return paintShaderCircle;
	}

	//空心大圆形画笔
	public static Paint getPaintBigCircle(Colors colors){
		paintBigCircle = new Paint();
		paintBigCircle.setARGB(255, colors.getColorR(), colors.getColorG(), colors.getColorB());
		paintBigCircle.setStrokeWidth(12);				//画笔宽度
		paintBigCircle.setAntiAlias(true);				//消除锯齿
		paintBigCircle.setStyle(Paint.Style.STROKE); 	//画空心圆
		return paintBigCircle;
	}

	//空心小圆形画笔
	public static Paint getPaintSmallCircle(Colors colors){
		paintSmallCircle = new Paint();
		paintSmallCircle.setARGB(255, colors.getColorR(), colors.getColorG(), colors.getColorB());
		paintSmallCircle.setStrokeWidth(8);				//画笔宽度
		paintSmallCircle.setAntiAlias(true);			//消除锯齿
		paintSmallCircle.setStyle(Paint.Style.STROKE);  //画空心圆
		return paintSmallCircle;
	}

	//线条画笔
	public static Paint getPaintLine(Colors colors){
		paintLine = new Paint();
		paintLine.setColor(Color.GRAY);
		paintLine.setARGB(255, colors.getColorR(), colors.getColorG(), colors.getColorB());
		paintLine.setStrokeWidth(12);					//画笔宽度
		paintLine.setAntiAlias(true);					//消除锯齿
		return paintLine;
	}
}

