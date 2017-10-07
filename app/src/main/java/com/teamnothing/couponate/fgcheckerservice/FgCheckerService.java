package com.teamnothing.couponate.fgcheckerservice;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.teamnothing.couponate.R;

import java.util.List;

/**
 * Created by vazzup on 7/1s0/17.
 */

public class FgCheckerService extends Service {

    private WindowManager windowManager;
    private ImageView serviceHead;
    WindowManager.LayoutParams params;
    private GestureDetector gestureDetector;

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        serviceHead = new ImageView(this);
        serviceHead.setImageResource(R.mipmap.ic_launcher_round);

        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //this code is for dragging the chat head
        serviceHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(gestureDetector.onTouchEvent(event)) {
                    ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> services = activityManager
                            .getRunningTasks(Integer.MAX_VALUE);
                    // List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
                    Log.d("ProcessInfo", "size of services is " + services.size());
                    if(services.size() > 1) {
                        Log.d("ProcessInfo", "Name of service is " + services.get(1));
                    }
                    Toast.makeText(FgCheckerService.this, "Fg: " + services.get(0).topActivity.getPackageName().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY
                                    + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(serviceHead, params);
                            return true;
                    }
                }
                return false;
            }
        });

        /*serviceHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
                Toast.makeText(FgCheckerService.this, "Fg: " + services.get(0).processName, Toast.LENGTH_LONG).show();
            }
        })*/

        windowManager.addView(serviceHead, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(serviceHead != null) {
            windowManager.removeView(serviceHead);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
