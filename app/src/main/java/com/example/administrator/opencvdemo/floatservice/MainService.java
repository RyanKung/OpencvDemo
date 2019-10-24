package com.example.administrator.opencvdemo.floatservice;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.R;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.HandlerUtil;
import com.example.administrator.opencvdemo.util.LaunchApp;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.module_orc.OpenCVHelper;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;

import java.util.List;

import static com.example.administrator.opencvdemo.util.LaunchApp.SELF_APP;

public class MainService extends Service {

    private static final String TAG = "MainService";

    private ScreenBroadcastReceiver mScreenBroadcastReceiver;
    //状态栏高度.
    private GameFloatView gameFloatView;

    //不与Activity进行绑定.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerUtil.send("启动服务");
        createTouchView();
        BaseApplication.setIsShowPanel(true);
        startScreenBroadcastReceiver();
        OpenCVHelper.init(this);
    }

    private void createTouchView() {
        gameFloatView = new GameFloatView(this);
        gameFloatView.showFloatView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("action")) {
            String action = intent.getStringExtra("action");
            if ("ACTION_BOOT_COMPLETED".equals(action) || "com.g.android.RING".equals(action)) {
                gameFloatView.hidePanel();
                // xiaoHao(true);
            }
        }
        return START_STICKY;
    }
    //
    // private void xiaoHao(boolean some) {
    //     Util.isWPZMGServiceRunning = !Util.isWPZMGServiceRunning;
    //     Intent intent2 = new Intent(MainService.this, WPZMGService2.class);
    //     if (!Util.isWPZMGServiceRunning) {
    //         intent2.putExtra("stop", true);
    //         Util.setResLastTime(0);
    //     }
    //     startService(intent2);
    // }

    @Override
    public void onDestroy() {
        if (gameFloatView != null) {
            gameFloatView.hideFloatView();
        }

        Log.d(TAG, "onDestroy: ");
        if (mScreenBroadcastReceiver != null) {
            unregisterReceiver(mScreenBroadcastReceiver);
            mScreenBroadcastReceiver = null;
        }
        super.onDestroy();
        BaseApplication.setIsShowPanel(false);
    }

    public static void start(Activity context) {
        Intent intent = new Intent(context, MainService.class);
        context.stopService(intent);
        context.startService(intent);
        context.moveTaskToBack(true);
    }

    private void startScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenBroadcastReceiver, filter);
    }
}