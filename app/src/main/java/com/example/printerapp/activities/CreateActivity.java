package com.example.printerapp.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.printerapp.R;
import com.example.printerapp.activities.MainActivity;
import com.example.printerapp.adapters.ResourcesListAdapter;
import com.example.printerapp.entities.BaseEntity;
import com.example.printerapp.entities.Resource;
import com.example.printerapp.fragments.IUpdatable;
import com.example.printerapp.managers.DbManager;

public class CreateActivity extends AppCompatActivity implements IUpdatable, TextWatcher,
        AdapterView.OnItemSelectedListener {
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        EditText amountField = findViewById(R.id.amountField);
        EditText sizeField = findViewById(R.id.sizeField);
        Spinner resourcesList = findViewById(R.id.resourcesList);

        dbManager = DbManager.getInstance(getApplicationContext());
        ResourcesListAdapter listAdapter = new ResourcesListAdapter(getApplicationContext(),
                R.layout.resources_list_item, dbManager.getResources());
        listAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        listAdapter.notifyDataSetChanged();

        resourcesList.setAdapter(listAdapter);

        resourcesList.setOnItemSelectedListener(this);
        amountField.addTextChangedListener(this);
        sizeField.addTextChangedListener(this);

        ((ImageButton) findViewById(R.id.backButton)).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void updateView(BaseEntity<Integer> relatedEntity, View relatedView) {
        if (relatedEntity instanceof Resource && relatedView instanceof TextView) {
            EditText amountField = findViewById(R.id.amountField);
            EditText sizeField = findViewById(R.id.sizeField);

            String amountFieldText = amountField.getText().toString();
            String sizeFieldText = sizeField.getText().toString();

            double amountNum = amountFieldText.isEmpty()? 0.0 : Double.parseDouble(amountFieldText);
            double sizeNum = sizeFieldText.isEmpty()? 0.0 : Double.parseDouble(sizeFieldText);

            double totalPrice = ((Resource) relatedEntity).getPrice() * amountNum * sizeNum;

            ((TextView) relatedView).setText(String.format("%.1f руб", totalPrice));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Spinner resourcesList = findViewById(R.id.resourcesList);
        TextView totalPriceField = findViewById(R.id.totalPriceField);

        ResourcesListAdapter listAdapter = (ResourcesListAdapter) resourcesList.getAdapter();
        updateView(listAdapter.getResourceByIndex(resourcesList
                .getSelectedItemPosition()), totalPriceField);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner resourcesList = findViewById(R.id.resourcesList);
        TextView totalPriceField = findViewById(R.id.totalPriceField);

        ResourcesListAdapter listAdapter = (ResourcesListAdapter) resourcesList.getAdapter();
        updateView(listAdapter.getResourceByIndex(resourcesList
                .getSelectedItemPosition()), totalPriceField);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
