package com.example.framework;
//Framework入口

import android.content.Context;

import com.example.framework.bmob.BmobManager;
import com.example.framework.cloud.CloudManager;
import com.example.framework.helper.GlideHelper;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.example.framework.utils.ToastUtil;

import org.litepal.LitePal;

//创建Framework Molde隔离工具类 和逻辑代码
public class Framework {
    //单例封装通用工具

    private volatile static Framework mFramework;

    public Framework() {
    }

    //双重校验锁单例
    public static Framework getFramework(){
        if(mFramework == null){
            synchronized (Framework.class){
                if(mFramework == null){
                    mFramework = new Framework();
                }
            }
        }
        return mFramework;
    }

    /**
     * 初始化框架 Model
     *
     * @param mContext
     */
    public void initFramework(Context mContext) {
        LogUtils.i("initFramework");
        SpUtils.getInstance().initSp(mContext);
       BmobManager.getInstance().initBmob(mContext);
        ToastUtil.initToast(mContext);
        CloudManager.getInstance().initCloud(mContext);
        LitePal.initialize(mContext);
       /* MapManager.getInstance().initMap(mContext);
        WindowHelper.getInstance().initWindow(mContext);
        CrashReport.initCrashReport(mContext, BUGLY_KEY, BuildConfig.LOG_DEBUG);
        ZXingLibrary.initDisplayOpinion(mContext);
        NotificationHelper.getInstance().createChannel(mContext);
        KeyWordManager.getInstance().initManager(mContext);*/

/*        //全局捕获RxJava异常
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e("RxJava：" + throwable.toString());
            }
        });*/
    }
}
