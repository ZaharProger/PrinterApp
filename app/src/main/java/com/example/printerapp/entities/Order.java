package com.example.printerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Order extends BaseEntity<Integer> implements Parcelable {
    private String name;
    private int amount;
    private long startDate;
    private long endDate;
    private Customer customer;
    private double size;
    private Resource resource;

    public Order(int key, String name, int amount, long startDate, long endDate, Customer customer,
                 double size, Resource resource) {
        this.key = key;
        this.name = name;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.size = size;
        this.resource = resource;
    }

    public Order(Parcel parcel) {
        key = parcel.readInt();
        name = parcel.readString();
        amount = parcel.readInt();
        startDate = parcel.readLong();
        endDate = parcel.readLong();
        customer = new Customer(parcel.readInt(), parcel.readString(), parcel.readString());
        size = parcel.readDouble();
        resource = new Resource(parcel.readInt(), parcel.readString(), parcel.readDouble());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel parcel) {
            return new Order(parcel);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

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

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(key);
        parcel.writeString(name);
        parcel.writeInt(amount);
        parcel.writeLong(startDate);
        parcel.writeLong(endDate);
        parcel.writeInt(customer.key);
        parcel.writeString(customer.getName());
        parcel.writeString(customer.getPhone());
        parcel.writeDouble(size);
        parcel.writeInt(resource.key);
        parcel.writeString(resource.getName());
        parcel.writeDouble(resource.getPrice());
    }
}
