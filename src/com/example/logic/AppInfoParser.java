package com.example.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppInfoParser {

    public static List<AppInfo> getAppInfos(Context context) {
        
        PackageManager packagemanager = context.getPackageManager();
        
        List<AppInfo> myAppInfosList = new ArrayList<AppInfo>();
      //获取手机安装的应用
        List<PackageInfo> packages = packagemanager.getInstalledPackages(0);
        //迭代所有的应用程序
        for (PackageInfo packageinfo: packages) {
            //驰石化基本信息对象
            AppInfo appInfo = new AppInfo();
            //获取引用程序包名
            String packageName = packageinfo.packageName;
            appInfo.setAppPackage(packageName);
            //获取应用程序图标
            Drawable icon = packageinfo.applicationInfo.loadIcon(packagemanager);
            appInfo.setIcon(icon);
            //获取应用程序名字
            String appName = packageinfo.applicationInfo.loadLabel(packagemanager).toString();
            appInfo.setAppName(appName);
            //获取应用程序目录
            String sourceDir = packageinfo.applicationInfo.sourceDir;
            
            File file = new File(sourceDir);
            //获取应用大小
            long length = file.length();
            appInfo.setAppSize(length);
            
            //获取应用程序标记
            int flags = packageinfo.applicationInfo.flags;
            if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
                //系统应用
                appInfo.setAppFlg(false);
            } else {
                //用户应用
                appInfo.setAppFlg(true);
            }
            
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
                //SD卡
                appInfo.setInRom(false);
            } else {
                //机器内存
                appInfo.setInRom(true);
            }
//            if (sourceDir.startsWith("/system")) {
//            //系统应用
//            } else {
//            //用户应用
//            }
            myAppInfosList.add(appInfo);
        }
        
        return myAppInfosList;
    }
}
