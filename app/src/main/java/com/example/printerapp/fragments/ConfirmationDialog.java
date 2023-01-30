package com.example.printerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.printerapp.R;
import com.example.printerapp.activities.MainActivity;
import com.example.printerapp.constants.Actions;
import com.example.printerapp.entities.Order;
import com.example.printerapp.managers.DbManager;

import java.util.List;

public class ConfirmationDialog extends DialogFragment {
    private Order order;
    private Actions action;
    private List<IUpdatable> observers;
    private boolean isCancelled;

    public ConfirmationDialog(Order order, Actions action, List<IUpdatable> observers) {
        this.order = order;
        this.action = action;
        this.observers = observers;
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
            switch (action) {
                case DELETE_ORDER:
                    dbManager.deleteOrder(order.getKey());
                    isCancelled = false;
                    onDestroy();
                    break;
            }
        });
        noButton.setOnClickListener(view -> {
            switch (action) {
                case DELETE_ORDER:
                    onDestroy();
                    break;
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
