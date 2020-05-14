package com.example.playstore;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private Context mContext;
    private List<AppData> itemList;
    private UiHelper uiHelper;


    public BannerAdapter(Context mContext, List<AppData> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
        uiHelper = new UiHelper(mContext);
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.view_banner,parent,false);
        return new BannerAdapter.BannerViewHolder(view,uiHelper);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        AppData item = itemList.get(position);

        holder.appBannerImageHolderIV.setImageBitmap(new MyReader(mContext).readImageFromAssets(item.getBannerReference()));
        holder.appLogoImageHolderIV.setImageBitmap(new MyReader(mContext).readImageFromAssets(item.getLogoReference()));
        holder.appNameTV.setText(item.getAppName());
        holder.appTagTV.setText(item.getTag());
        holder.appTitleTV.setText(item.getTitle());

        if(position == getItemCount()-1){
            uiHelper.setViewWidth(holder.constraintLayout,uiHelper.getDisplayWidthInPixels());
            int leftPadding = (int) uiHelper.convertDpToPixels(10);
            int rightPadding = uiHelper.getPercentOfDisplayWidthInPixels((float) 0.2);
            holder.constraintLayout.setPadding(leftPadding,0,rightPadding,0);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{

        ImageView appBannerImageHolderIV;
        ImageView appLogoImageHolderIV;
        TextView appTagTV;
        TextView appNameTV;
        TextView appTitleTV;
        ConstraintLayout constraintLayout;
        UiHelper uiHelper;


        public BannerViewHolder(@NonNull View itemView, UiHelper uiHelper) {
            super(itemView);

            appBannerImageHolderIV = itemView.findViewById(R.id.banner_image_holder);
            appLogoImageHolderIV = itemView.findViewById(R.id.banner_logo);
            appTagTV = itemView.findViewById(R.id.banner_tag);
            appNameTV = itemView.findViewById(R.id.banner_app_name);
            appTitleTV = itemView.findViewById(R.id.banner_title);


            this.uiHelper = uiHelper;
            constraintLayout = itemView.findViewById(R.id.banner_constraint_layout);
            int width = uiHelper.getPercentOfDisplayWidthInPixels((float) 0.8);
            uiHelper.setViewWidth(constraintLayout,width);
        }
    }


}
