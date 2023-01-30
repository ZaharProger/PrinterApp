package com.example.printerapp.constants;

public enum IntentValues implements EnumDecoder {
    ORDER("order");

    private String stringValue;

    IntentValues(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String getStringValue() {
        return null;
    }
}
