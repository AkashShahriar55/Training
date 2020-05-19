package com.example.playstore;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.playstore.ui.apps.AppsFragment;
import com.example.playstore.ui.games.GamesFragment;
import com.example.playstore.ui.today.TodayFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TODAY = "today";
    private static final String FRAGMENT_GAMES = "games";
    private static final String FRAGMENT_APPS = "apps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        MobileAds.initialize(this);
        MyAdLoader.loadAd(this);

        Fragment fragment = new TodayFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.nav_host_fragment,fragment);
        fragmentTransaction.addToBackStack(FRAGMENT_TODAY);
        fragmentTransaction.commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        showFragment(new TodayFragment(), FRAGMENT_TODAY);
                        return true;
                    case R.id.navigation_dashboard:
                        showFragment(new GamesFragment(), FRAGMENT_GAMES);
                        return true;
                    case R.id.navigation_notifications:
                        showFragment(new AppsFragment(), FRAGMENT_APPS);
                        return true;
                }
                return false;
            }
        });
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean popped = fragmentManager.popBackStackImmediate(tag,0);
        if(!popped){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.nav_host_fragment,fragment);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
