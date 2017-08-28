package com.mieasy.whrt_app_android_4.bean;

/**
 * @author itlanbao
 * 处理网络的参数常量类
 */
public class UrlConstance {

   public static final String LOGIN_URL = "http://app.whrt.office.mieasy.com/api/doUserAction.php?mode=LoginUser";
   public static final String LOGIN_URL2 = "http://app2.whrt.gov.cn/api/doUserAction.php?mode=LoginUser";
   public static final String REGISTER_URL = "http://app.whrt.office.mieasy.com/api/doUserAction.php?mode=AddUser";
   public static final String REGISTER_URL2 = "http://app2.whrt.gov.cn/api/doUserAction.php?mode=AddUser";
   public static  final  String  REVISE_URL="http://app2.whrt.gov.cn/api/doUserAction.php?mode=UpdaUser";
   public static  final  String  GET_URL="http://app.whrt.office.mieasy.com/api/doUserAction.php?mode=GetUserInfoByUser";
   public static final String APP_URL_SMS = "http://www.whrt.gov.cn/Handler/LoginUserInfo.ashx?mode=SendMsg";
   public static  final  String  UPDATE_PASSWORD="http://app.whrt.office.mieasy.com/api/doUserAction.php?mode=UpdaPass";
}
