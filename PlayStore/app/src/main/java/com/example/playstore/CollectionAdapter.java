package com.example.playstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.List;

public class CollectionAdapter  extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {
    private Context context;
    private List<CollectionData> itemList;

    public CollectionAdapter(Context context, List<CollectionData> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(context);
        view = mInflator.inflate(R.layout.collection_holder_with_title,parent,false);
        return new CollectionAdapter.CollectionViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        CollectionData data = itemList.get(position);

        if(data.getTitle().equals("no_title")){
            holder.collectionTitle.setVisibility(View.GONE);
        }else{
            holder.collectionTitle.setText(data.getTitle());
        }

        switch (data.getType()){
            case "Single_item":
                SingleItemAdapter singleItemAdapter = new SingleItemAdapter(context,data.getAppData());
                holder.collectionRecyclerView.setAdapter(singleItemAdapter);

                SnapHelper singleItemSnapHelper = new MyLinearSnapHelper();
                singleItemSnapHelper.attachToRecyclerView(holder.collectionRecyclerView);
                break;
            case "Three_item_pager":
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3,GridLayoutManager.HORIZONTAL,false);
                holder.collectionRecyclerView.setLayoutManager(gridLayoutManager);

                ThreeItemPagerAdapter threeItemPagerAdapter = new ThreeItemPagerAdapter(context,data.getAppData());
                holder.collectionRecyclerView.setAdapter(threeItemPagerAdapter);

                SnapHelper threeItemPagerSanpHelper = new MyPagerSnapHelper();
                threeItemPagerSanpHelper.attachToRecyclerView(holder.collectionRecyclerView);
                break;
            case "Banner":
                BannerAdapter bannerAdapter = new BannerAdapter(context,data.getAppData());
                holder.collectionRecyclerView.setAdapter(bannerAdapter);

                SnapHelper bannerSnapHelper = new MyPagerSnapHelper();
                bannerSnapHelper.attachToRecyclerView(holder.collectionRecyclerView);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class CollectionViewHolder extends RecyclerView.ViewHolder{

        TextView collectionTitle;
        RecyclerView collectionRecyclerView;
        Context context;


        public CollectionViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            this.context = context;
            collectionTitle = itemView.findViewById(R.id.collection_title);
            collectionRecyclerView = itemView.findViewById(R.id.collection_recycler_view);
            collectionRecyclerView.hasFixedSize();

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            collectionRecyclerView.setLayoutManager(layoutManager);
        }
    }
}
