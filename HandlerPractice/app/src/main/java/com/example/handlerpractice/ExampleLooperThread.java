package com.example.handlerpractice;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

public class ExampleLooperThread extends Thread {
    private static final String TAG = "ExampleLooperThread";

    public Looper looper;
    public Handler handler;
    // handler is associated with the message queue of this thread

    @Override
    public void run() {
        //handler will only work if there is a looper
        Looper.prepare(); // it creates both looper and msg queue

        looper = Looper.myLooper();//initialize looper

        // handler is associated with the message queue of this thread
        handler = new ExampleHandler();
        // start the loopers loop
        Looper.loop(); // this is an infinite loop until get a exit msg


        Log.d(TAG, "End of run():");
    }
}
