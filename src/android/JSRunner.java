package com.eegrid.phonegap;

import android.content.Context;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
    String format = "window.plugins.HMPlugin.receiveNotificationInAndroidCallback(%s);";
    try {
      JSONObject data = new JSONObject();
      data.put("extras", message);
      final String js = String.format(format, data.toString());
      HMPushPlugin.runJSOnUiThread(js, false);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * 华为 通知栏消息被点击
   *
   * @param context
   * @param message 收到的具体的消息内容
   */
  public static void onNotificationMessageClicked(String message) {
    NXTReceiver.pushLog("JSRunner.onNotificationMessageClicked: " + message);
    String format = "window.plugins.HMPlugin.openNotificationInAndroidCallback(%s);";
    try {
      JSONObject data = new JSONObject();
      data.put("extras", message);
      final String js = String.format(format, data.toString());
      HMPushPlugin.runJSOnUiThread(js, true);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * 小米 通知栏消息被点击
   *
   * @param context
   * @param message 收到的具体的消息内容
   */
  public static void onNotificationMessageClicked(Map<String, String> extras) {
    String format = "window.plugins.HMPlugin.openNotificationInAndroidCallback(%s);";
    JSONObject data = getExtrasObject(extras);
    NXTReceiver.pushLog("JSRunner.onNotificationMessageClicked: " + data.toString());
    final String js = String.format(format, data.toString());
    HMPushPlugin.runJSOnUiThread(js, true);
  }

  /**
   * 获取JSON数据对象
   *
   * @param extras 扩展数据
   */
  private static JSONObject getExtrasObject(Map<String, String> extras) {
    JSONObject data = new JSONObject();
    try {
        JSONObject jExtras = new JSONObject();
        for (Entry<String, String> entry : extras.entrySet()) {
            jExtras.put(entry.getKey(), entry.getValue());
        }
        if (jExtras.length() > 0) {
            data.put("extras", jExtras);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return data;
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
    String format = "window.plugins.HMPlugin.onReceiveHuaWeiToken(%s);";
    try {
      JSONObject data = new JSONObject();
      data.put("token", token);
      final String js = String.format(format, data.toString());
      HMPushPlugin.runJSOnUiThread(js, false);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
