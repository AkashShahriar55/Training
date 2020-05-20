package com.example.playstore;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.ArrayList;
import java.util.List;

public class MyAdLoader {
    private static List<PublisherInterstitialAd> interstitialAds = new ArrayList<>();
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

    static public void loadAd(final Context context){
        for (int i = 0; i < 5; i++) {

            // Create the PublisherInterstitialAd and set the adUnitId.
            final PublisherInterstitialAd interstitialAd = new PublisherInterstitialAd(context);
            // Defined in res/values/strings.xml
            interstitialAd.setAdUnitId(AD_UNIT_ID);

            interstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    interstitialAd.loadAd(new PublisherAdRequest.Builder().build());
                }
            });

            interstitialAd.loadAd(new PublisherAdRequest.Builder().build());

            interstitialAds.add(interstitialAd);
        }
    }

    public static List<PublisherInterstitialAd> getInterstitialAds() {
        return interstitialAds;
    }
}
