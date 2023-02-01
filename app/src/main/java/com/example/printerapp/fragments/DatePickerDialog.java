package com.example.printerapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import com.example.printerapp.R;
import com.example.printerapp.constants.Actions;
import com.example.printerapp.entities.BaseEntity;

import java.util.Calendar;
import java.util.List;

public class DatePickerDialog extends BaseDialog<String>
        implements android.app.DatePickerDialog.OnDateSetListener {

    public DatePickerDialog(List<IUpdatable<String>> observers){
        super(observers);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar c = Calendar.getInstance();

        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        return new android.app.DatePickerDialog(getActivity(), R.style.CustomDateTimePickerTheme, this,
                currentYear, currentMonth, currentDay);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String preparedMonth = (month < 10)? "0" : "";
        preparedMonth += month;
        String dateString = String.format("%d.%s.%d", dayOfMonth, preparedMonth, year);

        BaseEntity<String> dateObject = new BaseEntity<>();
        dateObject.setKey(dateString);

        new TimePickerDialog(observers, dateObject)
                .show(getParentFragmentManager(), Actions.PICK_TIME.getStringValue());
        observers.forEach(observer -> observer.updateView(dateObject, Actions.PICK_DATE));

        dismiss();
    }
}
