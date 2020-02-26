package com.example.framework;
//Framework入口

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
}
