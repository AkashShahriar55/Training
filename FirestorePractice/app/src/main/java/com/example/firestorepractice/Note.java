package com.example.firestorepractice;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String no;
    private String id;
    private String title;
    private String note;
    private int priority;


    public Note( String title, String note,int priority) {
        this.title = title;
        this.note = note;
        this.priority = priority;
    }

    public Note() {
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Exclude
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
