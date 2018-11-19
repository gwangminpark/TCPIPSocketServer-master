package com.insungdata.android.sockettest.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by user on 2018-11-14.
 */

public class MyReceiver extends BroadcastReceiver {
    public static final String MY_ACTION = "Test!!!";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Toast.makeText(context, "전원 연결 됨", Toast.LENGTH_SHORT).show();
        } else if (MY_ACTION.equals(intent.getAction())) {
            Toast.makeText(context, "이 방송은 나만의 방송", Toast.LENGTH_SHORT).show();

            // 이후의 브로드캐스트의 전파를 막기
            abortBroadcast();
        }
    }
}
