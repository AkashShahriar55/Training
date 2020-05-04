package com.example.imagepicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyFolderAdapter extends RecyclerView.Adapter<MyFolderAdapter.MyFolderViewHolder> {

    // this adapter will populate the folder names on the upper side of the view

    private Context mContext;
    private ArrayList<String> folders;
    private folderClickListener listener;
    private int pressedButtonIndex = 0; // this will help to control the ui changes of clicked button

    public MyFolderAdapter(Context mContext, ArrayList<String> folders,folderClickListener listener) {
        this.mContext = mContext;
        this.folders = folders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.folder_item,parent,false);

        return new MyFolderAdapter.MyFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyFolderViewHolder holder, final int position) {
        final String folder = folders.get(position);

        holder.button.setText(folder);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click the folder button set the pressed button index to toggle it to pressed state
                // call the interface listener's folderClicked method so that the image data will change along with the folder button click
                pressedButtonIndex = position;
                notifyDataSetChanged();
                listener.folderClicked(position);
            }
        });

        // change the UI of pressed and not pressed button using the pressedButtonIndex
        if(position == pressedButtonIndex){
            holder.button.setBackgroundResource(R.drawable.button_press);
            holder.button.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.button.setBackgroundResource(R.drawable.button_not_pressed);
            holder.button.setTextColor(Color.parseColor("#8E8E93"));

        }
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public static class MyFolderViewHolder extends RecyclerView.ViewHolder{

        Button button;

        public MyFolderViewHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.folderButton);
        }
    }

    interface folderClickListener{
        //this interface communicate with the PickerActivity so that the images can be filtered
        // this will trigger a method in PickerActivity that will filter the images in the MyImageAdapter
        void folderClicked(int folderSelected);

    }


}
