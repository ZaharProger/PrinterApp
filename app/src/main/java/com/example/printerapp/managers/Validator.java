package com.example.printerapp.managers;

import com.example.printerapp.constants.ValidationTypes;

public class Validator {
    public static boolean validate(String data, ValidationTypes validationType) {
        return data.matches(validationType.getStringValue());
    }
}
