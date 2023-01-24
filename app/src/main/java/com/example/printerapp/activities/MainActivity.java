package com.example.printerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.printerapp.R;
import com.example.printerapp.constants.Routes;
import com.example.printerapp.fragments.AnalyticsFragment;
import com.example.printerapp.fragments.BaseFragment;
import com.example.printerapp.fragments.CreateFragment;
import com.example.printerapp.fragments.OrdersFragment;
import com.example.printerapp.managers.DbManager;
import com.example.printerapp.managers.Router;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Router router;
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dbManager = new DbManager(getApplicationContext());

        List<BaseFragment> fragments = Arrays.asList(
                new OrdersFragment(Routes.ORDERS),
                new AnalyticsFragment(Routes.ANALYTICS),
                new CreateFragment(Routes.CREATE)
        );
        router = Router.getInstance(this, R.id.viewContainer, fragments);

        BottomNavigationView navbar = findViewById(R.id.navbar);
        navbar.setOnItemSelectedListener(item -> {
            Routes route = item.getItemId() == R.id.ordersItem? Routes.ORDERS :
                    item.getItemId() == R.id.analyticsItem? Routes.ANALYTICS : null;

            return route != null && router.route(route);
        });
        navbar.setSelectedItemId(R.id.ordersItem);

        FloatingActionButton createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> router.route(Routes.CREATE));
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public Router getRouter() {
        return router;
    }
}