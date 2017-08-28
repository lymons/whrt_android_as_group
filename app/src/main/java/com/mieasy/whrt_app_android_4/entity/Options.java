package com.mieasy.whrt_app_android_4.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Options entity. 服务器配置信息表
 */
@Table("OPTIONS")
public class Options implements Parcelable {
	@Ignore
	public static String OPTIONS_ID="OPTION_ID";
	@Ignore
	public static String OPTIONS_NAME="OPTION_NAME";
	@Ignore
	public static String OPTIONS_VALUE="OPTION_VALUE";
	@Ignore
	public static String OPTIONS_COMMENTS="OPTION_COMMENTS";
	
	@Column(value = "OPTION_ID")
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	private Integer optionId;			//配置表ID
	
	@Column(value = "OPTION_NAME")
	private String optionName;			//配置的名称
	
	@Column(value = "OPTION_VALUE")	
	private String optionValue;			//域名
	
	@Column(value = "OPTION_COMMENTS")
	private String optionComments;		//域名服务器详细介绍

	/** minimal constructor */
	public Options(Integer optionId,String optionName, String optionValue,String optionComments) {
		this.optionId = optionId;
		this.optionName = optionName;
		this.optionValue = optionValue;
		this.optionComments = optionComments;
	}

	// Property accessors

	public Integer getOptionId() {
		return this.optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	public String getOptionName() {
		return this.optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionValue() {
		return this.optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public String getOptionComments() {
		return this.optionComments;
	}

	public void setOptionComments(String optionComments) {
		this.optionComments = optionComments;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(optionId);
		dest.writeString(optionName);
		dest.writeString(optionValue);
		dest.writeString(optionComments);
	}
	
	public static final Parcelable.Creator<Options> CREATOR = new Creator<Options>() {
		
		@Override
		public Options[] newArray(int size) {
			return new Options[size];
		}
		
		@Override
		public Options createFromParcel(Parcel source) {
			return new Options(source.readInt(), source.readString(), source.readString(), source.readString());
		}
	};
}