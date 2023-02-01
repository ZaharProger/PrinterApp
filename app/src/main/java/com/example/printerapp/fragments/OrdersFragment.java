package com.example.printerapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.printerapp.R;
import com.example.printerapp.activities.CreateActivity;
import com.example.printerapp.activities.MainActivity;
import com.example.printerapp.adapters.CustomersListAdapter;
import com.example.printerapp.adapters.OrdersListAdapter;
import com.example.printerapp.constants.Actions;
import com.example.printerapp.constants.IntentValues;
import com.example.printerapp.entities.BaseEntity;
import com.example.printerapp.entities.Order;
import com.example.printerapp.managers.DbManager;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class OrdersFragment extends BaseFragment implements IUpdatable<Integer> {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_orders, container, false);

        DbManager dbManager = ((MainActivity) getActivity()).getDbManager();

        RecyclerView ordersList = fragmentView.findViewById(R.id.ordersList);
        ordersList.setHasFixedSize(false);
        ordersList.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersList.setAdapter(new OrdersListAdapter(getContext(),
                dbManager.getOrders(), Arrays.asList(this)));
        ordersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        ImageButton sortButton = fragmentView.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(view -> {
            OrdersListAdapter ordersListAdapter = (OrdersListAdapter) ordersList.getAdapter();
            ordersListAdapter.setSortAsc(!ordersListAdapter.isSortAsc());
            ordersListAdapter.sortOrders();

            sortButton.setImageResource(ordersListAdapter.isSortAsc()? R.drawable.ic_sort_asc :
                    R.drawable.ic_sort_desc);
        });

        Spinner customersList = fragmentView.findViewById(R.id.customersList);
        CustomersListAdapter customersListAdapter = new CustomersListAdapter(getContext(),
                R.layout.customers_list_item, dbManager.getCustomers());
        customersListAdapter.setDropDownViewResource(R.layout.customers_list_dropdown);
        customersListAdapter.notifyDataSetChanged();
        customersList.setAdapter(customersListAdapter);
        customersList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner customersList = fragmentView.findViewById(R.id.customersList);

                ArrayList<Order> filteredOrders = dbManager.filterOrders(customersListAdapter
                        .getItemByIndex(customersList.getSelectedItemPosition()));
                ((OrdersListAdapter) ordersList.getAdapter()).setOrders(filteredOrders);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateView(null, Actions.UPDATE_VIEW);

        return fragmentView;
    }

    @Override
    public void updateView(BaseEntity<Integer> relatedEntity, Actions relatedAction) {
        TextView notFoundText = fragmentView.findViewById(R.id.notFoundText);
        Spinner customersList = fragmentView.findViewById(R.id.customersList);
        RecyclerView ordersList = fragmentView.findViewById(R.id.ordersList);

        boolean isOrder = relatedEntity instanceof Order;

        switch (relatedAction) {
            case DELETE_ORDER:
            case FINISH_ORDER:
                if (isOrder) {
                    new ConfirmationDialog((Order) relatedEntity, relatedAction, Arrays.asList(this))
                            .show(getParentFragmentManager(), relatedAction.getStringValue());
                }
                break;
            case EDIT_ORDER:
                if (isOrder) {
                    Intent intent = new Intent(getActivity(), CreateActivity.class);
                    intent.putExtra(IntentValues.ORDER.getStringValue(), (Order) relatedEntity);

                    startActivity(intent);
                }
                break;
            case UPDATE_VIEW:
                DbManager dbManager = ((MainActivity) getActivity()).getDbManager();

                if (isOrder) {
                    CustomersListAdapter customersListAdapter = new CustomersListAdapter(getContext(),
                            R.layout.customers_list_item,
                            dbManager.getCustomers()
                    );

                    customersListAdapter.setDropDownViewResource(R.layout.customers_list_dropdown);
                    customersListAdapter.notifyDataSetChanged();
                    customersList.setAdapter(customersListAdapter);
                }

                if (dbManager.getOrders().size() != 0) {
                    ordersList.setVisibility(View.VISIBLE);
                    notFoundText.setVisibility(View.INVISIBLE);
                }
                else {
                    ordersList.setVisibility(View.INVISIBLE);
                    notFoundText.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
