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


public class ResourceImageAdapter extends RecyclerView.Adapter<ResourceImageAdapter.MyViewHolder> {
    private static final String TAG = "ResourceImageAdapter";
    HandlerThread handlerThread = new HandlerThread("LoadResourceImage");
    Handler handler;

    public Handler getHandler() {
        return handler;
    }

    private Context mContext; //context of the calling class
    private ArrayList<Integer> resourceIds; // list of all images

    ResourceImageAdapter(Context mContext, ArrayList<Integer> resourceIds) {
        this.mContext = mContext;
        this.resourceIds = resourceIds;

        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
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
        final Integer resourseId = resourceIds.get(position);

        handler.post(new LoadImageRunnable(mContext,holder,resourseId));


    }

    @Override
    public int getItemCount() {
        return resourceIds.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        holder.imageView.setImageBitmap(null);
        holder.progressBar.setVisibility(View.VISIBLE);
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
        WeakReference<Context>  contextWeakReference;
        WeakReference<MyViewHolder>  holderWeakReference;
        int resourseId;

        LoadImageRunnable(Context context,MyViewHolder holder,int resourseId) {
            this.contextWeakReference = new WeakReference<>(context);
            this.holderWeakReference = new WeakReference<>(holder);
            this.resourseId = resourseId;
        }

        @Override
        public void run() {
            // the request options ensure that it doesn't save any cache and center crop the image

            Bitmap bitmap = MyImageDecoder.decodeSampledBitmapFromResource(contextWeakReference.get().getResources(),resourseId,300,300);
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
