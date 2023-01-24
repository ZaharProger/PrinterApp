package com.example.printerapp.fragments;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.printerapp.constants.Routes;
import com.example.printerapp.entities.BaseEntity;

public class BaseFragment extends Fragment {
    protected Routes fragmentViewId;
    protected View fragmentView;

    public Routes getFragmentViewId() {
        return fragmentViewId;
    }
}
