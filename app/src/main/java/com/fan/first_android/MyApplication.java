package com.fan.first_android;

import com.fan.library.app.App;
import com.umeng.commonsdk.UMConfigure;

public class MyApplication extends App {

    @Override
    public void onCreate() {
        super.onCreate();

        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"");


    }
}
