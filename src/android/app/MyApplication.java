package com.xmtx.test;

import android.app.Application;

import com.huawei.android.hms.agent.HMSAgent;

import com.nxt.push.sdk.NXTPushManager;
import com.nxt.push.receiver.NXTReceiver;

/**
 * application类 | application class
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NXTPushManager.isAgentInitOk = HMSAgent.init(this);
        NXTReceiver.pushLog("MyApplication.onCreate --> " + NXTPushManager.isAgentInitOk);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        HMSAgent.destroy();
    }
}
