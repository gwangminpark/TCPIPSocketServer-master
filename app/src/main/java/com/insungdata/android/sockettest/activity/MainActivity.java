package com.insungdata.android.sockettest.activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.insungdata.android.sockettest.R;
import com.insungdata.android.sockettest.app.SocketService;
import com.insungdata.android.sockettest.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    public static final String INTENT_FILTER = MyApplication.INTENT_HEAD + "MAIN";

    private static SocketRecv Socketreceiver;

    private ActivityMainBinding binding;
    private MyApplication myApplication;
    private boolean mBound;

    AlertDialog.Builder builder;
    AlertDialog networkAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = DataBindingUtil.setContentView( this, R.layout.activity_main );

        // 서비스스타트
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);


        myApplication=(MyApplication)getApplicationContext();

        //리시버등록
        Socketreceiver= new SocketRecv();
        this.registerReceiver(Socketreceiver, new IntentFilter( INTENT_FILTER ) );

        binding.buttonConnect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );

        binding.buttonSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   myApplication.socketService.send( "광민!!" );
            }

        } );
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 서비스와 연결 해제
        if (mBound) {
            unbindService((myApplication.mConnection));
            mBound = false;
        }
        if (Socketreceiver != null)
            unregisterReceiver( Socketreceiver );
    }

    public class SocketRecv extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals( INTENT_FILTER ) == true) {

                if (intent.getBooleanExtra( MyApplication.networkIntentValue, false )) {
                    // 네트워크 성공 시
                    binding.buttonConnect.setBackgroundColor( Color.parseColor( "#2E64FE" ));

                }else if(intent.getBooleanExtra( "test" ,true)){
                    binding.buttonConnect.setBackgroundColor( Color.parseColor( "#01DF3A" ));
                }
                else {
                    // 네트워크 실패
                    if (networkAlertDialog != null && networkAlertDialog.isShowing()) {
                        return;
                    }
                    binding.buttonConnect.setBackgroundColor( Color.parseColor( "#FE2E2E" ));
   /*                 builder = new AlertDialog.Builder( MainActivity.this );
                    builder.setTitle( "네트워크 에러" );
                    builder.setMessage( "통신이 원할하지 않습니다 재시도해주세요." );
                    builder.setPositiveButton( "연결", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    } );
                    networkAlertDialog = builder.create();
                    networkAlertDialog.show();
                    networkAlertDialog.setOnDismissListener( new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //연결클릭시
                            Intent intent = new Intent(MainActivity.this, SocketService.class);
                            startService(intent);

                        }
                    } );*/
                }
            }
        }
    }
}
