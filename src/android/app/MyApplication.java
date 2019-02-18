package com.nxt;

import android.app.Application;

import com.huawei.android.hms.agent.HMSAgent;
import com.nxt.push.util.RomTypeUtil;

import com.nxt.push.sdk.NXTPushManager;
import com.nxt.push.receiver.NXTReceiver;

/**
 * application类 | application class
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (RomTypeUtil.isEMUI()) {
            NXTPushManager.isAgentInitOk = HMSAgent.init(this);
            NXTReceiver.pushLog("MyApplication.onCreate --> " + NXTPushManager.isAgentInitOk);
        }
        else if(RomTypeUtil.isMIUI()){
            NXTPushManager.init(null, this);
            NXTReceiver.pushLog("MyApplication.onCreate --> init MiPush");
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (RomTypeUtil.isEMUI()) {
            HMSAgent.destroy();
        }
    }
}
