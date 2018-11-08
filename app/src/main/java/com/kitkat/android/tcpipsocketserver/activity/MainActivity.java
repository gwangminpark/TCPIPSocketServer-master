package com.kitkat.android.tcpipsocketserver.activity;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.kitkat.android.tcpipsocketserver.R;
import com.kitkat.android.tcpipsocketserver.app.SocketService;
import com.kitkat.android.tcpipsocketserver.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private SocketService socketService;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        // 서비스와 연결
        Intent intent = new Intent(this, SocketService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        startService(intent);

        binding = DataBindingUtil.setContentView( this, R.layout.activity_main );

        binding.buttonConnect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );

        binding.buttonSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    socketService.send( "광민!!" );
            }

        } );
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 서비스와 연결 해제
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /**
     * bindService() 를 통해 서비스와 연결될 때의 콜백 정의
     */
    private ServiceConnection mConnection = new ServiceConnection() {
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
