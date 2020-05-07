package com.example.imagepicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;

import javax.xml.transform.Source;

public class ImageActivity extends AppCompatActivity {

    ProgressBar imageLoadProgress;
    ImageView imageHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageHolder = findViewById(R.id.image_holder);
        TextView imageName = findViewById(R.id.image_name);
        imageLoadProgress = findViewById(R.id.imageLoadProgress);

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
            new ShowImage(this).execute(image.getImageUri());
        } else{
            imageName.setText("Empty image");
        }

    }

    // make static so that it is separate from the outer class,
    // if we update the views directly of outer class and if the outer class destroys the reference can not be garbage collected
    // as the asynctask class is another reference . it will cause memory leak
    private static class ShowImage extends AsyncTask<Uri,Integer,Bitmap>{

        private WeakReference<ImageActivity> activityWeakReference;

        public ShowImage(ImageActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            //show the progress bar
            activityWeakReference.get().imageLoadProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Uri... uris) {
            Uri uri = uris[0];
            Bitmap bitmap = null;
            try {
                // as Media.getBitmap is depreciated above android sdk 29
                if(Build.VERSION.SDK_INT < 28){
                        bitmap = MediaStore.Images.Media.getBitmap(activityWeakReference.get().getContentResolver(),uri);
                } else{
                    // create a new source from a Uri
                    // we will use ImageDecoder for api level 29 and greater
                    ImageDecoder.Source source = ImageDecoder.createSource(activityWeakReference.get().getContentResolver(),uri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            activityWeakReference.get().imageLoadProgress.setVisibility(View.GONE);
            activityWeakReference.get().imageHolder.setImageBitmap(bitmap);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
