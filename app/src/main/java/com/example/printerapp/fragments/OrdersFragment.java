package com.example.printerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.printerapp.R;
import com.example.printerapp.activities.MainActivity;
import com.example.printerapp.constants.Routes;
import com.example.printerapp.entities.BaseEntity;

public class OrdersFragment extends BaseFragment implements IUpdatable {

    public OrdersFragment(Routes fragmentViewId) {
        this.fragmentViewId = fragmentViewId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_orders, container, false);

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
