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
      //��ȡ�ֻ���װ��Ӧ��
        List<PackageInfo> packages = packagemanager.getInstalledPackages(0);
        //�������е�Ӧ�ó���
        for (PackageInfo packageinfo: packages) {
            //��ʯ��������Ϣ����
            AppInfo appInfo = new AppInfo();
            //��ȡ���ó������
            String packageName = packageinfo.packageName;
            appInfo.setAppPackage(packageName);
            //��ȡӦ�ó���ͼ��
            Drawable icon = packageinfo.applicationInfo.loadIcon(packagemanager);
            appInfo.setIcon(icon);
            //��ȡӦ�ó�������
            String appName = packageinfo.applicationInfo.loadLabel(packagemanager).toString();
            appInfo.setAppName(appName);
            //��ȡӦ�ó���Ŀ¼
            String sourceDir = packageinfo.applicationInfo.sourceDir;
            
            File file = new File(sourceDir);
            //��ȡӦ�ô�С
            long length = file.length();
            appInfo.setAppSize(length);
            
            //��ȡӦ�ó�����
            int flags = packageinfo.applicationInfo.flags;
            if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
                //ϵͳӦ��
                appInfo.setAppFlg(false);
            } else {
                //�û�Ӧ��
                appInfo.setAppFlg(true);
            }
            
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
                //SD��
                appInfo.setInRom(false);
            } else {
                //�����ڴ�
                appInfo.setInRom(true);
            }
//            if (sourceDir.startsWith("/system")) {
//            //ϵͳӦ��
//            } else {
//            //�û�Ӧ��
//            }
            myAppInfosList.add(appInfo);
        }
        
        return myAppInfosList;
    }
}
