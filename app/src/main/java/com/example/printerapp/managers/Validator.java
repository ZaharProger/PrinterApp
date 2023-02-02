package com.example.printerapp.managers;

import android.widget.TextView;

import com.example.printerapp.constants.ValidationTypes;

import java.util.List;
import java.util.stream.Collectors;

public class Validator {
    public static boolean validate(String data, ValidationTypes validationType) {
        return data.matches(validationType.getStringValue());
    }

    public static List<TextView> getEmptyFields(List<TextView> fields) {
        return fields.stream()
                .filter(field -> field.getText().toString()
                        .matches(ValidationTypes.EMPTY_FIELD.getStringValue()))
                .collect(Collectors.toList());
    }
}
