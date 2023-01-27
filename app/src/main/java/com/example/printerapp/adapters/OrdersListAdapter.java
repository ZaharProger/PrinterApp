package com.example.printerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.printerapp.R;
import com.example.printerapp.entities.Order;
import com.example.printerapp.fragments.IUpdatable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersListAdapter extends
        RecyclerView.Adapter<OrdersListAdapter.OrdersListViewHolder> {

    private Context context;
    private ArrayList<Order> orders;
    private List<IUpdatable> observers;

    public OrdersListAdapter(Context context, ArrayList<Order> orders, List<IUpdatable> observers) {
        this.context = context;
        this.orders = orders;
        this.observers = observers;

        notifyDataSetChanged();
    }

    @NonNull
    public OrdersListAdapter.OrdersListViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);

        return new OrdersListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersListViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.orderPriceText.setText(String.format("%.1f руб.", order.getAmount() *
                order.getSize() * order.getResource().getPrice()));
        holder.orderNameText.setText(order.getName());
        holder.orderAmountText.setText(String.format("%s: %s шт.",
                context.getString(R.string.amount), order.getAmount()));
        holder.orderSizeText.setText(String.format("%s: %s см3",
                context.getString(R.string.size), order.getSize()));
        holder.orderResourceText.setText(String.format("%s: %s", context.getString(R.string.resource),
                order.getResource().getName()));
        holder.orderCustomerNameText.setText(order.getCustomerName());
        holder.orderCustomerPhoneText.setText(order.getCustomerPhone());

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT);
        holder.orderStartDateText.setText(String.format("%s%n%s",
                context.getString(R.string.start_date),
                format.format(new Date((long) order.getStartDate()))));
        holder.orderEndDateText.setText(String.format("%s%n%s",
                context.getString(R.string.end_date),
                format.format(new Date((long) order.getEndDate()))));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setAnswers(ArrayList<Order> orders) {
        this.orders.clear();
        this.orders.addAll(orders);

        notifyDataSetChanged();
    }

    static class OrdersListViewHolder extends RecyclerView.ViewHolder {
        private TextView orderPriceText;
        private TextView orderNameText;
        private TextView orderAmountText;
        private TextView orderSizeText;
        private TextView orderResourceText;
        private TextView orderStartDateText;
        private TextView orderEndDateText;
        private TextView orderCustomerNameText;
        private TextView orderCustomerPhoneText;

        public OrdersListViewHolder(@NonNull View itemView) {
            super(itemView);

            orderPriceText = itemView.findViewById(R.id.orderPriceText);
            orderNameText = itemView.findViewById(R.id.orderNameText);
            orderAmountText = itemView.findViewById(R.id.orderAmountText);
            orderSizeText = itemView.findViewById(R.id.orderSizeText);
            orderResourceText = itemView.findViewById(R.id.orderResourceText);
            orderStartDateText = itemView.findViewById(R.id.orderStartDateText);
            orderEndDateText = itemView.findViewById(R.id.orderEndDateText);
            orderCustomerNameText = itemView.findViewById(R.id.orderCustomerNameText);
            orderCustomerPhoneText = itemView.findViewById(R.id.orderCustomerPhoneText);
        }
    }
}
