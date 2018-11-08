package com.kitkat.android.tcpipsocketserver.app;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

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
    private static final String TAG = SocketService.class.getSimpleName();
    private Socket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private String mMessage;
    // Service의 레퍼런스를 반환하는 Binder 객체
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

        new SocketConnect().execute();

        return START_NOT_STICKY;
    }

    public void send(final String data) {
/*
        try {
            byte[] byteArr;
            byteArr = data.getBytes( "UTF-8" );
            mOutputStream.write( byteArr );
            mOutputStream.flush();
        } catch (IOException e) {
            //소켓 다시 파이프 연결 요청
            new SocketConnect().execute();
            e.printStackTrace();
        }
*/
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {

                    byte[] byteArr;
                    byteArr = data.getBytes( "UTF-8" );
                    mOutputStream.write( byteArr );
                    mOutputStream.flush();

                } catch (IOException e) {
                    //소켓 다시 파이프 연결 요청
                    new SocketConnect().execute();
                    e.printStackTrace();
                }
            }
        } ).start();
    }

    public class SocketConnect extends AsyncTask<Void, Integer, Boolean> {

        // doInBackground 메소드가 실행되기 전에 실행되는 메소드
        @Override
        protected void onPreExecute() {
            // UI 작업을 수행하는 부분
            super.onPreExecute();
        }

        // 실제 비즈니스 로직이 처리될 메소드(Thread 부분이라고 생각하면 됨)
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

        // 모든 작업이 끝난 후 처리되는 메소드
        // doInBackground의 수행이 끝난 뒤 실행하고 싶은 경우

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // 연결됐다는 것을 알려줘야 하는 부분으로 결과를 전송하자.
            } else {
                // 소켓 연결이 되지 않았기 때문에 다시 연결해주는 루틴을 만들어 호출하자.
            }
        }
    }

    public class MyBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }
}
