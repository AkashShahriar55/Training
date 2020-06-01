package com.example.firestorepractice;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.local.IndexFreeQueryEngine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context mContext;
    private ArrayList<Note> dataList;
    private clickEventListener listener;

    public NoteAdapter(Context mContext,ArrayList<Note> dataList,clickEventListener listener) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.note_layout,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note data = dataList.get(position);
        data.setNo(String.valueOf(position+1));
        String title = data.getNo()+"."+data.getTitle();
        holder.title.setText(title);
        holder.note.setText(data.getNote());
        String priority = "Priority: "+data.getPriority();
        holder.priority.setText(priority);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemDeleteClicked(data);
            }
        });

        holder.noteHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,data.getId(),Toast.LENGTH_LONG).show();
                listener.itemClicked(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title,note,priority;
        Button deleteBtn;
        ConstraintLayout noteHolder;
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_tv);
            note = itemView.findViewById(R.id.note_tv);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            noteHolder = itemView.findViewById(R.id.note_holder_rv);
            priority = itemView.findViewById(R.id.priority_tv);
        }
    }

    public interface clickEventListener{
        void itemClicked(Note note);
        void itemDeleteClicked(Note note);
    }
}
