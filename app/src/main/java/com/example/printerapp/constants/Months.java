package com.example.printerapp.constants;

import java.util.Arrays;
import java.util.List;

public enum Months implements EnumDecoder {
    JANUARY("Январь"),
    FEBRUARY("Февраль"),
    MARCH("Март"),
    APRIL("Апрель"),
    MAY("Май"),
    JUNE("Июнь"),
    JULY("Июль"),
    AUGUST("Август"),
    SEPTEMBER("Сентябрь"),
    OCTOBER("Октябрь"),
    NOVEMBER("Ноябрь"),
    DECEMBER("Декабрь");

    private String stringValue;

    Months(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String getStringValue() {
        return stringValue.split("[\\s]")[0];
    }

    public static Months getMonthByNumber(int number) {
        List<Months> months = Arrays.asList(
                JANUARY, FEBRUARY, MARCH, APRIL,
                MAY, JUNE, JULY, AUGUST,
                SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
        );

        return months.get(number - 1);
    }
}
