package com.example.parcelablepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private String name,age,phone;
    private TextInputLayout inputName,inputAge,inputPhone;
    private Button buttonSubmit;
    private Details details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputName = findViewById(R.id.mName);
        inputAge = findViewById(R.id.mAge);
        inputPhone = findViewById(R.id.mPhone);
        buttonSubmit = findViewById(R.id.mSubmit);



        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = inputName.getEditText().getText().toString();
                age = inputAge.getEditText().getText().toString();
                phone = inputPhone.getEditText().getText().toString();

                if(name.trim().isEmpty()){
                    inputName.setError("This field must not empty");
                } else if (age.trim().isEmpty()){
                    inputName.setError("This field must not empty");
                } else if(phone.trim().isEmpty()){
                    inputName.setError("This field must not empty");
                }else {
                    details = new Details(name,age,phone);
                    Intent intent = new Intent(MainActivity.this, RecieverActivity.class);
                    intent.putExtra("Information", details);
                    startActivity(intent);
                }
            }
        });


    }
}
