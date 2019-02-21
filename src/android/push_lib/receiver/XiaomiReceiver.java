package com.nxt.push.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.alibaba.fastjson.JSON;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;
import java.util.Map;

import com.nxt.push.receiver.NXTReceiver;
import com.eegrid.phonegap.JSRunner;

/**
 * 用于接收小米Push推送过来的消息,所有方法运行在非 UI 线程中。
 */
public class XiaomiReceiver extends PushMessageReceiver {

  public XiaomiReceiver() {
    super();
  }

  /**
   * 方法用来接收服务器向客户端发送的自定义动作通知消息，
   * 这个回调方法会在用户手动点击通知后触发
   */
  @Override
  public void onNotificationMessageClicked(Context context, MiPushMessage message) {
    NXTReceiver.pushLog("XiaomiReceiver.onNotificationMessageClicked: " + message.toString());
    Map<String, String> extra = message.getExtra();
    if(extra != null && extra.size() > 0){
      String strExtra = JOSN.toJSONString(extra);
      NXTReceiver.pushLog("XiaomiReceiver.onNotificationMessageClicked:strExtra-> " + strExtra);
      JSRunner.onNotificationMessageClicked(strExtra);
    }
  }

  /**
   * 方法用来接收客户端向服务器发送命令后的响应结果。
   */
  @Override
  public void onCommandResult(Context context, MiPushCommandMessage message) {
    NXTReceiver.pushLog("XiaomiReceiver.onCommandResult: " + message.getCommand());

    String command = message.getCommand();
    List<String> arguments = message.getCommandArguments();
    String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);

    if (MiPushClient.COMMAND_REGISTER.equals(command)) {
      SharedPreferences sharedPreference =
        context.getSharedPreferences(NXTReceiver.JINGOAL_PUSH_SP, Context.MODE_PRIVATE);
      SharedPreferences.Editor edit = sharedPreference.edit();
      edit.putString(NXTReceiver.SP_KEY_XIAOMI_TOKEN, cmdArg1);
      edit.commit();
    } 

    if (message.getResultCode() == ErrorCode.SUCCESS) {
      JSRunner.onCommandResult(message.getCommand(), true);
    }else{
      JSRunner.onCommandResult(message.getCommand(), false);
    }
  }


  @Override
  public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
    NXTReceiver.pushLog("XiaomiReceiver.onReceiveRegisterResult: " + message.toString());

    String command = message.getCommand();
    List<String> arguments = message.getCommandArguments();
    String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
    if (MiPushClient.COMMAND_REGISTER.equals(command)) {
        if (message.getResultCode() == ErrorCode.SUCCESS) {
          SharedPreferences sharedPreference =
          context.getSharedPreferences(NXTReceiver.JINGOAL_PUSH_SP, Context.MODE_PRIVATE);
          SharedPreferences.Editor edit = sharedPreference.edit();
          edit.putString(NXTReceiver.SP_KEY_XIAOMI_TOKEN, cmdArg1);
          edit.commit();
        }
    } 
    // super.onReceiveRegisterResult(context, message);
  }

  /**
   * 接收到通知消息
   *
   * @param context
   * @param miPushMessage
   */
  @Override
  public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
    JSRunner.onReceivePassThroughMessage(miPushMessage.getTitle());
  }

  @Override
  public void onReceiveMessage(Context context, MiPushMessage miPushMessage) {
    JSRunner.onReceivePassThroughMessage(miPushMessage.getTitle());
  }

  @Override
  public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
    JSRunner.onReceivePassThroughMessage(miPushMessage.getTitle());
  }

}
