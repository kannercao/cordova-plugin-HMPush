package com.eegrid.phonegap;

import android.content.Context;
import android.util.Log;

import com.nxt.push.receiver.NXTReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class JSRunner  {

  /**
   * 接收透传消息
   *
   * @param context
   * @param message 收到的具体的消息内容
   */
  public static void onReceivePassThroughMessage(String message) {
    NXTReceiver.pushLog("JSRunner.onReceivePassThroughMessage: " + message);
    String format = "window.plugins.NXTPlugin.receiveNotificationInAndroidCallback(%s);";
    try {
      JSONObject data = new JSONObject();
      data.put("extras", message);
      final String js = String.format(format, data.toString());
      NXTPushPlugin.runJSOnUiThread(js, false);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * 小米/华为 通知栏消息被点击（事实上，不仅仅包括被点击的消息 ）
   *
   * @param context
   * @param message 收到的具体的消息内容
   */
  public static void onNotificationMessageClicked(String message) {
    NXTReceiver.pushLog("JSRunner.onNotificationMessageClicked: " + message);
    String format = "window.plugins.NXTPlugin.openNotificationInAndroidCallback(%s);";
    try {
      JSONObject data = new JSONObject();
      data.put("extras", message);
      final String js = String.format(format, data.toString());
      NXTPushPlugin.runJSOnUiThread(js, true);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }


  /**
   * 接收命令设置返回信息
   *
   * @param context
   * @param command 执行的命令的类型
   * @param success 执行命令的结果，true or false
   */
  public static void onCommandResult(String command, boolean success) {
    NXTReceiver.pushLog("JSRunner.onCommandResult: " + command + " -> " + success);
  }

  public static void onHuaWeiRigisterResult(String token, boolean success) {
    NXTReceiver.pushLog("JSRunner.onHuaWeiRigisterResult: " + token);
    String format = "window.plugins.NXTPlugin.onReceiveHuaWeiToken(%s);";
    try {
      JSONObject data = new JSONObject();
      data.put("token", token);
      final String js = String.format(format, data.toString());
      NXTPushPlugin.runJSOnUiThread(js, false);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
