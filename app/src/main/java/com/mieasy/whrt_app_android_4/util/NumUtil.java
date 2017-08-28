package com.mieasy.whrt_app_android_4.util;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.bean.Colors;

import java.util.HashMap;
import java.util.Map;

public class NumUtil {
	public static final String DATABASE_NAME ="";
	public static String ID="POISITION_ID";
	public static String FILE_PATH;		//文件路径
	public static String CACHE_PATH;	//缓存路径
	public static String DB_NAME = "test.db";	//上一个版本的数据库名称
	public static int DB_SIZE = 0;	//缓存路径
	
	public final static String SHAREPRE_WHRTONCE = "WHRTONCE";		//是否第一次访问
	public final static String isFirstUse = "isFirstUse";			//是否第一次访问
	public final static String isTextSize = "isTextSize";				//字体大小

	//方向  跟  颜色值
	public static HashMap<String,Colors> colorsList = new HashMap<String,Colors>();
	//线路id 线路名称
	public static HashMap<String,String> linesMap = new HashMap<String,String>();
	//方向id  方向名称
	public static HashMap<String,String> directionsMap = new HashMap<String,String>();
	//设置index.json需要更新的标识
	public final static String HOMEPAGETAG = "HOMEPAGETAG";
	public final static String ANDROIDDB = "ANDROIDDB";
	public final static String BUILDINGLINE = "BUILDINGLINE";
	public final static String QAUPDATETIME = "QAUPDATETIME";
	public final static String NEWSUPDATETIME = "NEWSUPDATETIME";
	public static HashMap<String,String> indexDateMap = new HashMap<String,String>();
	
	//服务器1
	//public final static String STATUS = "http://appdb2.whrt.gov.cn/status";//原始的

	//app1.whrt.gov.cn 和 app2.whrt.gov.cn
	public final static String APP_URL_SERVER_1 = "http://app1.whrt.gov.cn/";
	public final static String APP_URL_SERVER_2 = "http://app2.whrt.gov.cn/";
    public final static String APP_URL_SERVER_3 = "http://app2.whrt.gov.cn/api";

    //内网测试
//	public final static String APP_URL_SERVER_1 = "http://whrt.office.mieasy.com/";
//	public final static String APP_URL_SERVER_2 = "http://whrt.office.mieasy.com/";
//	public final static String APP_URL_SERVER_3 = "http://app.whrt.office.mieasy.com/api";

	public final static String STATUS = APP_URL_SERVER_3+"/status.php";
//	public static String INTRA_IMAGE_URL = "http://whrt.office.mieasy.com/image/";
//	public static String INTRA_IMAGE_URL_NET = "http://gunicorn.office.mieasy.com/image/";
//	public static String INTENT_URL = "http://gunicorn.office.mieasy.com/";
	//地图图片的尺寸
	public static int MAP_WIDTH=3000;
	public static int MAP_HEIGHT=3000;
	//图片放大的最大尺寸
	public static int MAP_MAX_SCALE=4;
	//点击图片查找站点 的 偏移量
	public static int MAP_POINT_EXCURSION = 28;   

	//轨道线路数
	public static String STATIONLINENUM = "STATIONLINENUM";

	//起点、终点
	public static String STATIONS = "STATIONS";
	public static String STATIONS_START="STATIONS_START";
	public static String STATIONS_STOP="STATIONS_STOP";
	//给站内站外传值
	public static String JUMP_BUNDLE = "JUMP_BUNDLE";
	public static String JUMP_INTENT = "JUMP_INTENT";
	public static String JUMP_TYPE = "JUMP_TYPE";
	public static String JUMP_PARCELABLE = "JUMP_PARCELABLE";
	
	public static String ITEM_LOGO="ITEM_LOGO";
	public static int ITEM_LOGO_NOTICE = R.drawable.notice_img;
	public static int ITEM_LOGO_BIAO=R.drawable.biao_item;
	public static int ITEM_LOGO_PRO=R.drawable.pro_ing_item;
	public static int ITEM_LOGO_ASK=R.drawable.question_item;
	public static int ITEM_LOGO_COMPANY=R.drawable.company_item;
	public static int ITEM_LOGO_DEVELOP=R.drawable.develop_item;
	public static int ITEM_LOGO_PROLINE=R.drawable.trastion_hand;

	//选择站内站外
	public static String STATION_ITEM="STATION_ITEM";
	/***********************************所有站点*************************************/
	public static String STATIONLINE="STANDLINE";
	public static int STANDLINE_ONE = 1;
	public static int STANDLINE_TWO = 2;
	public static int STANDLINE_THREE = 3;
	public static int STANDLINE_FOUR = 4;
	public static String[] STATIONLINE_STR = {"1号线","2号线","3号线","4号线","6号线"};
	public static Map<String, String[]> DETAIL;

	public static String NEWS_URL="NEWS_URL";
//	public static String NEWS_URL_DATEIL="NEWS_URL_DATEIL";
	public static String NEWS_DATEIL_TITLE="NEWS_DATEIL_TITLE";
	
