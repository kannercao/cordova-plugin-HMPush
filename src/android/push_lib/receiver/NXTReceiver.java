package com.nxt.push.receiver;

/**
 * 今目标推送消息广播接收器,用于接收不同厂家的第三方push sdk推送过来的消息,统一处理,
 * UI必须要继承该类,并注册在 AndroidManifest.xml 文件中
 */
public class NXTReceiver {

  /**
   * 保存PushSDK初始化时的一些数据
   */
  public static final String JINGOAL_PUSH_SP = "JingoalPushSP";
  //保存华为推送初始化生成的token
  public static final String SP_KEY_HUAWEI_TOKEN = "SPKeyHuaweiToken";
  //小米Push初始化返回的regId
  public static final String SP_KEY_XIAOMI_TOKEN = "SPKeyXiaomiRegId";

  private NXTReceiver(){}

  public static String  gsLogs = "";
  public static Boolean gsIsDebug = true;

  public static void pushLog(String log) {
    if(gsIsDebug){
      gsLogs += log + "\n";
    }
  }

  /**
   * Push的客户端类型
   * 小米:3 华为:4
   */
  public static class PushClientType {
    //小米
    public static final int XIAO_MI = 3;
    //华为
    public static final int HUA_WEI = 4;
  }
}
