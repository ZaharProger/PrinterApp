package com.example.printerapp.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BaseSpinnerAdapter<T> extends ArrayAdapter<String> {
    protected int viewId;
    protected ArrayList<T> items;


    public BaseSpinnerAdapter(@NonNull Context context, int viewId, String[] items) {
        super(context, viewId, items);
        this.viewId = viewId;
    }

    public T getItemByIndex(int index) {
        return items.get(index);
    }
}
