package com.example.playstore.ui.games;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.playstore.AppData;
import com.example.playstore.CollectionAdapter;
import com.example.playstore.CollectionData;
import com.example.playstore.MyReader;
import com.example.playstore.R;

import java.util.ArrayList;
import java.util.List;

public class TopChartsFragment extends Fragment {

    private TopChartsViewModel mViewModel;

    public static TopChartsFragment newInstance() {
        return new TopChartsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.top_charts_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView appCollectionRV = view.findViewById(R.id.top_charts_collection_holder);
        //appCollectionRV.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        appCollectionRV.setLayoutManager(layoutManager);

        List<CollectionData> CollectionList = new MyReader(getContext()).readGamesData(MyReader.SECTION_TOP_CHARTS);


        CollectionAdapter adapter = new CollectionAdapter(getContext(),CollectionList);
        appCollectionRV.setAdapter(adapter);
    }
}
