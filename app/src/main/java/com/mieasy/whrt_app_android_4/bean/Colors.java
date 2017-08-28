package com.mieasy.whrt_app_android_4.bean;

public class Colors {
	private int colorR;
	private int colorG;
	private int colorB;
	private int colorA;
	public int getColorR() {
		return colorR;
	}
	public void setColorR(int colorR) {
		this.colorR = colorR;
	}
	public int getColorG() {
		return colorG;
	}
	public void setColorG(int colorG) {
		this.colorG = colorG;
	}
	public int getColorB() {
		return colorB;
	}
	public void setColorB(int colorB) {
		this.colorB = colorB;
	}
	public int getColorA() {
		return colorA;
	}
	public void setColorA(int colorA) {
		this.colorA = colorA;
	}
	public Colors(int colorR, int colorG, int colorB, int colorA) {
		super();
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
		this.colorA = colorA;
	}
	public Colors() {
		super();
	}
}
