package com.nxt.push.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.nxt.push.receiver.NXTReceiver;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * 小米推送初始化以及别名管理
 */
public class XiaomiPushClient implements NXTPushClient {

  // 清单文件中配置小米推送APPID的key
  private static final String MI_PUSH_APPID = "MI_PUSH_APPID";
  // 清单文件中配置小米推送APPKEY的key
  private static final String MI_PUSH_APPKEY = "MI_PUSH_APPKEY";

  @Override
  public boolean init(Activity activity){
    return true;
  }

  @Override
  public void registerPush(Context ctx) {
    try {
      ApplicationInfo applicationInfo = ctx.getPackageManager()
        .getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
      if (applicationInfo != null && applicationInfo.metaData != null) {
        String appid = applicationInfo.metaData.getString(MI_PUSH_APPID);
        String appkey = applicationInfo.metaData.getString(MI_PUSH_APPKEY);
        if (!TextUtils.isEmpty(appid) && !TextUtils.isEmpty(appkey)) {
          MiPushClient.registerPush(ctx, appid, appkey);
        } else {
          NXTReceiver.pushLog("AppId & Key empty...");
        }
      }
    } catch (Exception e) {
      NXTReceiver.pushLog(e.getMessage());
    }
  }

  @Override
  public void unRegisterPush(Context context) {
    MiPushClient.unregisterPush(context);

    SharedPreferences sharedPreference =
      context.getSharedPreferences(NXTReceiver.JINGOAL_PUSH_SP, Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = sharedPreference.edit();
    edit.remove(NXTReceiver.SP_KEY_XIAOMI_TOKEN);
    edit.commit();
  }

  @Override
  public void stopPush(Context ctx) {
  }

  @Override
  public void resumePush(Context ctx) {
  }

  @Override
  public void setAlias(Context context, String deviceId, String alias) {
    MiPushClient.setAlias(context, alias, null);
  }

  @Override
  public void deleteAlias(Context context, String deviceId, String alias) {
    MiPushClient.unsetAlias(context, alias, null);
  }

  @Override
  public String getToken(Context context) {
    SharedPreferences sharedPreference =
      context.getSharedPreferences(NXTReceiver.JINGOAL_PUSH_SP, Context.MODE_PRIVATE);
    return sharedPreference.getString(NXTReceiver.SP_KEY_XIAOMI_TOKEN, null);
  }

  @Override
  public int getClientType() {
    return NXTReceiver.PushClientType.XIAO_MI;
  }
}
