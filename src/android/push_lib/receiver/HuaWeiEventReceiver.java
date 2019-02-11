package com.nxt.push.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.app.NotificationManager;

import com.huawei.hms.support.api.push.PushReceiver;

import com.eegrid.phonegap.JSRunner;

/**
 * 接收华为Push推送过来的消息
 */
public class HuaWeiEventReceiver extends PushReceiver {

  /**
   * pushToken申请成功后,会自动回调该方法,应用可以通过该接口中获取token。 yipeng:获取到相关的 token 后存储到
   * sharedPreference 中，可以在 PushClient 中获取
   */
  @Override
  public void onToken(Context context, String token, Bundle extras) {
    NXTReceiver.pushLog("HuaWeiReceiver.onToken:" + token);

    /**
     * 将华为 token 存储在本地，便于 unRegister 使用
     */
    SharedPreferences sharedPreference = context.getSharedPreferences(NXTReceiver.JINGOAL_PUSH_SP,
        Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = sharedPreference.edit();
    edit.putString(NXTReceiver.SP_KEY_HUAWEI_TOKEN, token);
    edit.commit();

    JSRunner.onHuaWeiRigisterResult(token, true);
  }

  /**
   * 推送消息下来时会自动回调onPushMsg方法实,现应用透传消息处理
   *
   * @param extras 扩展信息,暂时不启用
   * @return 统一返回false, 暂时没有作用（官方文档未予说明）
   */
  @Override
  public boolean onPushMsg(Context context, byte[] msg, Bundle extras) {
    NXTReceiver.pushLog("HuaWeiReceiver.onPushMsg:" + new String(msg));
    JSRunner.onReceivePassThroughMessage(new String(msg));
    return true;
  }

  @Override
  public void onPushState(Context context, boolean b) {
    String ret = b ? "true" : "false";
    NXTReceiver.pushLog("HuaWeiReceiver.onPushState:" + ret);
  }

  /**
   * 供子类继承实现,实现业务事件。该方法会在设置标签、LBS信息之后、点击打开通知栏消息、 点击通知栏上的按钮之后被调用。由业务决定是否调用该函数。
   *
   * @param event 事件类型,event为枚举类型,事件定义如下:
   * 
   *              <pre>
   *              {@code
   *               public static enum Event
   *               {
   *                  NOTIFICATION_OPENED,//通知栏中的通知被点击打开
   *              NOTIFICATION_CLICK_BTN,//通知栏中的通知被点击打开 PLUGINRSP, //标签上报回应 } }
   * 
   *              <pre/>
   */
  @Override
  public void onEvent(Context context, Event event, Bundle extras) {
    NXTReceiver.pushLog("HuaWeiReceiver.onEvent: " + event.toString());

    if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
      int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0); 
      if (0 != notifyId) {
          NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
          manager.cancel(notifyId); 
      }

      String message = extras.getString(BOUND_KEY.pushMsgKey);  
      NXTReceiver.pushLog("HuaWeiReceiver.onEvent: " + message);
      JSRunner.onNotificationMessageClicked(message);
      super.onEvent(context, event, extras);
    }
  }

}
