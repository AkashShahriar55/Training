package com.example.playstore;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MyAdLoader {
    private static InterstitialAd interstitialAd;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

    static public void loadAd(final Context context){
        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(context);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(AD_UNIT_ID);

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                Toast.makeText(context," New Ad Loaded",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public static InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

}
