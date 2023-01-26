package com.example.printerapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.printerapp.R;
import com.example.printerapp.entities.Resource;

import java.util.ArrayList;

public class ResourcesListAdapter extends ArrayAdapter<String> {
    private ArrayList<Resource> resources;
    private int viewId;

    public ResourcesListAdapter(@NonNull Context context, int viewId,
                                ArrayList<Resource> resources) {
        super(context, viewId, resources.stream()
                .map(resource -> String.format("%s (+%s руб/см3)",
                        resource.getName(), resource.getPrice()))
                .toArray(String[]::new));

        this.viewId = viewId;
        this.resources = resources;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(getContext(), viewId, null);

            TextView resourceName = convertView.findViewById(R.id.resourceNameText);
            Resource currentResource = resources.get(position);

            resourceName.setText(currentResource.getName());
        }

        return convertView;
    }

    public Resource getResourceByIndex(int index) {
        return resources.get(index);
    }
}
