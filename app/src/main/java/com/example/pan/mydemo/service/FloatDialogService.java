package com.example.pan.mydemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.pan.mydemo.view.floatview.FloatManager;


public class FloatDialogService extends Service {


    public static FloatManager floatManager;

    public FloatDialogService() {

    }


    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //    FloatDialog.getInstence(this).createFloatDialog();
        if (floatManager == null) {
            floatManager = new FloatManager(this);
        }
        floatManager.createView();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (floatManager != null) {
            floatManager.removeView();
        }
        super.onDestroy();
    }
}
