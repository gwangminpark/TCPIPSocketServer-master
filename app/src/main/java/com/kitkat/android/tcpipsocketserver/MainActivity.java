package com.kitkat.android.tcpipsocketserver;


import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.kitkat.android.tcpipsocketserver.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SocketService socketService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = DataBindingUtil.setContentView( this, R.layout.activity_main );

        binding.buttonConnect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketService=new SocketService();
                socketService.connect();

            }
        } );

        binding.buttonSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                socketService.send( String.valueOf( binding.id.getText() ));
            }
        } );
    }

}
