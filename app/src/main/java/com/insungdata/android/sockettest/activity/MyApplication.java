package com.insungdata.android.sockettest.activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import com.insungdata.android.sockettest.app.SocketService;
import com.insungdata.android.sockettest.content.NetworkReceiver;

/**
 * Created by user on 2018-11-09.
 */

public class MyApplication extends Application {

    private final String FIILTER_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public final static String networkIntentValue = "NETWORK_STATE";
    public final static String INTENT_HEAD = "INSUNG_TEST_";

    public static SocketService socketService;

    private boolean mBound;
    private Context context;
    private BroadcastReceiver mReceiver = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent( this, SocketService.class );
        bindService( intent, mConnection, BIND_AUTO_CREATE );

        registerReceiver();
    }

    private void registerReceiver() {
        /** 1. intent filter를 만든다
         *  2. intent filter에 action을 추가한다.
         *  3. BroadCastReceiver를 구현한다.
         *  4. intent filter와 BroadCastReceiver를 등록한다.
         * */
        if (mReceiver != null) return;

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction( FIILTER_ACTION );
        this.mReceiver = new NetworkReceiver();
        this.registerReceiver( this.mReceiver, theFilter );

    }

    private void unregisterReceiver() {
        if (mReceiver != null)
            this.unregisterReceiver( mReceiver );
    }

    /**
     * bindService() 를 통해 서비스와 연결될 때의 콜백 정의
     */
    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // MyBinder와 연결될 것이며 IBinder 타입으로 넘어오는 것을 캐스팅하여 사용
            SocketService.MyBinder binder = (SocketService.MyBinder) service;
            socketService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 예기치 않은 종료
        }
    };
}
