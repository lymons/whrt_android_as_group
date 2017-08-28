package com.mieasy.whrt_app_android_4.bean;

import java.io.Serializable;

/**
 * @author itlanbao
 * IT蓝豹
 * 解析获取用户基本信息
 */
public class UserBaseInfo implements Serializable {

	private String code;
	private Msg msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public Msg getMsg() {
		return msg;
	}

	public void setMsg(Msg msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "UserBaseInfo{" +
				"code='" + code + '\'' +
				", msg=" + msg +
				'}';
	}
}
