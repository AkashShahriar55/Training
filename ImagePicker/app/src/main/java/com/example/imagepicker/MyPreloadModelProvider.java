package com.example.imagepicker;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPreloadModelProvider implements ListPreloader.PreloadModelProvider {

    /*** this class is not using for this application***/

    // this class will help to pre load the images ahead of the user current screen images
    // glide provide this ListPreloader.PreloadModelProvider to pre load the images

    private ArrayList<Images> images;
    private Context context;
    private int width,height;

    public MyPreloadModelProvider(ArrayList<Images> images, Context context, int width, int height) {
        this.images = images;
        this.context = context;
        this.width = width; // width of the image to be pre loaded
        this.height = height; // height of the image to be pre loaded
    }

    @NonNull
    @Override
    public List getPreloadItems(int position) {
        Uri uri = images.get(position).getImageUri();
        return Collections.singletonList(uri);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Object item) {
        // Returns a List of models that need to be loaded for the list to display adapter items in positions between start and end.
        // the image configuration must be same so that the other Glide in MyImageAdapter can use this images from the cache
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop();

        return Glide.with(context)
                .load(item.toString())
                .apply(requestOptions)
                .thumbnail(0.25f)
                .override(width,height);
    }
}
