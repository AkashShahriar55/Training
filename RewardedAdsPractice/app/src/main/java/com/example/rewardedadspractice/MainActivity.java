package com.example.rewardedadspractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;

import com.google.android.gms.ads.rewarded.RewardedAd;

import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    Button mShowAdBtn;
    TextView mLogTv;
    ConstraintLayout adContainerView;
    AdView adView;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this);

        mShowAdBtn = findViewById(R.id.show_ad_btn);
        mLogTv = findViewById(R.id.tv_log);
        adContainerView = findViewById(R.id.ad_container_view);

        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id));
        adContainerView.addView(adView);

        loadBanner();

        mShowAdBtn.setEnabled(false);

        this.rewardedAd = createAndLoadRewardedAds();

        adView.setAdListener(new AdListener(){
            //Override methods to do operation when event trigger
        });

    }

    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


    public void showAds(View view){
        if (rewardedAd.isLoaded()) {
            Activity activityContext = MainActivity.this;
            RewardedAdCallback adCallback = new RewardedAdCallback() {

                @Override
                public void onRewardedAdOpened() {
                    mLogTv.append("The rewarded ad was opened\n");
                }
                @Override
                public void onRewardedAdClosed() {
                    mShowAdBtn.setEnabled(false);
                    //Use this method to load next ad
                    rewardedAd = createAndLoadRewardedAds();
                    mLogTv.append("The rewarded ad was closed\n\n");
                }
                @Override
                public void onRewardedAdFailedToShow(int i) {
                    mLogTv.append("The rewarded ad was failed to show\n");
                }
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Reward item gives the amount by rewardItem.getAmount()(eg. 10)
                    // and type by rewardItem.getType() (eg. coin)
                    mLogTv.append("The earned reward is: "+rewardItem.getAmount()
                            +" "+rewardItem.getType()+"\n");
                    //eg. The earned reward is: 10 coin
                }
            };
            rewardedAd.show(activityContext, adCallback);
        } else {
            mLogTv.append("The rewarded ad wasn't loaded yet.\n");
        }
    }

    public RewardedAd createAndLoadRewardedAds(){

        final RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");

        RewardedAdLoadCallback loadCallback = new RewardedAdLoadCallback(){

            @Override
            public void onRewardedAdLoaded() {
                mShowAdBtn.setEnabled(true);
                // To get rewarded item before showing the ad rewardedAd.getRewardItem() can be used
                RewardItem rewardItem = rewardedAd.getRewardItem();
                mLogTv.append("The rewarded ad was loaded\n");
                mLogTv.append("And the reward is : "+rewardItem.getAmount()+" "+rewardItem.getType()+"\n");
            }

            @Override
            public void onRewardedAdFailedToLoad(int i) {
                mLogTv.append("The rewarded ad was failed to load\n");
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(),loadCallback);
        return rewardedAd;
    }
}
