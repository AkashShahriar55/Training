package com.example.imagepicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView imageHolder = findViewById(R.id.image_holder);
        TextView imageName = findViewById(R.id.image_name);

        //here the toolbar is created
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Image Viewer");
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        //the parcelable image data has fetched from the intent to show
        Intent intent = getIntent();
        Images image = intent.getParcelableExtra("Data");


        //set the image name and show the image using glide if image is not null
        if (image != null) {
            imageName.setText(image.getImageName());
            Glide.with(this)
                    .load(image.getImageUri())
                    .into(imageHolder);
        } else{
            imageName.setText("Empty image");
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
