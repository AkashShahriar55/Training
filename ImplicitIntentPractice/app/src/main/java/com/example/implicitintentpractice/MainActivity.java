package com.example.implicitintentpractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout mWebsiteEditText,mLocationEditText,mShareEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebsiteEditText = findViewById(R.id.textInputLayout);
        mLocationEditText = findViewById(R.id.textInputLayout2);
        mShareEditText = findViewById(R.id.textInputLayout3);
    }

    public void openWebsite(View view) {

        String url = mWebsiteEditText.getEditText().getText().toString();
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,webpage);
        //in this Intent constructor we specify an action and the data for the action
        //ACTION_VIEW(for view the data),ACTION_EDIT(for edit the data),
        // //ACTION_DIAL(for dial a phone number)

        if(intent.resolveActivity(getPackageManager()) != null){
            // in this block we are checking whether there is an Activity
            // that can handle our intent
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents","Can't handle this");
        }
    }

    public void openLocation(View view) {
        String loc = mLocationEditText.getEditText().getText().toString();

        //parse the location and create intent
        Uri addressUri = Uri.parse("geo:0,0?q="+loc);
        Intent intent = new Intent(Intent.ACTION_VIEW,addressUri);
        //this intent is for view the data somewhere

        if(intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
        }else{
            Log.d("ImplicitIntents","Can't handle this");
        }
    }

    public void shareText(View view) {
        String txt = mShareEditText.getEditText().getText().toString();
        String mimeType = "text/plain";

        ShareCompat.IntentBuilder.from(this)
                .setType(mimeType)
                .setChooserTitle("Share this text with: ")
                .setText(txt)
                .startChooser();
        //ShareCompat.IntentBuilder is for sharing data in various app
        // from() - the activity that launches this share intent
        //setType() - the mime type of the item to be shared
        // setChooserTitle() - the title on the system app chooser
        // setText() - the actual text to be shared, use setStream for sending audio files
        // startChooser() - show the system app chooser and send the intent
    }


}
