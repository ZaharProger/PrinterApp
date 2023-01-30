package com.example.printerapp.entities;

public class Waste extends BaseEntity<Integer> {
    private int wasteMonth;
    private int wasteYear;
    private double electricityAmount;
    private double resourceAmount;

    public Waste(int key, int wasteMonth, int wasteYear,
                 double electricityAmount, double resourceAmount) {

        this.key = key;
        this.wasteMonth = wasteMonth;
        this.wasteYear = wasteYear;
        this.electricityAmount = electricityAmount;
        this.resourceAmount = resourceAmount;
    }

    public int getWasteMonth() {
        return wasteMonth;
    }

    public int getWasteYear() {
        return wasteYear;
    }

    public double getElectricityAmount() {
        return electricityAmount;
    }

    public double getResourceAmount() {
        return resourceAmount;
    }
}
