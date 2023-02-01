package com.example.printerapp.constants;

public enum ValidationTypes implements EnumDecoder {
    NUM("[0123456789]"),
    FLOAT_NUM("[0123456789.]");

    private String stringValue;

    ValidationTypes(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }
}
