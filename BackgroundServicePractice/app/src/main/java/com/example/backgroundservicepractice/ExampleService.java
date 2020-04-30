package com.example.backgroundservicepractice;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ExampleService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ExampleService() {
        super("ExampleService");
    }

    /**
     * The Intentservice calls this method from the default worker thread with
     * the intent that started the service. When this method returns,
     * IntentService stops the service ,as appropriate**/
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Normally we would do some work here,like download a file.
        //For example we just sleep for 5 seconds.

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //don't need to call the super method
        //onBind() also don't need to call super method
        //any other method need to call super method

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service starting",Toast.LENGTH_SHORT).show();
        //must need to call the super method
        return super.onStartCommand(intent, flags, startId);
    }
}
