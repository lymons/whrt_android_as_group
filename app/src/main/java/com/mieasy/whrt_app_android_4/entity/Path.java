package com.mieasy.whrt_app_android_4.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 路线信息表
 * @author Administrator
 */
@Table("PATH")
public class Path {
	@Ignore
	public static String PATH_ID="PATH_ID";
	@Ignore
	public static String START_NUM = "START_NUM";
	@Ignore
	public static String END_NUM = "END_NUM";
	@Ignore
	public static String JSON_DATA = "JSON_DATA";
//	@Ignore
//	public static String FLAG = "FLAG";
	@Ignore
	public static String DISTANCE = "DISTANCE";
	@Ignore
	public static String PRICE = "PRICE";
	@Ignore
	public static String TIME = "TIME";
	
	@Column(value = "PATH_ID")
	@PrimaryKey(AssignType.AUTO_INCREMENT)
	private Integer id;				//站点ID
	
	@Column(value = "START_NUM")
	private Integer startNum;		//起点NUM
	
	@Column(value = "END_NUM")	
	private Integer endNum;			//终点NUM
	
	@Column(value = "JSON_DATA")
	private String jsonData;		//json
	
//	@Column(value = "FLAG")
//	private Integer flag;			//修改类型

	@Column(value = "DISTANCE")
	private String distance;		//里程

	@Column(value = "PRICE")
	private String price;			//价格

	@Column(value = "TIME")
	private int time;			//时间

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStartNum() {
		return startNum;
	}

	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}

	public Integer getEndNum() {
		return endNum;
	}

	public void setEndNum(Integer endNum) {
		this.endNum = endNum;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

//	public Integer getFlag() {
//		return flag;
//	}
//
//	public void setFlag(Integer flag) {
//		this.flag = flag;
//	}

	public Path(Integer id, Integer startNum, Integer endNum, String jsonData, /*Integer flag,*/ String distance, String price) {
		this.id = id;
		this.startNum = startNum;
		this.endNum = endNum;
		this.jsonData = jsonData;
//		this.flag = flag;
		this.distance = distance;
		this.price = price;
	}

	public Path() {
		super();
	}
}