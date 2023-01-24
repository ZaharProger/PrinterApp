package com.example.printerapp.fragments;

import android.view.View;

import com.example.printerapp.entities.BaseEntity;

public interface IUpdatable {
    void updateView(BaseEntity<Integer> relatedEntity, View relatedView);
}
