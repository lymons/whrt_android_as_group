package com.mieasy.whrt_app_android_4.bean;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 在线问答的json对象
 * @author Administrator
 *
 */
public class Translation implements Parcelable{
	private String remark;		//备注
	private String depart;		//部门
	private String title;		//标题
	private String question;	//问题
	private String unid;		//unid
	private String phoneNumber;	//电话号码
	private String submitDate;	//提交时间
	private String replyDate;	//回答时间
	private String reply;		//回答者
	private int _class;			//不知道什么鬼
	private String email;		//电子邮箱
	private String clientName;	//用户名
	
	public Translation() {
		super();
	}
	
	public Translation(String remark, String depart, String title, String question, String unid, String phoneNumber,
			String submitDate, String replyDate, String reply, int _class, String email, String clientName) {
		super();
		this.remark = remark;
		this.depart = depart;
		this.title = title;
		this.question = question;
		this.unid = unid;
		this.phoneNumber = phoneNumber;
		this.submitDate = submitDate;
		this.replyDate = replyDate;
		this.reply = reply;
		this._class = _class;
		this.email = email;
		this.clientName = clientName;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getUnid() {
		return unid;
	}
	public void setUnid(String unid) {
		this.unid = unid;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	public String getReplyDate() {
		return replyDate;
	}
	public void setReplyDate(String replyDate) {
		this.replyDate = replyDate;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public int get_class() {
		return _class;
	}
	public void set_class(int _class) {
		this._class = _class;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(remark);
		dest.writeString(depart);
		dest.writeString(title);
		dest.writeString(question);
		dest.writeString(unid);
		dest.writeString(phoneNumber);
		dest.writeString(submitDate);
		dest.writeString(replyDate);
		dest.writeString(reply);
		dest.writeInt(_class);
		dest.writeString(email);
		dest.writeString(clientName);
	}
	public static final Parcelable.Creator<Translation> CREATOR =new Creator<Translation>() {

		@Override
		public Translation[] newArray(int size) {
			return new Translation[size];
		}

		@Override
		public Translation createFromParcel(Parcel source) {
			return new Translation(source);
		}
	};
	 protected Translation(Parcel parcel) {
		 remark = parcel.readString();
		 depart = parcel.readString();
		 title = parcel.readString();
		 question = parcel.readString();
		 unid = parcel.readString();
		 phoneNumber = parcel.readString();
		 submitDate = parcel.readString();
		 replyDate = parcel.readString();
		 reply = parcel.readString();
		 _class = parcel.readInt();
		 email = parcel.readString();
		 clientName = parcel.readString();
     }
}