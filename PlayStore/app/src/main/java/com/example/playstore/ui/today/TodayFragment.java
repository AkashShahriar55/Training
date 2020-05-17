package com.example.playstore.ui.today;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playstore.MyReader;
import com.example.playstore.R;
import com.example.playstore.TodayCardAdapter;
import com.example.playstore.TodayData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodayFragment extends Fragment {

    private TodayViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView today_date = view.findViewById(R.id.today_date);
        today_date.setText(getCurrentDaye());

        initializeRecyclerView(view);

    }

    private void initializeRecyclerView(View view) {
        RecyclerView todayRV = view.findViewById(R.id.today_recyclerView);
        todayRV.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        todayRV.setLayoutManager(layoutManager);
        todayRV.setNestedScrollingEnabled(false);


        MyReader reader = new MyReader(getContext());
        List<TodayData> dataList = reader.readTodayData();

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
