package com.example.logic;

import android.graphics.drawable.Drawable;

public class AppInfo {

    /**
     * ͼ��
     */
    private Drawable icon;

    /**
     * Ӧ�ó�������
     */
    private String   appName;

    /**
     * Ӧ�ó������
     */
    private String   appPackage;

    /**
     * Ӧ�ó����С
     */
    private long     appSize;
    
    
    /**
     * Ӧ�ó������
     * true:�û�����
     * false:ϵͳ����
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
     * true:�ֻ��ڴ�
     * false:SD��
     */
    private boolean inRom;
}
