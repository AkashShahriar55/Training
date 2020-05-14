package com.example.playstore;

import android.os.Parcel;
import android.os.Parcelable;

public class AppData implements Parcelable {
    private String bannerReference;
    private String tag;
    private String title;
    private String logoReference;
    private String appName;
    private String appSize;

    public AppData(String bannerReference, String tag, String title, String logoReference, String appName, String appSize) {
        this.bannerReference = bannerReference;
        this.tag = tag;
        this.title = title;
        this.logoReference = logoReference;
        this.appName = appName;
        this.appSize = appSize;
    }

    protected AppData(Parcel in) {
        bannerReference = in.readString();
        tag = in.readString();
        title = in.readString();
        logoReference = in.readString();
        appName = in.readString();
        appSize = in.readString();
    }

    public static final Creator<AppData> CREATOR = new Creator<AppData>() {
        @Override
        public AppData createFromParcel(Parcel in) {
            return new AppData(in);
        }

        @Override
        public AppData[] newArray(int size) {
            return new AppData[size];
        }
    };

    public String getBannerReference() {
        return bannerReference;
    }

    public void setBannerReference(String bannerReference) {
        this.bannerReference = bannerReference;
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

    public String getLogoReference() {
        return logoReference;
    }

    public void setLogoReference(String logoReference) {
        this.logoReference = logoReference;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(bannerReference);
            dest.writeString(tag);
            dest.writeString(title);
            dest.writeString(logoReference);
            dest.writeString(appName);
            dest.writeString(appSize);
    }
}
