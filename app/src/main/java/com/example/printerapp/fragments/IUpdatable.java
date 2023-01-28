package com.example.printerapp.fragments;

import com.example.printerapp.constants.Actions;
import com.example.printerapp.entities.BaseEntity;

public interface IUpdatable {
    void updateView(BaseEntity<Integer> relatedEntity, Actions relatedAction);
}
