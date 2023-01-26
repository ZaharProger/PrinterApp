package com.example.printerapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.printerapp.R;
import com.example.printerapp.activities.MainActivity;
import com.example.printerapp.adapters.ResourcesListAdapter;
import com.example.printerapp.constants.Routes;
import com.example.printerapp.entities.BaseEntity;
import com.example.printerapp.entities.Resource;
import com.example.printerapp.managers.DbManager;

public class CreateFragment extends BaseFragment implements IUpdatable {

    public CreateFragment(Routes fragmentViewId) {
        this.fragmentViewId = fragmentViewId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_create, container, false);

        TextView totalPriceField = fragmentView.findViewById(R.id.totalPriceField);
        EditText amountField = fragmentView.findViewById(R.id.amountField);
        EditText sizeField = fragmentView.findViewById(R.id.sizeField);
        Spinner resourcesList = fragmentView.findViewById(R.id.resourcesList);

        DbManager dbManager = ((MainActivity) getActivity()).getDbManager();
        ResourcesListAdapter listAdapter = new ResourcesListAdapter(getContext(),
                R.layout.resources_list_item, dbManager.getResources());
        listAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        listAdapter.notifyDataSetChanged();

        resourcesList.setAdapter(listAdapter);

        resourcesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateView(listAdapter.getResourceByIndex(resourcesList
                        .getSelectedItemPosition()), totalPriceField);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        amountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateView(listAdapter.getResourceByIndex(resourcesList
                        .getSelectedItemPosition()), totalPriceField);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sizeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateView(listAdapter.getResourceByIndex(resourcesList
                        .getSelectedItemPosition()), totalPriceField);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return fragmentView;
    }

    @Override
    public void updateView(BaseEntity<Integer> relatedEntity, View relatedView) {
        if (relatedEntity instanceof Resource && relatedView instanceof TextView) {
            EditText amountField = fragmentView.findViewById(R.id.amountField);
            EditText sizeField = fragmentView.findViewById(R.id.sizeField);

            String amountFieldText = amountField.getText().toString();
            String sizeFieldText = sizeField.getText().toString();

            double amountNum = amountFieldText.isEmpty()? 0.0 : Double.parseDouble(amountFieldText);
            double sizeNum = sizeFieldText.isEmpty()? 0.0 : Double.parseDouble(sizeFieldText);

            double totalPrice = ((Resource) relatedEntity).getPrice() * amountNum * sizeNum;

            ((TextView) relatedView).setText(String.format("%.1f руб", totalPrice));
        }
    }
}
