package com.fan.library.app;

import android.app.Activity;
import android.app.Application;

import java.util.Locale;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.LogUtils;
import me.jessyan.autosize.utils.ScreenUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initScreen();

    }

    private void initScreen() {
        //当 App 中出现多进程, 适配所有的进程调用
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance()
                .setCustomFragment(false)//是否让框架支持自定义 Fragment 的适配参数, 默认false不支持.
                .setExcludeFontScale(false)//是否屏蔽系统字体大小对 AndroidAutoSize 的影响，默认false，App内的字体大小将会跟随系统设置中字体大小而改变
                .setOnAdaptListener(new onAdaptListener() {//屏幕适配监听器
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                        //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                        LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName()));
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName()));
                    }
                })
                .setLog(false)//是否打印AutoSize内部日志, 默认true
                .setUseDeviceSize(true)//是否使用设备实际尺寸做适配, 默认false,即在以屏幕高度为基准进行适配时,AutoSize 会将屏幕总高度减去状态栏高度来做适配;设置为true则使用设备的实际屏幕高度,不会减去状态栏高度
                .setBaseOnWidth(true)//是否全局按照宽度进行等比例适配, 默认true;如果设置为false, AutoSize 会全局按照高度进行适配
//                .setAutoAdaptStrategy(new AutoAdaptStrategy())//设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
        ;
        customAdaptForExternal();
    }

    /**
     * 给外部的三方库 {@link Activity} 自定义适配参数
     * 使用ExternalAdaptManager 来替代实现接口的方式, 来提供自定义适配参数
     */
    private void customAdaptForExternal() {
        //ExternalAdaptManager 是一个管理外部三方库的适配信息和状态的管理类, 详细介绍请看 ExternalAdaptManager 的类注释
        AutoSizeConfig.getInstance().getExternalAdaptManager()
//                .addExternalAdaptInfoOfActivity(DefaultErrorActivity.class, new ExternalAdaptInfo(true, 400))//对三方库指定Activity页面进行适配，设计图宽度在 380dp - 400dp 显示效果都还好
//                .addCancelAdaptOfActivity(DefaultErrorActivity.class)//加入的 Activity 将会放弃屏幕适配, 一般用于三方库的 Activity
        ;
    }
}
