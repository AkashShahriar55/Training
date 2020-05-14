package com.example.playstore;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ThreeItemPagerAdapter extends RecyclerView.Adapter<ThreeItemPagerAdapter.TopFreeAppsViewHolder> {

    private static final String TAG = "ThreeItemPagerAdapter";
    private Context mContext;
    private List<AppData> itemList;
    private int displayWidth;

    public ThreeItemPagerAdapter(Context mContext, List<AppData> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;

    }

    @NonNull
    @Override
    public TopFreeAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.view_three_item_pager,parent,false);
        return new TopFreeAppsViewHolder(view,displayWidth);
    }

    @Override
    public void onBindViewHolder(@NonNull TopFreeAppsViewHolder holder, int position) {
        AppData item = itemList.get(position);

        holder.appNameTV.setText(item.getAppName());
        holder.appSizeTV.setText(item.getAppSize());
        holder.logoHolder.setImageBitmap(new MyReader(mContext).readImageFromAssets(item.getLogoReference()));

        if(position == getItemCount() - 1){
            ViewGroup.LayoutParams layoutParams =  holder.constraintLayout.getLayoutParams();
            layoutParams.width = displayWidth;
            holder.constraintLayout.setLayoutParams(layoutParams);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class TopFreeAppsViewHolder extends RecyclerView.ViewHolder{

        ImageView logoHolder;
        TextView appNameTV;
        TextView appSizeTV;
        ConstraintLayout constraintLayout;


        public TopFreeAppsViewHolder(@NonNull View itemView,int displayWidth) {
            super(itemView);

            logoHolder = itemView.findViewById(R.id.three_item_pager_logo_holder);
            appNameTV = itemView.findViewById(R.id.three_item_pager_app_name);
            appSizeTV = itemView.findViewById(R.id.three_item_pager_app_size);

            constraintLayout = itemView.findViewById(R.id.constraintLayout);

            ViewGroup.LayoutParams layoutParams =  constraintLayout.getLayoutParams();
            layoutParams.width = displayWidth - 100;
            constraintLayout.setLayoutParams(layoutParams);
        }
    }
}
