package com.example.playstore;

public class TodayData {
    private String imageReference;
    private String tag;
    private String title;
    private String footer;

    public TodayData(String imageReference, String tag, String title, String footer) {
        this.imageReference = imageReference;
        this.tag = tag;
        this.title = title;
        this.footer = footer;
    }

    public TodayData() {
    }

    public String getImageReference() {
        return imageReference;
    }

    public void setImageReference(String imageReference) {
        this.imageReference = imageReference;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }
}
