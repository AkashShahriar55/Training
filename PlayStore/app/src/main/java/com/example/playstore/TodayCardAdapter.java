package com.example.playstore;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TodayCardAdapter extends RecyclerView.Adapter<TodayCardAdapter.TodayCardViewHolder>{

    private Context mContext;
    private List<TodayData> itemList;

    public TodayCardAdapter(Context mContext, List<TodayData> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public TodayCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.view_today_card,parent,false);
        return new TodayCardAdapter.TodayCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayCardViewHolder holder, int position) {
        TodayData data = itemList.get(position);
        Bitmap image = new MyReader(mContext).readImageFromAssets(data.getImageReference());
        holder.todayCardImage.setImageBitmap(image);
        holder.todayCardTag.setText(data.getTag());
        holder.todayCardTitle.setText(data.getTitle());
        holder.todayCardFooter.setText(data.getFooter());
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
