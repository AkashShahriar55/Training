package com.example.playstore.ui.apps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playstore.CollectionAdapter;
import com.example.playstore.CollectionData;
import com.example.playstore.MyReader;
import com.example.playstore.R;
import com.example.playstore.AppData;

import java.util.ArrayList;
import java.util.List;

public class AppsFragment extends Fragment {

    private AppsViewModel appsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_apps, container, false);
        
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initializeCollectionView(view);
    }

    private void initializeCollectionView(View view) {

        RecyclerView appCollectionRV = view.findViewById(R.id.collection_holder);
        //appCollectionRV.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        appCollectionRV.setLayoutManager(layoutManager);

        List<CollectionData> CollectionList = new MyReader(getContext()).readAppsData();

        CollectionAdapter adapter = new CollectionAdapter(getContext(),CollectionList);
        appCollectionRV.setAdapter(adapter);

    }
}
