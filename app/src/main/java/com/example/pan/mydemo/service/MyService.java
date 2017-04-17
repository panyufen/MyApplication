package com.example.pan.mydemo.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.pan.mydemo.IMyAidlInterface;

public class MyService extends Service {

    private MyIBinder myIBinder;

    private AlertDialog alertDialog;
    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        myIBinder = new MyIBinder();
        showDialog();
        alertDialog.show();
    }

    public void show(){
        alertDialog.show();
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("aaaaa");
        alertDialog = builder.create();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("AIDL ","onBind");
        return myIBinder;
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
        public void start(int id) throws RemoteException {
            Log.i("AIDL ","start");
            show();
        }

        @Override
        public void stop(int id) throws RemoteException {
            Log.i("AIDL ","stop");
        }
    }
}
