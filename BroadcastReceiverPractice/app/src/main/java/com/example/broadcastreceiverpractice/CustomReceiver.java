package com.example.broadcastreceiverpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomReceiver extends BroadcastReceiver {

    private static final String ACTION_CUSTOM_BROADCAST = BuildConfig.APPLICATION_ID+".ACTION_CUSTOM_BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        // get the intent action from the intent parameter
        String intentAction = intent.getAction();

        //get the random no from extra
        int rand = intent.getIntExtra("random",0);

        // switch for different toast message based on action
        if(intentAction != null){
            String toastMessage = "unknown intent action";
            switch (intentAction){
                case Intent.ACTION_POWER_CONNECTED:
                    toastMessage = "Power connected";
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    toastMessage = "Power Disconnected";
                    break;
                //respond to the custom broadcast
                case ACTION_CUSTOM_BROADCAST:
                    toastMessage = "Custom Broadcast Received and the number is "+rand;
                    break;
            }
            //Display the message
            Toast.makeText(context,toastMessage,Toast.LENGTH_SHORT).show();
        }

    }
}
