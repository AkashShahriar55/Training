package com.example.playstore;

import java.util.List;

public class CollectionData {
    private String title;
    private List<AppData> appData;
    private String type;

    public CollectionData(String title, List<AppData> appData,String type) {
        this.title = title;
        this.appData = appData;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AppData> getAppData() {
        return appData;
    }

    public void setAppData(List<AppData> appData) {
        this.appData = appData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
