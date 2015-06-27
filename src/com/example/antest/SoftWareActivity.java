package com.example.antest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logic.AppInfo;
import com.example.logic.AppInfoParser;

public class SoftWareActivity extends Activity implements OnClickListener {

    private TextView              sdCard, tv_rom;
    private ListView              listView;
    private List<AppInfo>         appInfos;
    private List<AppInfo> mUserList;
    private List<AppInfo> mSystemList;
    private PopupWindow   popupwindow;
    private AppInfo clickAppInfo;
    private UninstallReceiver recriver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 初始化画面
        initiew();
        // 初始化数据
        initData();
    }

    /**
     * 初始化应用数据
     */
    private void initData() {
        // 所有耗时操作放到子线程
        new Thread() {

            public void run() {
                // 获取手机上安转的程序信息
                appInfos = AppInfoParser.getAppInfos(SoftWareActivity.this);

                mUserList = new ArrayList<AppInfo>();
                mSystemList = new ArrayList<AppInfo>();

                for (AppInfo info : appInfos) {
                    if (info.isAppFlg()) {
                        mUserList.add(info);
                    } else {
                        mSystemList.add(info);
                    }
                }

                // 运行到主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        SoftWareAdapter adapter = new SoftWareAdapter();
                        listView.setAdapter(adapter);
                    }

                });
            };
        }.start();
    }

    private class SoftWareAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // 返回ListView条目个数
            // return appInfos.size();
            // +2 用户程序和系统程序标题
            return mUserList.size() + mSystemList.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == mUserList.size() + 1) {
                return null;
            }
            AppInfo appInfo;
            if (position < mUserList.size() + 1) {
                // 用户程序
                appInfo = mUserList.get(position - 1);
            } else {
                // 系统程序
                int location = mSystemList.size() - mUserList.size() - 2;
                appInfo = mSystemList.get(location);
            }
            return appInfo;
            // return appInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 当前position为0的时候，特殊条目：用户程序
            if (position == 0) {
                TextView textView = new TextView(SoftWareActivity.this);
                // 设置字体颜色白色
                textView.setTextColor(Color.WHITE);
                // 设置背景颜色黑色
                textView.setBackgroundColor(Color.BLACK);
                // 设置文本内容
                textView.setText("用户程序(" + mUserList.size() + ")");
                return textView;
            } else if (position == mUserList.size() + 1) {
                TextView textView = new TextView(SoftWareActivity.this);
                // 设置字体颜色白色
                textView.setTextColor(Color.WHITE);
                // 设置背景颜色黑色
                textView.setBackgroundColor(Color.BLACK);
                // 设置文本内容
                textView.setText("系统程序(" + mSystemList.size() + ")");
                return textView;
            }

            //
            ViewHolder holder = null;
            if (convertView != null && convertView instanceof LinearLayout) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
                convertView = View.inflate(SoftWareActivity.this,
                        R.layout.item_software, null);
                // 应用名字
                holder.tv_app_name = (TextView) convertView
                        .findViewById(R.id.tv_app_name);
                // 应用大小
                holder.tv_app_size = (TextView) convertView
                        .findViewById(R.id.tv_app_size);
                // 应用图标
                holder.iv_icon = (ImageView) convertView
                        .findViewById(R.id.iv_icon);
                // 应用安装位置
                holder.tv_location = (TextView) convertView
                        .findViewById(R.id.tv_location);
                convertView.setTag(holder);
            }

            // 根据当前位置获得当前对象
            // AppInfo appInfo = appInfos.get(position);
            AppInfo appInfo;
            if (position < mUserList.size() + 1) {
                // 用户程序
                appInfo = mUserList.get(position - 1);
            } else {
                // 系统程序
                int location = mSystemList.size() - mUserList.size() - 2;
                appInfo = mSystemList.get(location);
            }

            holder.tv_app_name.setText(appInfo.getAppName());
            holder.tv_app_size.setText(Formatter.formatFileSize(
                    SoftWareActivity.this, appInfo.getAppSize()));
            holder.iv_icon.setImageDrawable(appInfo.getIcon());
            return convertView;
        }
    }

    static class ViewHolder {
        // 应用名字
        TextView  tv_app_name;
        // 应用大小
        TextView  tv_app_size;
        // 应用图标
        ImageView iv_icon;
        // 应用安装位置
        TextView  tv_location;
    }

    private void initiew() {
        // TODO Auto-generated method stub
        setContentView(R.layout.software_activity);
        // 初始化内存
        tv_rom = (TextView) findViewById(R.id.rom);
        // 初始化SD卡
        sdCard = (TextView) findViewById(R.id.sdCard);
        // 初始化软件列表
        listView = (ListView) findViewById(R.id.listView);
        // 获取数据目录剩余存储空间
        long romFreeSpace = Environment.getDataDirectory().getFreeSpace();
        // 获取SD卡剩余存储空
        long sdFreeSpace = Environment.getExternalStorageDirectory()
                .getFreeSpace();
        // 格式化数据
        tv_rom.setText("内存可用：" + Formatter.formatFileSize(this, romFreeSpace));
        //
        sdCard.setText("SD卡可用：" + Formatter.formatFileSize(this, sdFreeSpace));
        // 滚动监听事件
        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
                // 让popwindow消失
                dissmissPopWindow(popupwindow);
            }

        });
        // 点击监听事件
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // 获取当前点击对象
                clickAppInfo = (AppInfo) listView
                        .getItemAtPosition(position);
                // 判断当前对象是否为空
                if (null != clickAppInfo) {
                    // 把XML转化为view
                    View contentView = View.inflate(SoftWareActivity.this,
                            R.layout.popup_item, null);
                    // 分享
                    TextView ll_share = (TextView) contentView
                            .findViewById(R.id.ll_share);
                    // 运行
                    TextView ll_start = (TextView) contentView
                            .findViewById(R.id.ll_start);
                    // 卸载
                    TextView ll_uninstall = (TextView) contentView
                            .findViewById(R.id.ll_uninstall);

                    ll_share.setOnClickListener(SoftWareActivity.this);
                    ll_start.setOnClickListener(SoftWareActivity.this);
                    ll_uninstall.setOnClickListener(SoftWareActivity.this);

                    // 让popwindow消失
                    dissmissPopWindow(popupwindow);

                    /**
                     * 第一个参数：展示的View 第二个参数：展示view的宽 第三个参数：展示view的高
                     */
                    popupwindow = new PopupWindow(contentView, -2, -2);

                    // 包含两个数的数组
                    int[] location = new int[2];
                    // 获取当前view在窗体上的位置
                    view.getLocationInWindow(location);

                    // Gravity.LEFT + Gravity.TOP 左上角
                    popupwindow.showAtLocation(parent, Gravity.LEFT
                            + Gravity.TOP, 60, location[1]);

                }
            }
        });
        
        recriver = new UninstallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(recriver, filter);
    }

    /**
     * 消除已经战士的对话框
     * 
     * @param popupwindow
     */
    private void dissmissPopWindow(PopupWindow popupwindow) {
        // TODO Auto-generated method stub
        if (null != popupwindow && popupwindow.isShowing()) {
            popupwindow.dismiss();
            unregisterReceiver(recriver);
        }
    }

    /**
     * PopWindow项目点击事件
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.ll_start:
            startApplication();
            break;
        case R.id.ll_share:
            shareApplication();
            break;
        case R.id.ll_uninstall:
            unStallApplication();
            break;
        }
    }

    /**
     * 卸载应用程序
     */
    private void unStallApplication() {
        // 
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + clickAppInfo.getAppPackage()));
        
    }

    /**
     * 启动相应的应用程序
     */
    private void startApplication() {
        Intent intent = new Intent();
        //
        PackageManager packageManager = this.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(clickAppInfo.getAppPackage());
        //
        if (null != intent) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "无法启动该应用程序！", 0);
        }
    }

    /**
     * 分享应用给好友
     */
    private void shareApplication() {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "推荐你使用一款应用");
        startActivity(intent);
    }
    
    private class UninstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取卸载程序信息
            String src = intent.getData().toString();
            Toast.makeText(SoftWareActivity.this, "卸载的应用包是：" + src, 0);
            //刷新界面，将已卸载程序移除
            initData();
            
        }
    
    }

}
