package com.example.printerapp.fragments;

import com.example.printerapp.constants.Actions;
import com.example.printerapp.entities.BaseEntity;

public interface IUpdatable<T> {
    void updateView(BaseEntity<T> relatedEntity, Actions relatedAction);
}
