package com.example.printerapp.constants;

public enum Actions implements EnumDecoder {
    EDIT_ORDER("EDIT_ORDER"),
    FINISH_ORDER("FINISH_ORDER"),
    DELETE_ORDER("DELETE_ORDER"),
    UPDATE_VIEW("UPDATE_VIEW");

    private String stringValue;

    Actions(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }
}
