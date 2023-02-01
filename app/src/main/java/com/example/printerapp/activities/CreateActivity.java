package com.example.printerapp.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
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
import com.example.printerapp.constants.ValidationTypes;
import com.example.printerapp.entities.BaseEntity;
import com.example.printerapp.entities.Customer;
import com.example.printerapp.entities.Order;
import com.example.printerapp.entities.Resource;
import com.example.printerapp.fragments.DatePickerDialog;
import com.example.printerapp.fragments.IUpdatable;
import com.example.printerapp.managers.DbManager;
import com.example.printerapp.managers.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CreateActivity extends AppCompatActivity implements IUpdatable<String> {
    private DbManager dbManager;
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private long editedOrderStartDate;
    private int editedOrderKey;
    private boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        isEditing = false;

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

        resourcesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updatePriceField();
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
                String currentText = amountField.getText().toString();

                if (!isEditing && !currentText.isEmpty()) {
                    isEditing = true;

                    String lastSymbol = currentText.substring(currentText.length() - 1);

                    amountField.setText(
                            Validator.validate(lastSymbol, ValidationTypes.NUM)? currentText :
                                    currentText.substring(0, currentText.lastIndexOf(lastSymbol))
                    );
                    Selection.setSelection(amountField.getText(), amountField.getText().length());

                    updatePriceField();

                    isEditing = false;
                }
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
                String currentText = sizeField.getText().toString();

                if (!isEditing && !currentText.isEmpty()) {
                    isEditing = true;

                    String lastSymbol = currentText.substring(currentText.length() - 1);

                    sizeField.setText(
                            Validator.validate(lastSymbol, ValidationTypes.FLOAT_NUM)? currentText :
                                    currentText.substring(0, currentText.lastIndexOf(lastSymbol))
                    );
                    Selection.setSelection(sizeField.getText(), sizeField.getText().length());

                    updatePriceField();

                    isEditing = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        customerPhoneField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String currentText = customerPhoneField.getText().toString();

                if (!isEditing && !currentText.isEmpty()) {
                    isEditing = true;

                    if (currentText.length() == 1) {
                        customerPhoneField.setText(String.format("+7%s", currentText));
                    }
                    else {
                        String lastSymbol = currentText.substring(currentText.length() - 1);
                        customerPhoneField.setText(
                                Validator.validate(lastSymbol, ValidationTypes.NUM)? currentText :
                                        currentText.substring(0, currentText.lastIndexOf(lastSymbol))
                        );
                    }

                    Selection.setSelection(customerPhoneField.getText(),
                            customerPhoneField.getText().length());

                    isEditing = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        endDateField.setOnTouchListener((view, motionEvent) -> {
            if (getSupportFragmentManager()
                    .findFragmentByTag(Actions.PICK_DATE.getStringValue()) == null) {

                new DatePickerDialog(Arrays.asList(this))
                        .show(getSupportFragmentManager(), Actions.PICK_DATE.getStringValue());
            }

            return true;
        });

        findViewById(R.id.backButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        Intent receivedIntent = getIntent();
        boolean hasOrder = receivedIntent.hasExtra(IntentValues.ORDER.getStringValue());

        if (hasOrder) {
            Order receivedOrder = receivedIntent
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

    private void updatePriceField() {
        Spinner resourcesList = findViewById(R.id.resourcesList);
        TextView totalPriceField = findViewById(R.id.totalPriceField);

        ResourcesListAdapter listAdapter = (ResourcesListAdapter) resourcesList.getAdapter();
        Resource chosenResource = listAdapter.getItemByIndex(resourcesList
                .getSelectedItemPosition());

        EditText amountField = findViewById(R.id.amountField);
        EditText sizeField = findViewById(R.id.sizeField);

        String amountFieldText = amountField.getText().toString();
        String sizeFieldText = sizeField.getText().toString();

        double amountNum = amountFieldText.isEmpty()? 0.0 : Double.parseDouble(amountFieldText);
        double sizeNum = sizeFieldText.isEmpty()? 0.0 : Double.parseDouble(sizeFieldText);

        double totalPrice = chosenResource.getPrice() * amountNum * sizeNum;

        totalPriceField.setText(String.format("%.1f руб", totalPrice));
    }

    @Override
    public void updateView(BaseEntity<String> relatedEntity, Actions relatedAction) {
        EditText endDateField = findViewById(R.id.endDateField);
        switch (relatedAction) {
            case PICK_DATE:
                endDateField.setText(String.format("%s 00:00", relatedEntity.getKey()));
                break;
            case PICK_TIME:
                endDateField.setText(String.format("%s", relatedEntity.getKey()));
                break;
        }
    }
}
