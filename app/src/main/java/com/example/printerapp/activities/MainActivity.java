package com.example.printerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.printerapp.R;
import com.example.printerapp.fragments.AnalyticsFragment;
import com.example.printerapp.fragments.BaseFragment;
import com.example.printerapp.fragments.OrdersFragment;
import com.example.printerapp.managers.DbManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dbManager = DbManager.getInstance(getApplicationContext());

        BottomNavigationView navbar = findViewById(R.id.navbar);
        navbar.setOnItemSelectedListener(item -> {
            BaseFragment fragmentToRoute = item.getItemId() == R.id.ordersItem?
                    new OrdersFragment() : item.getItemId() == R.id.analyticsItem?
                    new AnalyticsFragment() : null;

            if (fragmentToRoute != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.viewContainer, fragmentToRoute)
                        .commitNow();
            }

            return fragmentToRoute != null;
        });
        navbar.setSelectedItemId(R.id.ordersItem);

        FloatingActionButton createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateActivity.class);
            startActivity(intent);
        });
    }

    public DbManager getDbManager() {
        return dbManager;
    }
}