package com.example.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.MyViewHolder> implements Filterable {
    // this adapter will populate the picker gallery with the images retrieved from users phone
    // Adapter implements Filterable so that the images can be filtered using folder name

    private static final String TAG = "MyImageAdapter";

    private HandlerThread imageLoaderThread;
    private Handler threadHandler;

    public Handler getThreadHandler() {
        return threadHandler;
    }

    private Context mContext; //context of the calling class
    private ArrayList<Images> images; // list of all images
    private ArrayList<Images> itemListFiltered; //list of filtered images according to the folder name
    private int imageHolderDim; // This is the dimension of cardview with respect to the screen size

    MyImageAdapter(Context mContext, ArrayList<Images> images, int cardDim) {
        this.mContext = mContext;
        this.images = images;
        this.itemListFiltered = images;
        this.imageHolderDim = cardDim;

        imageLoaderThread  = new HandlerThread("ImageHandler");
        imageLoaderThread.start();
        threadHandler = new Handler(imageLoaderThread.getLooper());
    }

    public void setImageHolderDim(int imageHolderDim) {
        //this will set the dimension while change in an orientation ( landscape-portrait)
        this.imageHolderDim = imageHolderDim;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.image_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Images image = itemListFiltered.get(position);

        // the card View dimension is programmatically changed according to the screen size
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.imageHolder.getLayoutParams();
        params.width = imageHolderDim;
        params.height = imageHolderDim;
        holder.imageHolder.setLayoutParams(params);


        threadHandler.post(new LoadImageRunnable(this,holder,image));

        /***
        // the request options ensure that it doesn't save any cache and center crop the image
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop();

        //the image is being loaded here using glide.
        // the size has been override by 300*300 for the picker view
        // thumbnail is used fraction of 0.25 of the main image so that it load faster
        Glide.with(mContext)
                .load(image.getImageUri())
                .apply(requestOptions)
                .override(300,300)
                .thumbnail(0.25f)
                .into(holder.imageView);
        ***/
        // here if the user click on any image holder it will intent to imageActivity with parcelable instance of Images
        holder.imageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ImageActivity.class);
                intent.putExtra("Data",image);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        holder.imageView.setImageBitmap(null);
        holder.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Filter getFilter() {
        // this method will filter the image according to the folder name
        // it will search from the images list of all photos and copy only the images containing desired folder into itemListFiltered

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String folderNameSearch = constraint.toString(); // the folder name
                ArrayList<Images> filteredList = new ArrayList<>(); // the filteredResult
                // if the folder name is All photos then it will return all the images in the list
                //otherwise only copy the images having the desired folder name
                if(folderNameSearch.isEmpty() || folderNameSearch.contains("All Photos")){
                    filteredList = images; // copy all images to filtered list
                }
                else{
                    // search the images having the desired folder name and copy it to a new list
                    for(Images row : images){
                        if(row.getFolderName().contains(folderNameSearch))
                            filteredList.add(row);
                    }
                }
                // return the filter result for publish
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                //filtered result has published and here the itemListFiltered will be assigned with the new list
                // and notifyDataSetChanged
                itemListFiltered = (ArrayList<Images>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        CardView imageHolder;
        ProgressBar progressBar;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.pickerimage);
            imageHolder = itemView.findViewById(R.id.my_card_view);
            progressBar = itemView.findViewById(R.id.cardProgressBar);

        }
    }



    private static class LoadImageRunnable implements Runnable{
        WeakReference<MyImageAdapter>  adapterWeakReference;
        WeakReference<MyViewHolder>  holderWeakReference;
        WeakReference<Images> imagesWeakReference;

        LoadImageRunnable(MyImageAdapter adapter, MyViewHolder holder, Images image) {
            this.adapterWeakReference = new WeakReference<>(adapter);
            this.holderWeakReference = new WeakReference<>(holder);
            this.imagesWeakReference = new WeakReference<>(image);
        }

        @Override
        public void run() {
            // the request options ensure that it doesn't save any cache and center crop the image

            Uri uri = imagesWeakReference.get().getImageUri();
            Bitmap bitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                    adapterWeakReference.get().mContext.getContentResolver(), imagesWeakReference.get().getImageId(),
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    options );

            Handler mainHandler = new Handler(Looper.getMainLooper());

            final Bitmap finalBitmap = bitmap;
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(holderWeakReference.get() != null){
                        holderWeakReference.get().progressBar.setVisibility(View.GONE);
                        holderWeakReference.get().imageView.setImageBitmap(finalBitmap);
                    }

                }
            });

        }
    }
}
