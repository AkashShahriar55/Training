package com.example.playstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;


public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "FirstActivity";

    AdView adView;
    ConstraintLayout adContainerView;

    private TextView welcomeMsgTv;

    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        MobileAds.initialize(this);
        MyAdLoader.loadAd(this);

        welcomeMsgTv = findViewById(R.id.welcome_tv);

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        HashMap<String,Object> defaults = new HashMap<>();
        defaults.put("welcome_message","Welcome to this app");
        defaults.put("banner_ad",true);
        remoteConfig.setDefaultsAsync(defaults);


        
        Button openAppStoreBtn = findViewById(R.id.open_app_store);
        openAppStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });



        /**Step 1 - Create an AdView and set the ad unit ID on it.**/
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id));

        /**Step 2 - add the adView to a container and manage the view location
        //getting the container of the banner
        adContainerView = findViewById(R.id.ad_container_view);
        //set an id for manage the view later
        adView.setId(View.generateViewId());
        //add the view to the container
        adContainerView.addView(adView);
        // Manage the location using ConstraintSet for constraint layout
        ConstraintSet constraintSet = new ConstraintSet();
        int id = adView.getId();
        constraintSet.clone(adContainerView);
        constraintSet.connect(id,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);
        constraintSet.connect(id,ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT);
        constraintSet.connect(id,ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT);
        constraintSet.applyTo(adContainerView);**/


        /**Step 2 - add the adView to a container and manage the view location**/
        //getting the container of the banner
        LinearLayout adContainer = findViewById(R.id.banner_holder);
        //add the view to the container
        adContainer.addView(adView);




        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);
                            Toast.makeText(FirstActivity.this, "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(FirstActivity.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        displayWelcomeMessage();
                        loadBanner();
                    }
                });

        Button crashBtn = findViewById(R.id.crash_button);
        crashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

    }

    private void displayWelcomeMessage() {
        String welcomeMsg = remoteConfig.getString("welcome_message");
        welcomeMsgTv.setText(welcomeMsg);
    }

    private void loadBanner() {
        boolean showBannerAd = remoteConfig.getBoolean("banner_ad");
        if(!showBannerAd){
            return;
        }
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();

        /**Step 5 - Set the adaptive ad size on the ad view.**/
        adView.setAdSize(adSize);

        /**Step 5 - Start loading the ad in the background.**/
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        /**Step 3 - Determine the screen width (less decorations) to use for the ad width**/
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        /**Step 4 - Get adaptive ad size and return for setting on the ad view.**/
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
