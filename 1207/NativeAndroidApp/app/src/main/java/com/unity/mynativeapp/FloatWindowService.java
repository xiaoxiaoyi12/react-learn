package com.unity.mynativeapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.MotionEvent;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import com.unity3d.player.UnityPlayer;


public class FloatWindowService extends Service {
    private static final String TAG = "FloatWindowService";
//    private Handler handler = new Handler();

    protected UnityPlayer mUnityPlayer;

    int statusBarHeight = -1;
//    public FloatWindowService() {
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "FloatWindowService Created");
        //OnCreate中来生成悬浮窗.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showFloatingView();
        }
    }

    /**
     * 悬浮窗控件可以是任意的View的子类类型
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFloatingView() {
        if (Settings.canDrawOverlays(getApplicationContext())) {
            //WindowManager 对象
            WindowManager manager = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);

//            //新建悬浮控件
//            Button button = new Button(this);
//            button.setText("Floating Window");
//            button.setBackgroundColor(Color.BLUE);

            //设置layoutParams
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            params.format = PixelFormat.RGBA_8888;
//            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //设置不阻挡其他view的触摸事件
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.gravity = Gravity.RIGHT | Gravity.TOP;
            params.x = 0;
            params.y = 0;
            params.width=600;
            params.height=800;

            LayoutInflater inflater = LayoutInflater.from(getApplication());
            LinearLayout toucherLayout=(LinearLayout) inflater.inflate(R.layout.float_window, null);
            //添加view到windowManager
            manager.addView(toucherLayout, params);

//        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//            if (resourceId > 0) {
//                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//            }
//            Log.i(TAG, "状态栏高度为:" + statusBarHeight);

            mUnityPlayer= new UnityPlayer(this.getApplicationContext());
//            mUnityPlayer = getApplication().getUnityPlayer();
            ((RelativeLayout) toucherLayout.findViewById(R.id.rl_pet)).addView(mUnityPlayer);
            mUnityPlayer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //ImageButton我放在了布局中心，布局一共300dp
                    params.x = (int) event.getRawX() - 150;
                    //这就是状态栏偏移量用的地方
                    params.y = (int) event.getRawY() - 150 - statusBarHeight;
                    manager.updateViewLayout(toucherLayout,params);
                    return false;
                }
            });

//            mUnityPlayer.startOrientationListener(1);
//            mUnityPlayer.resume();
            //触摸事件
//            displayView.setOnTouchListener(new OnFloatingButtonTouchListener());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUnityPlayer.windowFocusChanged(true);  // 不能写
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mUnityPlayer.pause();
//        mUnityPlayer.onUnityPlayerQuitted();
        mUnityPlayer.destroy();
        super.onDestroy();

    }
    private void createWindowView() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        //设置view
        Button button = new Button(this);
        button.setText("Floating Window");
        button.setBackgroundColor(Color.BLUE);
        // 设置LayoutParams
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSPARENT);

        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        layoutParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

//        layoutParams.gravity= Gravity.LEFT|Gravity.TOP;
        layoutParams.width=500;
        layoutParams.height=300;
        layoutParams.x=300;
        layoutParams.y=300;

        windowManager.addView(button,layoutParams);

//        Button btnView = new Button(getApplicationContext());
//        btnView.setBackgroundResource(R.mipmap.ic_launcher);
//        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//
//        // 设置Window Type
//        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        // 设置悬浮框不可触摸
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        // 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应
////        params.format = PixelFormat.RGBA_8888;
//        // 设置悬浮框的宽高
//        params.width = 200;
//        params.height = 200;
////        params.gravity = Gravity.LEFT;
//        params.x = 200;
//        params.y = 000;
        // 设置悬浮框的Touch监听
//        btnView.setOnTouchListener(new View.OnTouchListener() {
//            //保存悬浮框最后位置的变量
//            int lastX, lastY;
//            int paramX, paramY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        lastX = (int) event.getRawX();
//                        lastY = (int) event.getRawY();
//                        paramX = params.x;
//                        paramY = params.y;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        int dx = (int) event.getRawX() - lastX;
//                        int dy = (int) event.getRawY() - lastY;
//                        params.x = paramX + dx;
//                        params.y = paramY + dy;
//                        // 更新悬浮窗位置
//                        windowManager.updateViewLayout(btnView, params);
//                        break;
//                }
//                return true;
//            }
//        });
//        manager.addView(btnView, params);
//        isAdded = true;
    }
//    class RefreshTask extends TimerTask{
//        @Override
//        public void run(){
//            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
//            if (!MyWindowManager.isWindowShowing()) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("create","res");
//                        MyWindowManager.createSmallWindow(getApplicationContext());
//                    }
//                });
//            }
////         else if( MyWindowManager.isWindowShowing()){
////             handler.post(new Runnable() {
////                 @Override
////                 public void run() {
////                     MyWindowManager.removeSmallWindow(getApplicationContext());
////                     MyWindowManager.removeBigWindow(getApplicationContext());
////                 }
////             });
////         }
//            else if (MyWindowManager.isWindowShowing()) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("context",String.valueOf(getApplicationContext()));
//                        MyWindowManager.updateUsedPercent(getApplicationContext());
//                    }
//                });
//            }
//        }
//    }
    /**
     * 判断当前界面是否是桌面
     */
//    private boolean isHome() {
//        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
//        return getHomes().contains(rti.get(0).topActivity.getPackageName());
//    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
//    private List<String> getHomes() {
//        List<String> names = new ArrayList<String>();
//        PackageManager packageManager = this.getPackageManager();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
//                PackageManager.MATCH_DEFAULT_ONLY);
//        for (ResolveInfo ri : resolveInfo) {
//            names.add(ri.activityInfo.packageName);
//        }
//        return names;
//    }

}