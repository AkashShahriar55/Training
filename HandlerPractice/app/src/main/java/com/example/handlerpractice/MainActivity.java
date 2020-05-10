package com.example.handlerpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int TASK_A = 1;
    private static final int TASK_B = 2;

    private ExampleLooperThread looperThread = new ExampleLooperThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTask(View view) {
        looperThread.start();
    }

    public void stopTask(View view) {
        // if we don't quit the looper
        // if calling activity destroys there will be zombie thread running background
        looperThread.looper.quit();
    }

    public void startTaskA(View view) {

        Message msg = Message.obtain(); // obtain because it is recycled
        msg.what = TASK_A;
        looperThread.handler.sendMessage(msg);

        /***
         * this is using handler and runnable
         * here post is usually sending a message with a callback runnable
        Handler threadHandler = new Handler(looperThread.looper);

        // this runnable will have memory leak
        // it is a non static inner class that has implicit reference of the activity
        // if outer activity is destroyed but the reference of the runnable cannot be garbage collected
        // solution : make it static and access the ui variables using weakreference
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    Log.d(TAG, "run: "+i);
                    SystemClock.sleep(1000);
                }
            }
        });
         ***/
    }

    public void startTaskB(View view) {
        Message msg = Message.obtain(); // obtain because it is recycled
        msg.what = TASK_B;
        looperThread.handler.sendMessage(msg);
    }
}
