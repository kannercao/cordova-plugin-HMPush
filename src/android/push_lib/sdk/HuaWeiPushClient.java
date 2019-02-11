package com.nxt.push.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.HMSAgent;
import com.nxt.push.receiver.NXTReceiver;

/**
 * 华为推送初始化以及别名管理
 */
public class HuaWeiPushClient implements NXTPushClient {

  @Override
  public boolean init(Activity activity){
    // if(HMSAgent.init(activity)){
      HMSAgent.connect(activity, new ConnectHandler(){
        @Override
        public void onConnect(int rst){
          NXTReceiver.pushLog("HuaWeiPushClient.init -> onConnect: " + rst);
        }
      });
      return true;
    // }else{
      // return false;
    // }
  }

  @Override
  public void registerPush(Context ctx) {
    HMSAgent.Push.getToken(null);
  }

  @Override
  public void unRegisterPush(Context context) {
    String token = this.getToken(context);
    if(token != null){
      HMSAgent.Push.deleteToken(token, null);
    }

    SharedPreferences sharedPreference =
      context.getSharedPreferences(NXTReceiver.JINGOAL_PUSH_SP, Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = sharedPreference.edit();
    edit.remove(NXTReceiver.SP_KEY_HUAWEI_TOKEN);
    edit.commit();
  }

  @Override
  public void stopPush(Context ctx) {
    HMSAgent.Push.enableReceiveNotifyMsg(false, null);
    HMSAgent.Push.enableReceiveNormalMsg(false, null);
  }
 
  @Override
  public void resumePush(Context ctx) {
    HMSAgent.Push.enableReceiveNotifyMsg(true, null);
    HMSAgent.Push.enableReceiveNormalMsg(true, null);
  } 


  @Override
  public void setAlias(Context context, String deviceId, String alias) {
    //    to do nothing 华为不支持别名 
  }

  @Override
  public void deleteAlias(Context context, String deviceId, String alias) {
    //    to do nothing 华为不支持别名
  }

  @Override
  public String getToken(Context context) {
    SharedPreferences sharedPreference =
      context.getSharedPreferences(NXTReceiver.JINGOAL_PUSH_SP, Context.MODE_PRIVATE);
    return sharedPreference.getString(NXTReceiver.SP_KEY_HUAWEI_TOKEN, null);
  }

  @Override
  public int getClientType() {
    return NXTReceiver.PushClientType.HUA_WEI;
  }
}
