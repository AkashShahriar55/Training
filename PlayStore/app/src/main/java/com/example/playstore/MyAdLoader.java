package com.example.playstore;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

public class MyAdLoader {
    private static List<InterstitialAd> interstitialAds = new ArrayList<>();
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

    static public void loadAd(final Context context){
        for (int i = 0; i < 5; i++) {
            // Create the InterstitialAd and set the adUnitId.
            final InterstitialAd interstitialAd = new InterstitialAd(context);
            // Defined in res/values/strings.xml
            interstitialAd.setAdUnitId(AD_UNIT_ID);

            interstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
            });
            interstitialAd.loadAd(new AdRequest.Builder().build());
            interstitialAds.add(interstitialAd);
        }
    }

    public static List<InterstitialAd> getInterstitialAds() {
        return interstitialAds;
    }
}
