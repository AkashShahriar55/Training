package com.example.imagepicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;

import java.util.ArrayList;
import java.util.Arrays;

public class PickerActivity extends AppCompatActivity implements MyFolderAdapter.folderClickListener {


    private ArrayList<String> folders= new ArrayList<String>(); // list of folders
    private MyImageAdapter imageAdapter;
    RecyclerView mRecyclerView;
    ArrayList<Images> images;

    //size of the images for thumbnail
    private final int imageWidthPixels = 300;
    private final int imageHeightPixels = 300;




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        mRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView mFolderRecyclerView = findViewById(R.id.folderRCV);


        // get the orientation so that we can make some ui changes according to it
        int orientation = getResources().getConfiguration().orientation;
        int imageHolderDim = 0;
        // get the size of grid item according to screen size and orientation
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            imageHolderDim = getImageHolderDim(5);
        } else if(orientation == Configuration.ORIENTATION_PORTRAIT){
            imageHolderDim = getImageHolderDim(3);
        }




        folders.add("All Photos");
        images = getImageData(); // get the images data from the phone
        imageAdapter = new MyImageAdapter(this,images,imageHolderDim); // create the image adapter

        /***
         * not using for this application
        ListPreloader.PreloadSizeProvider sizeProvider = new FixedPreloadSizeProvider(imageWidthPixels,imageHeightPixels);
        ListPreloader.PreloadModelProvider modelProvider = new MyPreloadModelProvider(images,this,imageWidthPixels,imageHeightPixels);
        RecyclerViewPreloader<Photo> preloader = new RecyclerViewPreloader<Photo>(Glide.with(this),modelProvider,sizeProvider,10);
        mRecyclerView.addOnScrollListener(preloader);
         ***/

        // as RecyclerView's size is not affected by the adapter contents
        // by setting it to fixed size it can do some optimization
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);


        // set the grid size according to the orientation
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,5));
        }else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }

        mRecyclerView.setAdapter(imageAdapter);


        // set the folder button adapter with horizontal layout
        MyFolderAdapter myFolderAdapter = new MyFolderAdapter(this,folders,this);
        mFolderRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mFolderRecyclerView.setAdapter(myFolderAdapter);



    }

    private int getImageHolderDim(int columnSize) {
        // in this method I calculated the item width*height
        // so that it adapts with the screen size
        // i used displayMetrics to get the screen width
        // then divided it by 3 for portrait as i want to show 3 columns in portrait mode
        // and divided it by 5 for landscape as i want to show 5 columns in landscape mode
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        // I want to give margin of 5 dp at all side so the image size will be
        // (width-10)*(height-10) where height = width
        float margin_dp = 10.0f;

        // Get the screen's density scale
        float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        int margin_px = (int) (margin_dp * scale + 0.5f);

        // the item size will be determined according to the orientation too
        // if landscape = divide by 5 , if portrait = divide by 3
        return width/columnSize-margin_px;
    }


    private ArrayList<Images> getImageData() {
        // here the data of the images fetched using the MediaStore database
        // it is querying the media by it's _id, display name, bucket display name(folder name)
        ArrayList<Images> images = new ArrayList<>();
        Uri allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; // the reference of all images

        // projection is the media store database column to fetch
        String[] projection = new String[]{
          MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        };

        System.out.println(allImagesUri);
        System.out.println(Arrays.toString(projection));

        Cursor cursor = this.getContentResolver().query(allImagesUri, projection, null,null,null);
        try{
            // cursor will go through all the database and will fetch all the information (column)
            if(cursor != null){
                cursor.moveToFirst();
                do{
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    long dataId =  cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,dataId);

                    if(!folders.contains(folder)){
                        // this will add folder names to the folders list so that
                        // the folder buttons can be populated
                        folders.add(folder);
                    }

                    Images image = new Images(imageUri,folder,name);
                    images.add(image);

                }while (cursor.moveToNext());
                cursor.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return images;
    }



    public void closePicker(View view) {
        finish();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        // here some changes with Image Holder Dimension and grid span count has done
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.getLayoutManager().removeAllViews();
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,5));
            imageAdapter.setImageHolderDim(getImageHolderDim(5));
            imageAdapter.notifyDataSetChanged();


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.getLayoutManager().removeAllViews();
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
            imageAdapter.setImageHolderDim(getImageHolderDim(3));
            imageAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void folderClicked(int folderSelected) {
        // the folder buttons was clicked
        // it will filter the imageAdapter using getFilter().filter(folder_name) method
        // and also scroll to the start
        imageAdapter.getFilter().filter(folders.get(folderSelected));
        mRecyclerView.scrollToPosition(0);
    }
}
