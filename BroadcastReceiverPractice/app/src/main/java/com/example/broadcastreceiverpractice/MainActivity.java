package com.example.broadcastreceiverpractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private CustomReceiver mReceiver = new CustomReceiver();

    private static final String ACTION_CUSTOM_BROADCAST
            = BuildConfig.APPLICATION_ID+".ACTION_CUSTOM_BROADCAST";

    private TextView message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = findViewById(R.id.textView);

        //it specifies the types of intents a component can receive
        //it will filter out the intents based on action and category
        IntentFilter filter = new IntentFilter();
        //When the system receives an intent as a broadcast
        //it searches the receivers based on action value

        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        //register the receiver using MainActivity context so it will able
        // to receive broadcasts as long as MainActivity is running

        this.registerReceiver(mReceiver,filter);

        //register custom broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver
                ,new IntentFilter(ACTION_CUSTOM_BROADCAST));


    }


    @Override
    protected void onDestroy() {
        //Unregister the receiver
        //to save system resources and avoid leaks
        //dynamic receiver must be unregistered when not needed
        this.unregisterReceiver(mReceiver);
        super.onDestroy();

        //unregister custom broadcast
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }


    public void sendCustomBroadcast(View view) {
        //as this broadcast will be used in this app
        //LocalBroadcastManager will be used to register
        // for sending broadcasts within the app
        //local broadcasts keep information more secure
        //app data isn't shared with other android apps

        //create a custom broadcast intent
        Intent customBroadcastIntent = new Intent(ACTION_CUSTOM_BROADCAST);
        //sending random no using extra using intent
        int rand = new Random().nextInt(100);
        customBroadcastIntent.putExtra("random",rand);
        //send the broadcast using LocalBroadcastManager
        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent);

        message.setText(String.valueOf(rand));
    }
}
