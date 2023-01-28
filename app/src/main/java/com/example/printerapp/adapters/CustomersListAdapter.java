package com.example.printerapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.printerapp.R;
import com.example.printerapp.entities.BaseEntity;
import com.example.printerapp.entities.Customer;

import java.util.ArrayList;

public class CustomersListAdapter extends BaseSpinnerAdapter<BaseEntity<String>> {

    public CustomersListAdapter(@NonNull Context context, int viewId,
                                ArrayList<Customer> customers) {
        super(context, viewId, mapData(customers));

        this.viewId = viewId;
        items = new ArrayList<>();
        fillItems(customers);
    }

    private void fillItems(ArrayList<Customer> customers) {
        items.clear();
        for (int i = 0; i < customers.size() + 1; ++i) {
            BaseEntity<String> filter = new BaseEntity<>();
            filter.setKey(i == 0 ? "Все" : customers.get(i - 1).getName());
            items.add(filter);
        }
    }

    private static String[] mapData(ArrayList<Customer> customers) {
        String[] mappedData = new String[customers.size() + 1];

        for (int i = 0; i < mappedData.length; ++i) {
            BaseEntity<String> filter = new BaseEntity<>();
            filter.setKey(i == 0 ? "Все" : customers.get(i - 1).getName());
            mappedData[i] = filter.getKey();
        }

        return mappedData;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        fillItems(customers);
        clear();
        addAll(mapData(customers));

        notifyDataSetChanged();
    }

    @Override
    public BaseEntity<String> getItemByIndex(int index) {
        return index == 0? null : super.getItemByIndex(index);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(getContext(), viewId, null);

            TextView customerNameText = convertView.findViewById(R.id.customerNameText);
            BaseEntity<String> filter = items.get(position);

            customerNameText.setText(filter.getKey());
        }

        return convertView;
    }
}
