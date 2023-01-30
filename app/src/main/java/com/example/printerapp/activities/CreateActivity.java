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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.printerapp.R;
import com.example.printerapp.adapters.ResourcesListAdapter;
import com.example.printerapp.constants.Actions;
import com.example.printerapp.constants.IntentValues;
import com.example.printerapp.entities.BaseEntity;
import com.example.printerapp.entities.Customer;
import com.example.printerapp.entities.Order;
import com.example.printerapp.entities.Resource;
import com.example.printerapp.fragments.IUpdatable;
import com.example.printerapp.managers.DbManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity implements IUpdatable, TextWatcher,
        AdapterView.OnItemSelectedListener {
    private DbManager dbManager;
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private long editedOrderStartDate;
    private int editedOrderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        EditText nameField = findViewById(R.id.nameField);
        EditText endDateField = findViewById(R.id.endDateField);
        EditText amountField = findViewById(R.id.amountField);
        EditText sizeField = findViewById(R.id.sizeField);
        Spinner resourcesList = findViewById(R.id.resourcesList);
        EditText customerNameField = findViewById(R.id.customerNameField);
        EditText customerPhoneField = findViewById(R.id.customerPhoneField);

        dbManager = DbManager.getInstance(getApplicationContext());
        ResourcesListAdapter listAdapter = new ResourcesListAdapter(getApplicationContext(),
                R.layout.resources_list_item, dbManager.getResources());
        listAdapter.setDropDownViewResource(R.layout.resources_list_dropdown);
        listAdapter.notifyDataSetChanged();

        resourcesList.setAdapter(listAdapter);

        resourcesList.setOnItemSelectedListener(this);
        amountField.addTextChangedListener(this);
        sizeField.addTextChangedListener(this);

        findViewById(R.id.backButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        Intent receivedIntent = getIntent();
        boolean hasOrder = receivedIntent.hasExtra(IntentValues.ORDER.getStringValue());

        if (hasOrder) {
            Order receivedOrder = (Order) receivedIntent
                    .getParcelableExtra(IntentValues.ORDER.getStringValue());

            editedOrderStartDate = receivedOrder.getStartDate();
            editedOrderKey = receivedOrder.getKey();

            nameField.setText(receivedOrder.getName());
            endDateField.setText(format.format(new Date(receivedOrder.getEndDate())));
            amountField.setText(String.format("%s", receivedOrder.getAmount()));
            sizeField.setText(String.format("%s", receivedOrder.getSize()));
            resourcesList.setSelection(receivedOrder.getResource().getKey() - 1);
            customerNameField.setText(receivedOrder.getCustomer().getName());
            customerPhoneField.setText(receivedOrder.getCustomer().getPhone());
        }

        ((TextView) findViewById(R.id.createActivityHeader))
                .setText(getString(hasOrder? R.string.edit_header : R.string.create_header));

        Button addOrderButton = findViewById(R.id.addOrderButton);
        addOrderButton.setText(getString(hasOrder? R.string.save : R.string.create));
        addOrderButton.setOnClickListener(view -> {
            try {
                Date date = format.parse(endDateField.getText().toString());
                Order newOrder = new Order (
                        hasOrder? editedOrderKey : 0,
                        nameField.getText().toString().trim(),
                        Integer.parseInt(amountField.getText().toString().trim()),
                        hasOrder? editedOrderStartDate : System.currentTimeMillis(),
                        date.getTime(),
                        new Customer(
                                0,
                                customerNameField.getText().toString().trim(),
                                customerPhoneField.getText().toString().trim()
                        ),
                        Double.parseDouble(sizeField.getText().toString().trim()),
                        listAdapter.getItemByIndex(resourcesList.getSelectedItemPosition())
                );

                if (hasOrder) {
                    dbManager.editOrder(newOrder);
                }
                else {
                    dbManager.addOrder(newOrder);
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (ParseException ignored) {
            }
        });
    }

    @Override
    public void updateView(BaseEntity<Integer> relatedEntity, Actions relatedAction) {
        Spinner resourcesList = findViewById(R.id.resourcesList);
        TextView totalPriceField = findViewById(R.id.totalPriceField);

        ResourcesListAdapter listAdapter = (ResourcesListAdapter) resourcesList.getAdapter();
        relatedEntity = listAdapter.getItemByIndex(resourcesList
                .getSelectedItemPosition());

        if (relatedEntity != null) {
            EditText amountField = findViewById(R.id.amountField);
            EditText sizeField = findViewById(R.id.sizeField);

            String amountFieldText = amountField.getText().toString();
            String sizeFieldText = sizeField.getText().toString();

            double amountNum = amountFieldText.isEmpty()? 0.0 : Double.parseDouble(amountFieldText);
            double sizeNum = sizeFieldText.isEmpty()? 0.0 : Double.parseDouble(sizeFieldText);

            double totalPrice = ((Resource) relatedEntity).getPrice() * amountNum * sizeNum;

            totalPriceField.setText(String.format("%.1f руб", totalPrice));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        updateView(null, null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        updateView(null, null);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
