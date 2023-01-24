package com.example.printerapp.entities;

public class Resource extends BaseEntity<Integer> {
    private String name;
    private double price;

    public Resource(int key, String name, double price) {
        this.key = key;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
