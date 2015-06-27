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
        // ��ʼ������
        initiew();
        // ��ʼ������
        initData();
    }

    /**
     * ��ʼ��Ӧ������
     */
    private void initData() {
        // ���к�ʱ�����ŵ����߳�
        new Thread() {

            public void run() {
                // ��ȡ�ֻ��ϰ�ת�ĳ�����Ϣ
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

                // ���е����߳�
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
            // ����ListView��Ŀ����
            // return appInfos.size();
            // +2 �û������ϵͳ�������
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
                // �û�����
                appInfo = mUserList.get(position - 1);
            } else {
                // ϵͳ����
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

            // ��ǰpositionΪ0��ʱ��������Ŀ���û�����
            if (position == 0) {
                TextView textView = new TextView(SoftWareActivity.this);
                // ����������ɫ��ɫ
                textView.setTextColor(Color.WHITE);
                // ���ñ�����ɫ��ɫ
                textView.setBackgroundColor(Color.BLACK);
                // �����ı�����
                textView.setText("�û�����(" + mUserList.size() + ")");
                return textView;
            } else if (position == mUserList.size() + 1) {
                TextView textView = new TextView(SoftWareActivity.this);
                // ����������ɫ��ɫ
                textView.setTextColor(Color.WHITE);
                // ���ñ�����ɫ��ɫ
                textView.setBackgroundColor(Color.BLACK);
                // �����ı�����
                textView.setText("ϵͳ����(" + mSystemList.size() + ")");
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
                // Ӧ������
                holder.tv_app_name = (TextView) convertView
                        .findViewById(R.id.tv_app_name);
                // Ӧ�ô�С
                holder.tv_app_size = (TextView) convertView
                        .findViewById(R.id.tv_app_size);
                // Ӧ��ͼ��
                holder.iv_icon = (ImageView) convertView
                        .findViewById(R.id.iv_icon);
                // Ӧ�ð�װλ��
                holder.tv_location = (TextView) convertView
                        .findViewById(R.id.tv_location);
                convertView.setTag(holder);
            }

            // ���ݵ�ǰλ�û�õ�ǰ����
            // AppInfo appInfo = appInfos.get(position);
            AppInfo appInfo;
            if (position < mUserList.size() + 1) {
                // �û�����
                appInfo = mUserList.get(position - 1);
            } else {
                // ϵͳ����
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
        // Ӧ������
        TextView  tv_app_name;
        // Ӧ�ô�С
        TextView  tv_app_size;
        // Ӧ��ͼ��
        ImageView iv_icon;
        // Ӧ�ð�װλ��
        TextView  tv_location;
    }

    private void initiew() {
        // TODO Auto-generated method stub
        setContentView(R.layout.software_activity);
        // ��ʼ���ڴ�
        tv_rom = (TextView) findViewById(R.id.rom);
        // ��ʼ��SD��
        sdCard = (TextView) findViewById(R.id.sdCard);
        // ��ʼ������б�
        listView = (ListView) findViewById(R.id.listView);
        // ��ȡ����Ŀ¼ʣ��洢�ռ�
        long romFreeSpace = Environment.getDataDirectory().getFreeSpace();
        // ��ȡSD��ʣ��洢��
        long sdFreeSpace = Environment.getExternalStorageDirectory()
                .getFreeSpace();
        // ��ʽ������
        tv_rom.setText("�ڴ���ã�" + Formatter.formatFileSize(this, romFreeSpace));
        //
        sdCard.setText("SD�����ã�" + Formatter.formatFileSize(this, sdFreeSpace));
        // ���������¼�
        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
                // ��popwindow��ʧ
                dissmissPopWindow(popupwindow);
            }

        });
        // ��������¼�
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // ��ȡ��ǰ�������
                clickAppInfo = (AppInfo) listView
                        .getItemAtPosition(position);
                // �жϵ�ǰ�����Ƿ�Ϊ��
                if (null != clickAppInfo) {
                    // ��XMLת��Ϊview
                    View contentView = View.inflate(SoftWareActivity.this,
                            R.layout.popup_item, null);
                    // ����
                    TextView ll_share = (TextView) contentView
                            .findViewById(R.id.ll_share);
                    // ����
                    TextView ll_start = (TextView) contentView
                            .findViewById(R.id.ll_start);
                    // ж��
                    TextView ll_uninstall = (TextView) contentView
                            .findViewById(R.id.ll_uninstall);

                    ll_share.setOnClickListener(SoftWareActivity.this);
                    ll_start.setOnClickListener(SoftWareActivity.this);
                    ll_uninstall.setOnClickListener(SoftWareActivity.this);

                    // ��popwindow��ʧ
                    dissmissPopWindow(popupwindow);

                    /**
                     * ��һ��������չʾ��View �ڶ���������չʾview�Ŀ� ������������չʾview�ĸ�
                     */
                    popupwindow = new PopupWindow(contentView, -2, -2);

                    // ����������������
                    int[] location = new int[2];
                    // ��ȡ��ǰview�ڴ����ϵ�λ��
                    view.getLocationInWindow(location);

                    // Gravity.LEFT + Gravity.TOP ���Ͻ�
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
     * �����Ѿ�սʿ�ĶԻ���
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
     * PopWindow��Ŀ����¼�
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
     * ж��Ӧ�ó���
     */
    private void unStallApplication() {
        // 
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + clickAppInfo.getAppPackage()));
        
    }

    /**
     * ������Ӧ��Ӧ�ó���
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
            Toast.makeText(this, "�޷�������Ӧ�ó���", 0);
        }
    }

    /**
     * ����Ӧ�ø�����
     */
    private void shareApplication() {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "�Ƽ���ʹ��һ��Ӧ��");
        startActivity(intent);
    }
    
    private class UninstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // ��ȡж�س�����Ϣ
            String src = intent.getData().toString();
            Toast.makeText(SoftWareActivity.this, "ж�ص�Ӧ�ð��ǣ�" + src, 0);
            //ˢ�½��棬����ж�س����Ƴ�
            initData();
            
        }
    
    }

}
