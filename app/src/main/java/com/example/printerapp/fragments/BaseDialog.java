package com.example.printerapp.fragments;

import androidx.fragment.app.DialogFragment;

import java.util.List;

public class BaseDialog<T> extends DialogFragment {
    protected List<IUpdatable<T>> observers;

    public BaseDialog(List<IUpdatable<T>> observers) {
        this.observers = observers;
    }
}
