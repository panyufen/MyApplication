package com.example.pan.mydemo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.IMyAidlInterface;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class ServiceActivity extends BaseActivity {

    private static final String REMOTE_SERVICE_ACTION = "com.example.pan.mydemo.aidl";
    private Toolbar mToolBar;
    private IMyAidlInterface myAidlInterface;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        mToolBar = setSupportActionBar(R.id.tool_bar);
//        startService(new Intent(this,MyService.class));

    }

    public void bind(View v){
        Intent intent = new Intent(REMOTE_SERVICE_ACTION);
        intent.setPackage(this.getPackageName());
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);


//        Intent intent = new Intent(this,SecondActivity.class);
//        startActivity(intent);

//        show( v );

    }


    public void start( View v ) {
        try {
            myAidlInterface.start(1111);
        }catch( RemoteException e ){
            e.printStackTrace();
        }
    }

    public void stop ( View v ){
        try {
            myAidlInterface.stop(2222);

        }catch( RemoteException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("Lifecycle onDestroy");
        if( myAidlInterface != null ) {
            unbindService(serviceConnection);
        }
    }

}
