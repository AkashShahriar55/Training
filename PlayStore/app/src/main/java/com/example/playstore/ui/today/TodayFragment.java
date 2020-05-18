package com.example.playstore.ui.today;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playstore.MyReader;
import com.example.playstore.R;
import com.example.playstore.TodayCardAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class TodayFragment extends Fragment {

    private TodayViewModel homeViewModel;
    private View view;

    private ProgressBar loadingProgressBar;
    private List<Object> dataList;
    private AdLoader adLoader;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        TextView today_date = view.findViewById(R.id.today_date);
        today_date.setText(getCurrentDaye());

        loadingProgressBar = view.findViewById(R.id.loading_progress);
        
        //read data from json
        MyReader reader = new MyReader(getContext());
        dataList = reader.readTodayData();


        ConnectivityManager cm =
                (ConnectivityManager)requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            loadNativeAds();
        }else{
            initializeRecyclerView();
        }


        


    }

    private void loadNativeAds() {
        AdLoader.Builder builder = new AdLoader.Builder(requireContext(),getResources().getString(R.string.ads_app_unit));
        adLoader = builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // A native ad loaded successfully, check if the loader has finished loading
                //and if so, insert the ads into the list

                mNativeAds.add(unifiedNativeAd);
                if(!adLoader.isLoading()){
                    insertAdsInDataList();
                    initializeRecyclerView();
                }
            }
        }).withAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                // A native ad failed to load, check if the ad loader has finished loading
                // and if so, insert the ads into the list.
                Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                        + " load another.");
                if (!adLoader.isLoading()) {
                    insertAdsInDataList();
                    initializeRecyclerView();
                }
            }
        }).build();

        // Load the Native Express ad.
        adLoader.loadAds(new AdRequest.Builder().build(),2);

    }

    private void insertAdsInDataList() {
        if (mNativeAds.size() <= 0) {
            return;
        }

        int offset = (dataList.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad: mNativeAds) {
            dataList.add(index, ad);
            index = index + offset;
        }
    }

    private void initializeRecyclerView() {
        loadingProgressBar.setVisibility(View.GONE);
        RecyclerView todayRV = view.findViewById(R.id.today_recyclerView);
        todayRV.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        todayRV.setLayoutManager(layoutManager);
        todayRV.setNestedScrollingEnabled(false);
        

        TodayCardAdapter adapter = new TodayCardAdapter(getContext(),dataList);
        todayRV.setAdapter(adapter);
    }

    private String getCurrentDaye() {
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        String months[] = {"JAN","FEB","MAR","APR","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
        int day = c.get(Calendar.DAY_OF_WEEK);
        String days[] = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
        return days[day-1] + " " + date + " " + (months[month]);
    }
}
