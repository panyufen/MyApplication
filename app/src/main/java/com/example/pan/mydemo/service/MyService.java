package com.example.pan.mydemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.pan.mydemo.IMyAidlInterface;

public class MyService extends Service {

    private MyIBinder myIBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        myIBinder = new MyIBinder();
        Toast.makeText(this, "service start", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("AIDL ", "onBind");
        return new LocalBinder();
    }

    public class MyIBinder extends IMyAidlInterface.Stub {

        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         *
         * @param anInt
         * @param aLong
         * @param aBoolean
         * @param aFloat
         * @param aDouble
         * @param aString
         */
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int start(int id) throws RemoteException {
            Log.i("AIDL ", "start");
            return 1;
        }

        @Override
        public int stop(int id) throws RemoteException {
            Log.i("AIDL ", "stop");
            return 2;
        }
    }

    public int start() {
        return 11;
    }

    public int stop() {
        return 22;
    }


    public class LocalBinder extends Binder {

        public MyService getBinder() {
            return MyService.this;
        }

    }
}
