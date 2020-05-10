package com.example.imagepicker;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Images implements Parcelable {

    // this is the class for holding the image data
    // imageUri is the reference to the image


    private Uri imageUri;
    private long imageId;
    private String folderName;
    private String imageName;

    public Images(Uri imageUri, String folderName, String imageName,long imageId) {
        this.imageUri = imageUri;
        this.folderName = folderName;
        this.imageName = imageName;
        this.imageId =imageId;
    }

    protected Images(Parcel in) {
        imageUri = Uri.parse(in.readString());
        folderName = in.readString();
        imageName = in.readString();
        imageId = in.readLong();
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public static final Creator<Images> CREATOR = new Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel in) {
            return new Images(in);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUri.toString());
        dest.writeString(folderName);
        dest.writeString(imageName);
        dest.writeLong(imageId);
    }
}
