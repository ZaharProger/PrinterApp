package com.example.printerapp.entities;

public class Order extends BaseEntity<Integer> {
    private String name;
    private int amount;
    private long startDate;
    private long endDate;
    private String customerName;
    private String customerPhone;
    private double size;
    private Resource resource;

    public Order(int key, String name, int amount, long startDate, long endDate, String customerName,
                 String customerPhone, double size, Resource resource) {
        this.key = key;
        this.name = name;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.size = size;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public double getSize() {
        return size;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public Resource getResource() {
        return resource;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }
}
