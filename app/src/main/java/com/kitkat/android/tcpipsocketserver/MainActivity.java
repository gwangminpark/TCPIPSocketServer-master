package com.kitkat.android.tcpipsocketserver;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    public static final String ip = "192.168.1.96";
    public static final int port = 9506;

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private EditText editTextID;
    private EditText editTextPW;
    private TextView textViewMsg;
    private Button buttonConnect;
    private Button buttonClose;
    public String host;
    Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        editTextID = (EditText) findViewById( R.id.id );
        editTextPW = (EditText) findViewById( R.id.pw );

        textViewMsg = (TextView) findViewById( R.id.textViewMsg );
        buttonConnect = (Button) findViewById( R.id.buttonConnect );
        buttonClose = (Button) findViewById( R.id.buttonClose );


        buttonConnect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connect( host );
            }
        } );
        buttonClose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler = new Handler();

                BackThread thread = new BackThread();  // 작업스레드 생성
                thread.setDaemon(true);  // 메인스레드와 종료 동기화
                thread.start();     // 작업스레드 시작 -> run() 이 작업스레드로 실행됨
            }
        } );
    }

    @SuppressLint("StaticFieldLeak")//메모리누수방지
    public void connect(String host) {

        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    socket = new Socket();
                    socket.connect( new InetSocketAddress( ip, port ) );
                    publishProgress();

                    is = socket.getInputStream();
                    os = socket.getOutputStream();

                    byte[] byteArr;
                    String msg = "소켓연결시작!!!";
                    byteArr = msg.getBytes( "UTF-8" );
                    os.write( byteArr );
                    os.write( byteArr );
                    os.flush();

                    byteArr = new byte[512];
                    int readByteCount = is.read( byteArr );

                    if (readByteCount == -1)
                        throw new IOException();

                    msg = new String( byteArr, 0, readByteCount, "UTF-8" );

                    os.close();
                    is.close();


                    return msg;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate( values );
                Toast.makeText( getApplicationContext(), "Server Connected!", Toast.LENGTH_SHORT ).show();
            }

            @Override
            protected void onPostExecute(String s) {

                // background스레드를 실행하기전 준비 단계이다.
                super.onPostExecute( s );
                textViewMsg.setText( s );
            }
        }.execute( host );
    }

    class BackThread extends Thread{  // Thread 를 상속받은 작업스레드 생성
        @Override
        public void run() {

            try {
                byte[] byteArr;
                String msg = editTextID.getText()+"\11"+editTextPW.getText();
                byteArr = msg.getBytes( "UTF-8" );
                socket.connect( new InetSocketAddress( ip, port ) );
                os = socket.getOutputStream();
                os.write( byteArr );
                os.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // end class BackThread
}
