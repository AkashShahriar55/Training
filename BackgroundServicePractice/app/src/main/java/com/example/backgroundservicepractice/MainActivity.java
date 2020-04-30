package com.example.backgroundservicepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, ExampleService.class);
        //startService(intent);

        Intent intent1 = new Intent(this,AnotherService.class);
        startService(intent1);
        Intent intent2 = new Intent(this,AnotherService.class);
        startService(intent2);
    }
}
