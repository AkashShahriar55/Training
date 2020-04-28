/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.hellosharedprefs;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * HelloSharedPrefs is an adaptation of the HelloToast app from chapter 1.
 * It includes:
 * - Buttons for changing the background color.
 * - Maintenance of instance state.
 * - Themes and styles.
 * - Read and write shared preferences for the current count and the color.
 * <p>
 * This is the starter code for HelloSharedPrefs.
 */
public class MainActivity extends AppCompatActivity {
    // Current count
    private int mCount = 0;
    // Current background color
    private int mColor;
    // Text view to display both count and color
    private TextView mShowCountTextView;

    // Key for current count
    private final String COUNT_KEY = "count";
    // Key for current color
    private final String COLOR_KEY = "color";

    private SharedPreferences mPreferences; // member variable to hold a reference of a shared preference
    private String sharedPrefFile = "com.example.android.hellosharedprefs"; // member variable to hold the name of the sharedpref file


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views, color
        mShowCountTextView = findViewById(R.id.count_textview);
        mColor = ContextCompat.getColor(this,
                R.color.default_background);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE); // initialize the shared pref
        //getSharedPreferences() method opens the pref file named 'sharedPrefFile' and with mode 'MODE_PRIVATE'

        // Restore the shared preference data
        //mCount = mPreferences.getInt(COUNT_KEY,0);
        //mColor = mPreferences.getInt(COLOR_KEY,mColor);

        Gson gson = new Gson();
        String objJSON = mPreferences.getString("Object","");

        if(!objJSON.isEmpty()){
            MyObject object = gson.fromJson(objJSON,MyObject.class);

            mCount = object.getmCount();
            mColor = object.getmColor();
        }


        //It don't need to get a shared preference editor for reading
        //getInt() take a key and a default value . it returns saved value if found any
        //otherwise it returns default value in this case,0 for count and mColor for color

        mShowCountTextView.setText(String.format("%s",mCount));
        mShowCountTextView.setBackgroundColor(mColor);
        // show the value in the ui



    }

    /**
     * Handles the onClick for the background color buttons. Gets background
     * color of the button that was clicked, and sets the TextView background
     * to that color.
     *
     * @param view The view (Button) that was clicked.
     */
    public void changeBackground(View view) {
        int color = ((ColorDrawable) view.getBackground()).getColor();
        mShowCountTextView.setBackgroundColor(color);
        mColor = color;
    }

    /**
     * Handles the onClick for the Count button. Increments the value of the
     * mCount global and updates the TextView.
     *
     * @param view The view (Button) that was clicked.
     */
    public void countUp(View view) {
        mCount++;
        mShowCountTextView.setText(String.format("%s", mCount));
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        //SharedPreferences.Editor is needed to edit the shared pref file
        //this class includes multiple "put" methods for different data types

        MyObject object = new MyObject(mCount,mColor);
        Gson gson = new Gson();
        String objJSON = gson.toJson(object);
        preferencesEditor.putString("Object",objJSON);

        //preferencesEditor.putInt(COUNT_KEY,mCount);// saving the count key-value pair
        //preferencesEditor.putInt(COLOR_KEY,mColor);// saving the color key-value pair


        preferencesEditor.apply();
        //this will save the preferences
        //apply() method saves the preferences asynchronously
        //commit() method saves the preferences asynchronously
    }

    /**
     * Saves the instance state if the activity is restarted (for example,
     * on device rotation.) Here you save the values for the count and the
     * background color.
     *
     * @param outState The state data.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(COUNT_KEY, mCount);
        outState.putInt(COLOR_KEY, mColor);
    }

    /**
     * Handles the onClick for the Reset button. Resets the global count and
     * background variables to the defaults and resets the views to those
     * default values.
     *
     * @param view The view (Button) that was clicked.
     */
    public void reset(View view) {
        // Reset count
        mCount = 0;
        mShowCountTextView.setText(String.format("%s", mCount));

        // Reset color
        mColor = ContextCompat.getColor(this,
                R.color.default_background);
        mShowCountTextView.setBackgroundColor(mColor);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        //this will needed to edit the mPreferences reference
        preferencesEditor.clear();
        // simple idea is to clear the editor
        preferencesEditor.apply();
        //and apply it to save with empty preference
    }
}
