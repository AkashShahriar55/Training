package com.example.playstore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SingleItemAdapter extends RecyclerView.Adapter<SingleItemAdapter.AppNormalViewHolder> {

    private static final String TAG = "SingleItemAdapter";

    private Context mContext;
    private List<AppData> itemList;

    public SingleItemAdapter(Context mContext, List<AppData> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public AppNormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.view_single_item,parent,false);
        return new AppNormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppNormalViewHolder holder, int position) {
        AppData item = itemList.get(position);
        if(position == getItemCount()-1){
            UiHelper uiHelper = new UiHelper(mContext);
            uiHelper.setViewWidth(holder.itemLayout,500);
        }

        holder.singleItemName.setText(item.getAppName());
        holder.singleItemSize.setText(item.getAppSize());
        holder.singleItemImageHolder.setImageBitmap(new MyReader(mContext).readImageFromAssets(item.getLogoReference()));

        holder.singleItemImageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,AppDetailsActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class AppNormalViewHolder extends RecyclerView.ViewHolder{

        ImageView singleItemImageHolder;
        TextView singleItemName;
        TextView singleItemSize;
        ConstraintLayout itemLayout;

        public AppNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            singleItemImageHolder = itemView.findViewById(R.id.single_item_image_holder);
            singleItemName = itemView.findViewById(R.id.single_item_name);
            singleItemSize = itemView.findViewById(R.id.single_item_size);
            itemLayout = itemView.findViewById(R.id.single_item_layout);
        }
    }
}
