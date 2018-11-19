package com.insungdata.android.sockettest.app;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.insungdata.android.sockettest.activity.MainActivity;
import com.insungdata.android.sockettest.activity.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by user on 2018-10-25.
 */

public class SocketService extends Service {
    public static final String SERVER_IP = "192.168.1.96";
    public static final int SERVER_PORT = 5025;

    private final int HANDLER_SOCKET_CONNECTED = 1001;
    private final int HANDLER_SOCKETRE_COUNT = 1002;
    private final int HANDLER_NETWORK_ERROR = 1003;
    private final int HANDLER_NETWORK_OK = 1004;
    private final int HANDLER_DATA_ERROR = 1005;

    private static final String TAG = SocketService.class.getSimpleName();
    private Socket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private int mSocketRecount =0;
    private String mMessage;
    private IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d( TAG, "onCreate: " );
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d( TAG, "onBind: " );
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d( TAG, "onUnbind: " );
        return super.onUnbind( intent );
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //서비스 상태 를 확인해서 초기화후 커넥션 연결
        if (mSocket == null || mSocket.isClosed() == true || mSocket.isConnected() == false) {
            handler.sendEmptyMessage( HANDLER_SOCKET_CONNECTED );
        } else {
            //소켓이 정상 연결되있을때 StartService할 경우가 있을까..??
        }
        return START_NOT_STICKY;
    }

    public void send(final String data) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    if (mOutputStream == null) {
                        onSocketReset();
                        new SocketConnect().execute();
                    }
                    byte[] byteArr;
                    byteArr = data.getBytes( "UTF-8" );
                    mOutputStream.write( byteArr );
                    mOutputStream.flush();

                } catch (IOException e) {

                    //소켓 다시 파이프 연결 요청
                    onSocketReset();
                    new SocketConnect().execute();
                    e.printStackTrace();
                }
            }
        } ).start();
    }

    public void onSocketReset() {
        mSocketRecount++;
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
                mOutputStream = null;
            }
            if (mInputStream != null) {
                mInputStream.close();
                mInputStream = null;
            }
            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
        } catch (Exception e) {

        }
    }

    public class SocketConnect extends AsyncTask<Void, Integer, Boolean> {

        // doInBackground 메소드가 실행되기 전에 실행되는 메소드
        @Override
        protected void onPreExecute() {
            // UI 작업을 수행하는 부분
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mSocket = new Socket();
                mSocket.connect( new InetSocketAddress( SERVER_IP, SERVER_PORT ) );

                mInputStream = mSocket.getInputStream();
                mOutputStream = mSocket.getOutputStream();

                return mSocket.isConnected();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                handler.sendEmptyMessage( HANDLER_NETWORK_OK );
            } else {
                Intent intent = new Intent( MainActivity.INTENT_FILTER );
                intent.putExtra( MyApplication.networkIntentValue, false );
                sendBroadcast( intent );

                onSocketReset();
                new SocketConnect().execute();
                // 소켓 연결이 되지 않았기 때문에 다시 연결해주는 루틴을 만들어 호출하자.
            }
        }
    }

    public class MyBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_SOCKET_CONNECTED ) {
                onSocketReset();
                new SocketConnect().execute();

            } else if (msg.what == HANDLER_SOCKETRE_COUNT) {

                if (mSocketRecount >= 5) {
                    ++mSocketRecount;                //SocketConnect에서 예외발생이 5회이상일때 안보내주기 위해서 값을 추가한다.
                    Toast.makeText( SocketService.this, "프로그램 종료 후 인터넷을 확인 후 연결해주세요.", Toast.LENGTH_LONG ).show();
                } else {
                    ++mSocketRecount;
                    Toast.makeText( SocketService.this, "재접속 카운트" + mSocketRecount + "회", Toast.LENGTH_SHORT ).show();
                }

            } else if (msg.what == HANDLER_NETWORK_ERROR) {

                //에러가 있다는것을 액티비티로 전송
                Intent intent = new Intent( MainActivity.INTENT_FILTER );
                intent.putExtra( MyApplication.networkIntentValue, false );
                sendBroadcast( intent );

            } else if (msg.what == HANDLER_NETWORK_OK) {

                // 연결됐다는 것을 알려줘야 하는 부분(액티비티) 로 결과를 전송
                Intent intent = new Intent( MainActivity.INTENT_FILTER );
                intent.putExtra( MyApplication.networkIntentValue, true );
                sendBroadcast( intent );

            } else if (msg.what == HANDLER_DATA_ERROR) {

            }
        }

    };
}
