package com.example.printerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.printerapp.R;
import com.example.printerapp.activities.MainActivity;
import com.example.printerapp.adapters.OrdersListAdapter;
import com.example.printerapp.entities.BaseEntity;
import com.example.printerapp.managers.DbManager;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.Arrays;

public class OrdersFragment extends BaseFragment implements IUpdatable {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_orders, container, false);

        DbManager dbManager = ((MainActivity) getActivity()).getDbManager();

        RecyclerView ordersList = fragmentView.findViewById(R.id.ordersList);
        Spinner customersList = fragmentView.findViewById(R.id.customersList);

        ArrayAdapter<String> customersListAdapter = new ArrayAdapter<>(getContext(),
                R.layout.customers_list_item, dbManager.getCustomers());
        customersListAdapter.setDropDownViewResource(R.layout.customers_list_dropdown);
        customersListAdapter.notifyDataSetChanged();
        customersList.setAdapter(customersListAdapter);

        ordersList.setHasFixedSize(false);
        ordersList.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersList.setAdapter(new OrdersListAdapter(getContext(),
                dbManager.getOrders(), Arrays.asList(this)));

        ordersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                BottomAppBar bottomAppBar = ((MainActivity) getActivity())
                        .findViewById(R.id.bottomAppBar);
                bottomAppBar.animate()
                        .translationY(dy > 0 ? bottomAppBar.getHeight() : 0)
                        .setDuration(300);
            }
        });

        updateView(null, null);

        return fragmentView;
    }

    @Override
    public void updateView(BaseEntity<Integer> relatedEntity, View relatedView) {
        Spinner customersList = fragmentView.findViewById(R.id.customersList);
        RecyclerView ordersList = fragmentView.findViewById(R.id.ordersList);
        TextView notFoundText = fragmentView.findViewById(R.id.notFoundText);

        int ordersSize = ((MainActivity) getActivity()).getDbManager().getOrders().size();
        if (ordersSize != 0) {
            customersList.setVisibility(View.VISIBLE);
            ordersList.setVisibility(View.VISIBLE);
            notFoundText.setVisibility(View.INVISIBLE);
        }
        else {
            customersList.setVisibility(View.INVISIBLE);
            ordersList.setVisibility(View.INVISIBLE);
            notFoundText.setVisibility(View.VISIBLE);
        }
    }
}
