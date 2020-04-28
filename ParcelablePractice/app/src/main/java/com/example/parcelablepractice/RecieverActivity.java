package com.example.parcelablepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RecieverActivity extends AppCompatActivity {

    private TextView name,age,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciever);

        name = findViewById(R.id.textName);
        age = findViewById(R.id.textAge);
        phone = findViewById(R.id.textPhone);

        Intent intent = getIntent();
        Details details = intent.getParcelableExtra("Information");

        if (details != null) {
            name.setText("Name: "+details.getName());
            age.setText("Age: "+details.getAge());
            phone.setText("Phone: "+details.getPhone());
        }

    }
}
