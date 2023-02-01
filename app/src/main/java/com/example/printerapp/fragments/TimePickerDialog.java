package com.example.printerapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.example.printerapp.R;
import com.example.printerapp.constants.Actions;
import com.example.printerapp.entities.BaseEntity;

import java.util.Calendar;
import java.util.List;

public class TimePickerDialog extends BaseDialog<String>
        implements android.app.TimePickerDialog.OnTimeSetListener {

    private BaseEntity<String> date;

    public TimePickerDialog(List<IUpdatable<String>> observers, BaseEntity<String> date) {
        super(observers);
        this.date = date;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new android.app.TimePickerDialog(getActivity(),
                R.style.CustomDateTimePickerTheme, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String preparedHours = (hourOfDay < 10)? "0" : "";
        String preparedMinutes = (minute < 10)? "0" : "";

        preparedHours += hourOfDay;
        preparedMinutes += minute;

        BaseEntity<String> timeObject = new BaseEntity<>();
        timeObject.setKey(String.format("%s %s:%s", date.getKey(), preparedHours, preparedMinutes));

        observers.forEach(observer -> observer.updateView(timeObject, Actions.PICK_TIME));

        dismiss();
    }
}