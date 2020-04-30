package com.example.backgroundservicepractice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnotherService extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    //Handler that receives messages from the thread
    private final class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            //Normally we would do some work here , like download a file.
            //For our sample , we just sleep for 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        //Start up the thread running the service. Note that we create a
        //separate thread because the service normally runs in the process's
        //main thread, which we don't want to block. We also make it
        //background priority so CPU-INTENSIVE work doesn;t disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();

        //Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"service starting"+ startId,Toast.LENGTH_SHORT).show();

        //For each start request, send a message to start a job and deliver the
        //start id so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        //If we get killed, after returning from here, restart
        return START_STICKY;
        //START_NOT_STICKY - do not recreate the service unless
        //there are pending intents to deliver

        //START_STICKY - recreate the service and call onStartCommand(),
        //but do not redeliver the last intent

        //START_REDELIVER_INTENT - recreate the service and call onStartCommand()
        //with the last intent that was delivered to the service
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"service done",Toast.LENGTH_SHORT).show();
    }
}
