package com.example.playstore.ui.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.playstore.R;

public class GamesFragment extends Fragment {

    private GamesViewModel gamesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_games, container, false);

        /***
        gamesViewModel =
                ViewModelProviders.of(this).get(GamesViewModel.class);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        gamesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        ***/


        return root;
    }


    public static final int FOR_YOU = 1;
    public static final int TOP_CHARTS = 2;

    private int selectedFragment = FOR_YOU;

    Button forYouBtn;
    Button topChartsBtn ;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        forYouBtn = view.findViewById(R.id.for_you_button);
        topChartsBtn = view.findViewById(R.id.top_charts_button);

        selectedFragment = FOR_YOU;
        toggleButton(FOR_YOU);
        changeFragment(new ForYouFragment());

        forYouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFragment != FOR_YOU){
                    selectedFragment = FOR_YOU;
                    toggleButton(FOR_YOU);
                    changeFragment(new ForYouFragment());
                }

            }
        });

        topChartsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedFragment != TOP_CHARTS){
                    changeFragment(new TopChartsFragment());
                    selectedFragment = TOP_CHARTS;
                    toggleButton(TOP_CHARTS);
                }
            }
        });
    }

    void changeFragment(Fragment newFragment){
        // Create new fragment and transaction
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.games_fragment, newFragment);

        // Commit the transaction
        transaction.commit();

    }

    void toggleButton(int selectedFragment){
        if(selectedFragment == FOR_YOU){
            forYouBtn.setBackgroundResource(R.drawable.button_selected);
            forYouBtn.setTextColor(getResources().getColor(R.color.colorWhite));
            topChartsBtn.setBackgroundResource(R.drawable.button_not_selected);
            topChartsBtn.setTextColor(getResources().getColor(R.color.colorTitle));
        }else if(selectedFragment == TOP_CHARTS){
            topChartsBtn.setBackgroundResource(R.drawable.button_selected);
            topChartsBtn.setTextColor(getResources().getColor(R.color.colorWhite));
            forYouBtn.setBackgroundResource(R.drawable.button_not_selected);
            forYouBtn.setTextColor(getResources().getColor(R.color.colorTitle));
        }
    }
}
