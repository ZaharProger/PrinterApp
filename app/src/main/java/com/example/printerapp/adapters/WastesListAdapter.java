package com.example.printerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.printerapp.R;
import com.example.printerapp.constants.Months;
import com.example.printerapp.entities.Waste;

import java.util.ArrayList;

public class WastesListAdapter extends
        RecyclerView.Adapter<WastesListAdapter.WastesListViewHolder> {

    private Context context;
    private Waste currentWaste;
    private ArrayList<Waste> wastes;

    public WastesListAdapter(Context context, Waste currentWaste, ArrayList<Waste> wastes) {
        this.context = context;
        this.currentWaste = currentWaste;
        this.wastes = wastes;

        notifyDataSetChanged();
    }

    @NonNull
    public WastesListAdapter.WastesListViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wastes_list_item, parent, false);

        return new WastesListAdapter.WastesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WastesListViewHolder holder, int position) {
        Waste waste = wastes.get(position);

        holder.wasteDateText.setText(String.format(
                "%s %sг.",
                Months.getMonthByNumber(waste.getWasteMonth()).getStringValue(),
                waste.getWasteYear()
        ));
        holder.itemElectricityAmount.setText(String.format("%.2f кВт", waste.getElectricityAmount()));
        holder.itemResourceAmount.setText(String.format("%.2f см3", waste.getResourceAmount()));

        double electricityDiff = currentWaste.getElectricityAmount() - waste.getElectricityAmount();
        int electricityDiffColor = electricityDiff < 0? R.color.negative_stat : electricityDiff > 0?
                R.color.positive_stat : R.color.same_stat;

        double resourceDiff = currentWaste.getResourceAmount() - waste.getResourceAmount();
        int resourceDiffColor = resourceDiff < 0? R.color.negative_stat : resourceDiff > 0?
                R.color.positive_stat : R.color.same_stat;

        holder.itemElectricityAmount.setTextColor(context.getColor(electricityDiffColor));
        holder.itemResourceAmount.setTextColor(context.getColor(resourceDiffColor));
    }

    @Override
    public int getItemCount() {
        return wastes.size();
    }

    static class WastesListViewHolder extends RecyclerView.ViewHolder {
        private TextView wasteDateText;
        private TextView itemElectricityAmount;
        private TextView itemResourceAmount;

        public WastesListViewHolder(@NonNull View itemView) {
            super(itemView);

            wasteDateText = itemView.findViewById(R.id.wasteDateText);
            itemElectricityAmount = itemView.findViewById(R.id.itemElectricityAmount);
            itemResourceAmount = itemView.findViewById(R.id.itemResourceAmount);
        }
    }
}
