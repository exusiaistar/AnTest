package com.example.logic;

import android.graphics.drawable.Drawable;

public class AppInfo {

    /**
     * 图标
     */
    private Drawable icon;

    /**
     * 应用程序名字
     */
    private String   appName;

    /**
     * 应用程序包名
     */
    private String   appPackage;

    /**
     * 应用程序大小
     */
    private long     appSize;
    
    
    /**
     * 应用程序分类
     * true:用户程序
     * false:系统程序
     */
    private boolean appFlg;
    
    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    public boolean isAppFlg() {
        return appFlg;
    }

    public void setAppFlg(boolean appFlg) {
        this.appFlg = appFlg;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    /**
     * true:手机内存
     * false:SD卡
     */
    private boolean inRom;
}
