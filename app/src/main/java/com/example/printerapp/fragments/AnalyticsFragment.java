package com.example.printerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.printerapp.R;
import com.example.printerapp.activities.MainActivity;
import com.example.printerapp.adapters.WastesListAdapter;
import com.example.printerapp.entities.Waste;
import com.example.printerapp.managers.DbManager;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnalyticsFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_analytics, container, false);

        DbManager dbManager = ((MainActivity) getActivity()).getDbManager();

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        Date currentDate = new Date();

        int currentMonth = Integer.parseInt(monthFormat.format(currentDate));
        int currentYear = Integer.parseInt(yearFormat.format(currentDate));

        Waste currentWaste = dbManager.getWasteByDate(currentMonth, currentYear);

        ((TextView) fragmentView.findViewById(R.id.electricityAmountText))
                .setText(String.format("%.2f", currentWaste.getElectricityAmount()));
        ((TextView) fragmentView.findViewById(R.id.resourceAmountText))
                .setText(String.format("%.2f", currentWaste.getResourceAmount()));

        WastesListAdapter wastesListAdapter = new WastesListAdapter(
                getContext(),
                currentWaste,
                dbManager.getPastWastes(currentMonth, currentYear)
        );

        RecyclerView wastesList = fragmentView.findViewById(R.id.wastesList);
        wastesList.setHasFixedSize(false);
        wastesList.setLayoutManager(new LinearLayoutManager(getContext()));
        wastesList.setAdapter(wastesListAdapter);
        wastesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                BottomAppBar bottomAppBar = getActivity()
                        .findViewById(R.id.bottomAppBar);
                FloatingActionButton fab = getActivity()
                        .findViewById(R.id.createButton);

                bottomAppBar.animate()
                        .translationY(dy > 0 ? bottomAppBar.getHeight() : 0)
                        .setDuration(300);
                fab.animate()
                        .translationX(dy > 0 ? bottomAppBar.getWidth() : 0)
                        .setDuration(300);
            }
        });

        if (wastesListAdapter.getItemCount() != 0) {
            wastesList.setVisibility(View.VISIBLE);
            fragmentView.findViewById(R.id.compareWastesText).setVisibility(View.VISIBLE);
            fragmentView.findViewById(R.id.notFoundWastesText).setVisibility(View.INVISIBLE);
        }
        else {
            wastesList.setVisibility(View.INVISIBLE);
            fragmentView.findViewById(R.id.compareWastesText).setVisibility(View.INVISIBLE);
            fragmentView.findViewById(R.id.notFoundWastesText).setVisibility(View.VISIBLE);
        }

        return fragmentView;
    }
}
