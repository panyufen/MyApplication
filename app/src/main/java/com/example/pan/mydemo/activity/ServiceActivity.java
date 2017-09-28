package com.example.pan.mydemo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.IMyAidlInterface;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.service.MyService;

public class ServiceActivity extends BaseActivity {

    private static final String REMOTE_SERVICE_ACTION = "com.example.pan.mydemo.aidl";
    private Toolbar mToolBar;
    private IMyAidlInterface myAidlInterface;

    private MyService myService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            myService = ((MyService.LocalBinder)service).getBinder();
            Toast.makeText(ServiceActivity.this, "bind success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(ServiceActivity.this, "onServiceDisconnected", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        mToolBar = setSupportActionBar(R.id.tool_bar);

    }

    public void bind(View v) {
        Intent intent = new Intent(REMOTE_SERVICE_ACTION);
        intent.setPackage(this.getPackageName());
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void unbind(View v) {
        if (myAidlInterface != null) {
            unbindService(serviceConnection);
        }
    }


    public void start(View v) {
//        try {
//            if (myAidlInterface != null) {
//                Toast.makeText(this, String.valueOf(myAidlInterface.start(1111)), Toast.LENGTH_SHORT).show();
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
        if( myService!= null ){
            Toast.makeText(this, String.valueOf(myService.start()), Toast.LENGTH_SHORT).show();
        }
    }

    public void stop(View v) {
//        try {
//            if (myAidlInterface != null) {
//                Toast.makeText(this, String.valueOf(myAidlInterface.stop(2222)), Toast.LENGTH_SHORT).show();
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
        if( myService!= null ){
            Toast.makeText(this, String.valueOf(myService.stop()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("Lifecycle onDestroy");
        if (myAidlInterface != null) {
            unbindService(serviceConnection);
        }
    }

}
