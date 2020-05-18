package com.example.playstore;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.List;

public class TodayCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    // A today card item view type.
    private static final int TODAY_CARD_VIEW_TYPE = 0;

    // The unified native ad view type.
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    private Context mContext;

    private List<Object> itemList;

    public TodayCardAdapter(Context mContext, List<Object> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.view_ads_card,
                        parent, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case TODAY_CARD_VIEW_TYPE:
                // Fall through.
            default:
                View todayCardViewLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_today_card, parent, false);
                return new TodayCardViewHolder(todayCardViewLayout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = itemList.get(position);
        if (item instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return TODAY_CARD_VIEW_TYPE;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAdViewHolder adViewHolder = (UnifiedNativeAdViewHolder) holder;
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) itemList.get(position);
                populateNativeAdView(nativeAd, adViewHolder);
                break;
            case TODAY_CARD_VIEW_TYPE:
                // fall through
            default:
                TodayCardViewHolder todayCardItemHolder = (TodayCardViewHolder) holder;
                TodayData data = (TodayData) itemList.get(position);
                Bitmap image = new MyReader(mContext).readImageFromAssets(data.getImageReference());
                todayCardItemHolder.todayCardImage.setImageBitmap(image);
                todayCardItemHolder.todayCardTag.setText(data.getTag());
                todayCardItemHolder.todayCardTitle.setText(data.getTitle());
                todayCardItemHolder.todayCardFooter.setText(data.getFooter());
        }

    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdViewHolder holder) {
        UnifiedNativeAdView adView = holder.getAdView();
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
            holder.getGooglePlayLogo().setVisibility(View.GONE);
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            holder.getGooglePlayLogo().setVisibility(View.VISIBLE);
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

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class TodayCardViewHolder extends RecyclerView.ViewHolder{

        ImageView todayCardImage;
        TextView todayCardTag;
        TextView todayCardTitle;
        TextView todayCardFooter;

        public TodayCardViewHolder(@NonNull View itemView) {
            super(itemView);

            todayCardImage = itemView.findViewById(R.id.today_card_image);
            todayCardTag = itemView.findViewById(R.id.today_card_tag);
            todayCardTitle = itemView.findViewById(R.id.today_card_title);
            todayCardFooter = itemView.findViewById(R.id.today_card_footer);
        }
    }
}
