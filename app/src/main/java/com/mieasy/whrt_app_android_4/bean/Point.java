package com.mieasy.whrt_app_android_4.bean;

/**
 * 传递的点坐标的对象
 * @author Administrator
 *
 */
public class Point {
	private int pointX;
	private int pointY;
	
	private Double dx;
	private Double dy;
	
	public Point() {
		super();
	}
	public Point(int pointX, int pointY) {
		super();
		this.pointX = pointX;
		this.pointY = pointY;
	}
	public int getPointX() {
		return pointX;
	}
	public int getPointY() {
		return pointY;
	}

	public void setPointX(int pointX) {
		this.pointX = pointX;
	}
	
	public void setPointY(int pointY) {
		this.pointY = pointY;
	}
	
	public Point(Double pointX, Double pointY) {
		super();
		dx = new Double(pointX);
		dy = new Double(pointY);
		this.pointX = dx.intValue();
		this.pointY = dy.intValue();
	}
}
