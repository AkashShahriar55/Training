package com.example.android.hellosharedprefs;

public class MyObject {
    // Current count
    private int mCount = 0;
    // Current background color
    private int mColor;

    public MyObject(int mCount, int mColor) {
        this.mCount = mCount;
        this.mColor = mColor;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }
}
