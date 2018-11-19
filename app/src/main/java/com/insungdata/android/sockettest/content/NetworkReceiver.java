package com.insungdata.android.sockettest.content;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.insungdata.android.sockettest.activity.MainActivity;
import com.insungdata.android.sockettest.activity.MyApplication;
import com.insungdata.android.sockettest.app.SocketService;

/**
 * Created by user on 2018-11-09.
 */

@SuppressLint("NewApi")
public class NetworkReceiver extends BroadcastReceiver implements ConnectivityManager.OnNetworkActiveListener {
    static  String action="test";
    private SocketService socket;
    @Override
    public void onReceive(Context context, Intent intent) {

        action=intent.getAction();

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){


            Log.d("TestReceiver","네트워크변경 감지 ");
            /*
            네트워크 변화가 생길때마다 이쪽으로 들어온다.
            */
        }

    }

    @Override
    public void onNetworkActive() {
        //. 이 리스너는 네트워크가 활성화 될 때만 알려줌
        //Log.d("TestReceiver","action : " + action);
    }
}

