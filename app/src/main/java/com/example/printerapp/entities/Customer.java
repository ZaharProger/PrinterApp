package com.example.printerapp.entities;

public class Customer extends BaseEntity<Integer> {
    private String name;
    private String phone;

    public Customer(int key, String name, String phone) {
        this.key = key;
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
