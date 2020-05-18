package com.example.playstore;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

public class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {
    private UnifiedNativeAdView adView;
    private ImageView googlePlayLogo;

    public UnifiedNativeAdView getAdView() {
        return adView;
    }

    public ImageView getGooglePlayLogo(){
        return googlePlayLogo;
    }

    public UnifiedNativeAdViewHolder(@NonNull View itemView) {
        super(itemView);
        adView = (UnifiedNativeAdView) itemView.findViewById(R.id.today_ad_view);
        googlePlayLogo = itemView.findViewById(R.id.today_ad_google_play);

        // The MediaView will display a video asset if one is present in the ad, and the
        // first image asset otherwise.
        adView.setMediaView((MediaView) adView.findViewById(R.id.today_ad_media));

        // Register the view used for each individual asset.
        adView.setHeadlineView(adView.findViewById(R.id.today_ad_headline));
        adView.setBodyView(adView.findViewById(R.id.today_ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.today_ad_to_call_action));
        adView.setIconView(adView.findViewById(R.id.today_ad_icon));
        adView.setPriceView(adView.findViewById(R.id.today_ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.today_ad_rating));
        adView.setStoreView(adView.findViewById(R.id.today_ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.today_ad_advertiser));
    }
}
