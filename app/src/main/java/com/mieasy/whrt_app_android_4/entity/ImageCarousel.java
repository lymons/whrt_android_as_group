package com.mieasy.whrt_app_android_4.entity;

public class ImageCarousel {
	private String subject;
	private String Unid;
	private String picPath;
	private String picTitle;
	private String publishtime;
	public ImageCarousel(String subject, String unid, String picPath, String picTitle, String publishtime) {
		super();
		this.subject = subject;
		Unid = unid;
		this.picPath = picPath;
		this.picTitle = picTitle;
		this.publishtime = publishtime;
	}
	public ImageCarousel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getUnid() {
		return Unid;
	}
	public void setUnid(String unid) {
		Unid = unid;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getPicTitle() {
		return picTitle;
	}
	public void setPicTitle(String picTitle) {
		this.picTitle = picTitle;
	}
	public String getPublishtime() {
		return publishtime;
	}
	public void setPublishtime(String publishtime) {
		this.publishtime = publishtime;
	}
	
	
}
