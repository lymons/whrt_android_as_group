package com.mieasy.whrt_app_android_4.bean;

public class UpdateApkInfo {
	private double size;
	private String fileName;
	private String version;
	private String desc;
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public UpdateApkInfo(double size, String fileName, String version, String desc) {
		super();
		this.size = size;
		this.fileName = fileName;
		this.version = version;
		this.desc = desc;
	}
	public UpdateApkInfo() {
		super();
	}
}
