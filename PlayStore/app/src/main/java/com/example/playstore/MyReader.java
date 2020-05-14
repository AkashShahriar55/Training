package com.example.playstore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyReader {

    private static final String TAG = "MyReader";
    public static final String SECTION_FOR_YOU = "ForYou";
    public static final String SECTION_TOP_CHARTS = "TopCharts";
    private String fileName;
    private Context context;
    public MyReader(Context context) {
        fileName = "data.json";
        this.context = context;
    }

    public List<TodayData> readTodayData(){
        try {
            String json = readJsonFromAssets(fileName);
            JSONObject mainObj = new JSONObject(json);
            JSONArray todayArray = mainObj.getJSONArray("Today");
            List<TodayData> dataList = new ArrayList<>();
            for (int i = 0; i < todayArray.length(); i++) {
                JSONObject itemData = todayArray.getJSONObject(i);
                String imageRef = itemData.getString("Image");
                String tag = itemData.getString("Tag");
                String title = itemData.getString("Title");
                String footer = itemData.getString("Footer");

                dataList.add(new TodayData(imageRef,tag,title,footer));
            }

            return dataList;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<CollectionData> readGamesData(String section){
        try {
            String json = readJsonFromAssets(fileName);
            JSONObject mainObj = new JSONObject(json);
            JSONObject gamesObj = mainObj.getJSONObject("Games");
            JSONArray forYouArray = gamesObj.getJSONArray(section);
            return getCollectionData(forYouArray);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CollectionData> readAppsData(){
        try {
            String json = readJsonFromAssets(fileName);
            JSONObject mainObj = new JSONObject(json);
            JSONArray appsArray = mainObj.getJSONArray("Apps");
            return getCollectionData(appsArray);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<CollectionData> getCollectionData(JSONArray array) throws JSONException {
        List<CollectionData> dataList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject itemObj = array.getJSONObject(i);
            String title = "no_title";
            if(!itemObj.isNull("Title"))
                title = itemObj.getString("Title");
            String type = itemObj.getString("Type");
            List<AppData> appData = getAppData(itemObj);

            dataList.add(new CollectionData(title,appData,type));
        }

        return dataList;
    }

    private List<AppData> getAppData(JSONObject itemObj) throws JSONException {
        JSONArray appDataArray = itemObj.getJSONArray("AppData");
        List<AppData> appData = new ArrayList<>();
        for (int i = 0; i < appDataArray.length(); i++) {
            JSONObject dataObj = appDataArray.getJSONObject(i);
            String banner = dataObj.getString("Banner");
            String tag = dataObj.getString("Tag");
            String title = dataObj.getString("Title");
            String logo = dataObj.getString("Logo");
            String appName = dataObj.getString("AppName");
            String appSize = dataObj.getString("AppSize");

            appData.add(new AppData(banner,tag,title,logo,appName,appSize));
        }

        return appData;
    }


    private String readJsonFromAssets(String fileName) throws IOException {
        String json = null;
        InputStream is = context.getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");
        return json;
    }

    public Bitmap readImageFromAssets(String imageName){
        try {
            InputStream is = context.getAssets().open(imageName);
            Bitmap bitmapImage = BitmapFactory.decodeStream(is);
            return bitmapImage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
