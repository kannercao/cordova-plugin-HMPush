package com.eegrid.phonegap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nxt.push.receiver.NXTReceiver;
import com.nxt.push.util.RomTypeUtil;
import com.nxt.push.sdk.NXTPushManager;
import com.nxt.push.sdk.NXTPushClient;

public class HMPushPlugin extends CordovaPlugin {

  private static final String TAG = "HMPushPlugin";

  private Context mContext;

  private static String jsCode = null;
  private static HMPushPlugin instance;
  private static Activity cordovaActivity;

  public HMPushPlugin() {
    instance = this;
  }

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    NXTReceiver.pushLog("NXTPush initialize...");
    super.initialize(cordova, webView);
    mContext = cordova.getActivity().getApplicationContext();
    cordovaActivity = cordova.getActivity();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    cordovaActivity = null;
    instance = null;
  }

  @Override
  public boolean execute(final String action, final JSONArray data, final CallbackContext callbackContext)
      throws JSONException {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        try {
          Method method = HMPushPlugin.class.getDeclaredMethod(action, JSONArray.class, CallbackContext.class);
          method.invoke(HMPushPlugin.this, data, callbackContext);
        } catch (Exception e) {
          NXTReceiver.pushLog(e.toString());
        }
      }
    });

    return true;
  }

  void init(JSONArray data, CallbackContext callbackContext) {
    if (RomTypeUtil.isEMUI() || RomTypeUtil.isMIUI()) {
      if (RomTypeUtil.isEMUI()) {
        NXTPushManager.init(cordovaActivity, mContext);
      }
      NXTReceiver.pushLog("NXTPush.init -> do last jscode: " + (jsCode == null ? "null" : jsCode));
      if (jsCode != null) {
        HMPushPlugin.runJSOnUiThread(jsCode, false);
        jsCode = null;
      }
      callbackContext.success(RomTypeUtil.isEMUI() ? "HW" : "MI");
    } else {
      callbackContext.success("OTHER");
    }
  }

  void setTags(JSONArray data, CallbackContext callbackContext) {
  }

  void setAlias(JSONArray data, CallbackContext callbackContext) {
    try {
      // 注册华为与小米
      NXTPushManager.setAlias(mContext, "USELESS", data.getString(0));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  void deleteAlias(JSONArray data, CallbackContext callbackContext) {
    try {
      // 注册华为与小米
      NXTPushManager.deleteAlias(mContext, "USELESS", data.getString(0));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * 极光专有方法开始
   */
  void setDebugMode(JSONArray data, CallbackContext callbackContext) {
    boolean mode;
    try {
      mode = data.getBoolean(0);
      NXTReceiver.gsIsDebug = mode;
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  void stopPush(JSONArray data, CallbackContext callbackContext) {
    NXTPushManager.stopPush(mContext);
  }

  void resumePush(JSONArray data, CallbackContext callbackContext) {
    NXTPushManager.resumePush(mContext);
  }

  // 借这个接口获取插件日志
  boolean isPushStopped(JSONArray data, CallbackContext callbackContext) {
    callbackContext.success(false);
    return false;
  }

  void getPushLogs(JSONArray data, CallbackContext callbackContext) {
    String logs = NXTPushManager.getPluginLog();
    callbackContext.success(logs);
  }

  void getRegistrationID(JSONArray data, CallbackContext callbackContext) {
    if (RomTypeUtil.isEMUI() || RomTypeUtil.isMIUI()) {
      String hwToken = NXTPushManager.getToken(mContext);
      callbackContext.success(hwToken);
    } else {
      callbackContext.success("not support phone.");
    }
  }

  void unRegisterPush(JSONArray data, CallbackContext callbackContext) {
    NXTPushManager.unRegisterPush(mContext);
  }



  /**
   * 用于 Android 6.0 以上系统申请权限，具体可参考：
   * http://docs.Push.io/client/android_api/#android-60
   */
  void requestPermission(JSONArray data, CallbackContext callbackContext) {

  }

  /** 极光专有方法结束 */

  /**
   * 专门用来执行来自 JPush 的执行请求
   */
  public static void runJSOnUiThread(final String js, boolean isNeedCatch) {
    if (instance == null) {
      if (isNeedCatch) {
        jsCode = js;
      }
      return;
    }
    cordovaActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        instance.webView.loadUrl("javascript:" + js);
      }
    });
  }

  public static Activity getActivity() {
    return cordovaActivity;
  }

  /**
   * 判断是否开启了通知权限
   */
  void areNotificationEnabled(JSONArray data, final CallbackContext callback) {
    int isEnabled;
    if (hasPermission("OP_POST_NOTIFICATION")) {
      isEnabled = 1;
    } else {
      isEnabled = 0;
    }
    callback.success(isEnabled);
  }

  private boolean hasPermission(String appOpsServiceId) {
    Context context = cordova.getActivity().getApplicationContext();
    if (Build.VERSION.SDK_INT >= 24) {
      NotificationManager mNotificationManager = (NotificationManager) context
          .getSystemService(Context.NOTIFICATION_SERVICE);
      return mNotificationManager.areNotificationsEnabled();
    } else {
      AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
      ApplicationInfo appInfo = context.getApplicationInfo();

      String pkg = context.getPackageName();
      int uid = appInfo.uid;
      Class appOpsClazz;

      try {
        appOpsClazz = Class.forName(AppOpsManager.class.getName());
        Method checkOpNoThrowMethod = appOpsClazz.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
        Field opValue = appOpsClazz.getDeclaredField(appOpsServiceId);
        int value = opValue.getInt(Integer.class);
        Object result = checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg);
        return Integer.parseInt(result.toString()) == AppOpsManager.MODE_ALLOWED;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return false;
  }
}