	//fragment类别
	public final static String FRAGMENT_TYPE="FRAGMENT_TYPE";
	public final static String JSON_URL = "";
	//index
	public final static String INDEX_JSON = "index.json";
	public final static String JSON_SUFFIX = ".json";
	//APK更新
	public final static String APK_UPDATE = "apk.json";
	//首页轮播json
	public final static String URL_LOCAL_HOMEPAGE="homepage.json";
	//在建线路
	public final static String URL_LOCAL_LINE_ZJXL="buildingline.json";
	//地铁新闻 ---集团快讯（item1） json
	public final static String URL_LOCAL_NEWS_JTKX=JSON_URL+"news/jtkx/";
	//地铁新闻 ---地铁运营（item2） json
	public final static String URL_LOCAL_NEWS_DTYY=JSON_URL+"news/dtyy/";
	//地铁新闻 ---招标中标（item3） json
	public final static String URL_LOCAL_NEWS_ZBZB=JSON_URL+"news/zbzb/"; 
	//通知通告
	public final static String URL_LOCAL_NEWS_TZGG=JSON_URL+"news/tzgg/"; 
	//在建工程---地铁建设（item1）json
	public final static String URL_LOCAL_NEWS_DTJS=JSON_URL+"news/dtjs/"; 
	//在建工程---建设公司（item2）json
	public final static String URL_LOCAL_NEWS_JSGS=JSON_URL+"news/jsgs/"; 
	//在建工程---土地开发（item3）json
	public final static String URL_LOCAL_NEWS_TDKF=JSON_URL+"news/tdkf/"; 
	//Q&A
	public final static String URL_LOCAL_NEWS_ZXWD=JSON_URL+"qa/";
	//默认收藏列表
	public final static String APP_URL_SERVER_WHRT =APP_URL_SERVER_3+"/doUserAction.php?mode=defaultrecord";
	//添加收藏
	public final static String URL_COLLECTION_ADD=APP_URL_SERVER_3+"/doUserAction.php?mode=addrecord";
    //收藏列表(用户登陆后的收藏列表)
	public final static String URL_COLLECTION_LOGIN_LIST=APP_URL_SERVER_3+"/doUserAction.php?mode=listrecord";
    //收藏删除
	public final static String URL_COLLECTION_DELETE=APP_URL_SERVER_3+"/doUserAction.php?mode=delrecord";
	//修改收藏
	public final static String URL_COLLECTION_UPDATE=APP_URL_SERVER_3+"/doUserAction.php?mode=editrecord";
	//在线咨询
	public final static String URL_ONLINE_CONSULTING = "http://www.whrt.gov.cn/Handler/LoginUserInfo.ashx?";

//
//	//---首页轮播
//	public static String URL_GET_ITEM_BY_TYPE_HOMEPAGE =APP_URL_SERVER_2+URL_LOCAL_HOMEPAGE;
//	//新闻列表---集团快讯
//	public static String URL_GET_ITEM_BY_TYPE_38 =APP_URL_SERVER_2+URL_LOCAL_NEWS_JTKX;
//	//新闻列表---地铁建设
//	public static String URL_GET_ITEM_BY_TYPE_51 =APP_URL_SERVER_2+URL_LOCAL_NEWS_DTJS;
//	//新闻列表---地铁运营
//	public static String URL_GET_ITEM_BY_TYPE_56 =APP_URL_SERVER_2+URL_LOCAL_NEWS_DTYY;
//	//新闻列表---土地开发
//	public static String URL_GET_ITEM_BY_TYPE_74 =APP_URL_SERVER_2+URL_LOCAL_NEWS_TDKF;
//	//新闻列表---通知公告
//	public static String URL_GET_ITEM_BY_TYPE_92 =APP_URL_SERVER_2+URL_LOCAL_NEWS_TZGG;
//	//新闻列表---招标中标
//	public static String URL_GET_ITEM_BY_TYPE_93=APP_URL_SERVER_2+URL_LOCAL_NEWS_ZBZB;
//	//新闻列表---建设公司
//	public static String URL_GET_ITEM_BY_TYPE_110 =APP_URL_SERVER_2+URL_LOCAL_NEWS_JSGS;
//	//在线咨询
//	public static String URL_GET_ITEM_BY_ITEM_ASK = APP_URL_SERVER_2+URL_LOCAL_NEWS_ZXWD;
//	//线路图
//	public static String UTL_GET_ITEM_BY_ITEM_LINE = APP_URL_SERVER_2+URL_LOCAL_LINE_ZJXL;
	/********************************Asset文件夹下的文件************************************/
	//ImageLoader URI
	public static String IMAGE_HTTP_URI = "http://site.com/"; // from Web	Example:"http://site.com/image.png"
	public static String IMAGE_FILES_URI = "file:///mnt/sdcard/"; // from SD card  Example:"file:///mnt/sdcard/image.png"
	public static String IMAGE_CONTENT_URI = "content://media/external/audio/albumart/1"; // from content provider  Example:"content://media/external/audio/albumart/1"
	public static String IMAGE_ASSETS_URI = "assets://"; // from assets	Example:"assets://image.png"
	public static String DB_NAME_ORIGINAL="";
	public static final String START_STATION ="STARTSTATION";
	public static final String STOP_STATION ="STOPSTATION";
	public static final String MAPNAV ="MAPNAV";
}