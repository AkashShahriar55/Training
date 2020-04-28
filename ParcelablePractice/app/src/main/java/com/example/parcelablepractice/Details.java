package com.example.parcelablepractice;

import android.os.Parcel;
import android.os.Parcelable;

public class Details implements Parcelable {
    private String name;
    private String age;
    private String phone;

    public Details(String name, String age, String phone) {
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    //Constructor used to reading the saved values from the parcel:
    protected Details(Parcel in) {
        name = in.readString();
        age = in.readString();
        phone = in.readString();
    }

    //CREATOR used for unparcelling the parcel (creating the object). This method is to bind everything together
    public static final Creator<Details> CREATOR = new Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //This method returns the hashcode of the object. This method does not do too much.
    @Override
    public int describeContents() {
        return 0;
    }

    //In this method, you need to add all the properties to a parcel which you want to transfer.
    // You use write methods to add each of the properties.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(phone);
    }
}
