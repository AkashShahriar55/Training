package com.example.playstore.ui.apps;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playstore.CollectionAdapter;
import com.example.playstore.CollectionData;
import com.example.playstore.MyReader;
import com.example.playstore.R;
import com.example.playstore.AppData;
import com.example.playstore.UiHelper;
import com.example.playstore.UnifiedNativeAdViewHolder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.List;

public class AppsFragment extends Fragment {

    private AppsViewModel appsViewModel;
    private ConstraintLayout adContainer;
    private float screenwidth;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_apps, container, false);
        
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initializeCollectionView(view);
    }

    private void initializeCollectionView(View view) {

        RecyclerView appCollectionRV = view.findViewById(R.id.collection_holder);
        //appCollectionRV.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        appCollectionRV.setLayoutManager(layoutManager);

        List<CollectionData> CollectionList = new MyReader(getContext()).readAppsData();

        CollectionAdapter adapter = new CollectionAdapter(getContext(),CollectionList);
        appCollectionRV.setAdapter(adapter);

        adContainer = view.findViewById(R.id.apps_ad_container);
        adContainer.setVisibility(View.GONE);

        loadNativeAds();
    }

    private void loadNativeAds() {
        AdLoader.Builder builder = new AdLoader.Builder(requireContext(),getResources().getString(R.string.ads_app_unit));
        AdLoader adLoader = builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // A native ad loaded successfully, check if the loader has finished loading
                //and if so, insert the ads into the list
                populateNativeAdView(unifiedNativeAd);
                adContainer.setVisibility(View.VISIBLE);
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animate);
                adContainer.startAnimation(hyperspaceJumpAnimation);

            }
        }).withAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                // A native ad failed to load, check if the ad loader has finished loading
                // and if so, insert the ads into the list.
                Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                        + " load another.");
            }
        }).build();

        // Load the Native Express ad.
        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd) {
        ImageView googlePlay = root.findViewById(R.id.today_ad_google_play);
        UnifiedNativeAdView adView = root.findViewById(R.id.today_ad_view);
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


        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();



        if (icon == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            googlePlay.setVisibility(View.GONE);
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            googlePlay.setVisibility(View.VISIBLE);
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
}
