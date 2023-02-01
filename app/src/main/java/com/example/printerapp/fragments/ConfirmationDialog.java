package com.example.printerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.printerapp.R;
import com.example.printerapp.activities.MainActivity;
import com.example.printerapp.constants.Actions;
import com.example.printerapp.constants.WastesConstants;
import com.example.printerapp.entities.Order;
import com.example.printerapp.entities.Waste;
import com.example.printerapp.managers.DbManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConfirmationDialog extends BaseDialog<Integer> {
    private Order order;
    private Actions action;
    private boolean isCancelled;

    public ConfirmationDialog(Order order, Actions action, List<IUpdatable<Integer>> observers) {
        super(observers);
        this.order = order;
        this.action = action;
        isCancelled = true;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_confirmation, container, false);

        TextView confirmationText = dialogView.findViewById(R.id.confirmationText);
        confirmationText.setText(action == Actions.DELETE_ORDER?
                getString(R.string.delete_confirmation) : getString(R.string.finish_confirmation));

        Button yesButton = dialogView.findViewById(R.id.yesButton);
        Button noButton = dialogView.findViewById(R.id.noButton);

        DbManager dbManager = ((MainActivity) getActivity()).getDbManager();

        yesButton.setOnClickListener(view -> {
            if (action == Actions.FINISH_ORDER || action == Actions.DELETE_ORDER) {
                dbManager.deleteOrder(order.getKey());
                if (action == Actions.FINISH_ORDER) {
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                    Date currentDate = new Date();

                    int currentMonth = Integer.parseInt(monthFormat.format(currentDate));
                    int currentYear = Integer.parseInt(yearFormat.format(currentDate));

                    Waste foundWaste = dbManager.getWasteByDate(currentMonth, currentYear);

                    double electricityAmount = (System.currentTimeMillis() - order.getStartDate())
                            / WastesConstants.MILLISECONDS_IN_HOUR
                            * WastesConstants.PRINTER_ELECTRICITY_AMOUNT;
                    double resourceAmount = order.getAmount() * order.getSize();

                    electricityAmount += foundWaste != null? foundWaste.getElectricityAmount() : 0;
                    resourceAmount += foundWaste != null? foundWaste.getResourceAmount() : 0;

                    Waste newWaste = new Waste(
                            foundWaste.getKey() != 0? foundWaste.getKey() : 0,
                            currentMonth,
                            currentYear,
                            electricityAmount,
                            resourceAmount
                    );

                    if (foundWaste.getKey() != 0) {
                        dbManager.editWaste(newWaste);
                    }
                    else {
                        dbManager.addWaste(newWaste);
                    }
                }

                isCancelled = false;
                onDestroy();
            }
        });
        noButton.setOnClickListener(view -> {
            if (action == Actions.FINISH_ORDER || action == Actions.DELETE_ORDER) {
                onDestroy();
            }
        });

        return dialogView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        observers.forEach(observer -> observer
                .updateView(isCancelled? null : order, Actions.UPDATE_VIEW));
        dismiss();
    }
}
