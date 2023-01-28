package com.example.printerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.printerapp.R;
import com.example.printerapp.constants.Actions;
import com.example.printerapp.entities.Customer;
import com.example.printerapp.entities.Order;
import com.example.printerapp.fragments.IUpdatable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrdersListAdapter extends
        RecyclerView.Adapter<OrdersListAdapter.OrdersListViewHolder> {

    private Context context;
    private ArrayList<Order> orders;
    private List<IUpdatable> observers;
    private boolean isSortAsc;

    public OrdersListAdapter(Context context, ArrayList<Order> orders, List<IUpdatable> observers) {
        this.context = context;
        this.orders = orders;
        this.observers = observers;
        isSortAsc = true;
        sortOrders();

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
        holder.orderCustomerNameText.setText(order.getCustomer().getName());
        holder.orderCustomerPhoneText.setText(order.getCustomer().getPhone());

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT);
        holder.orderStartDateText.setText(String.format("%s%n%s",
                context.getString(R.string.start_date),
                format.format(new Date(order.getStartDate()))));
        holder.orderEndDateText.setText(String.format("%s%n%s",
                context.getString(R.string.end_date),
                format.format(new Date(order.getEndDate()))));

        holder.deleteButton.setOnClickListener(view -> {
            observers.forEach(observer -> observer.updateView(order, Actions.DELETE_ORDER));
        });
        holder.editButton.setOnClickListener(view -> {
            observers.forEach(observer -> observer.updateView(order, Actions.EDIT_ORDER));
        });
        holder.finishButton.setOnClickListener(view -> {
            observers.forEach(observer -> observer.updateView(order, Actions.FINISH_ORDER));
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
        sortOrders();

        notifyDataSetChanged();
    }

    public void deleteOrderById(int orderId) {
        orders.removeIf(order -> order.getKey() == orderId);
        notifyDataSetChanged();
    }

    public ArrayList<Customer> getCustomers() {
        return (ArrayList<Customer>) orders.stream()
                .map(Order::getCustomer)
                .collect(Collectors.toList());
    }

    public void sortOrders() {
        if (isSortAsc) {
            orders.sort(Comparator.comparingLong(Order::getEndDate));
        }
        else {
            orders.sort(Comparator.comparingLong(Order::getEndDate).reversed());
        }

        notifyDataSetChanged();
    }

    public void setSortAsc(boolean isSortAsc) {
        this.isSortAsc = isSortAsc;
    }

    public boolean isSortAsc() {
        return isSortAsc;
    }

    static class OrdersListViewHolder extends RecyclerView.ViewHolder {
        private final TextView orderPriceText;
        private final TextView orderNameText;
        private final TextView orderAmountText;
        private final TextView orderSizeText;
        private final TextView orderResourceText;
        private final TextView orderStartDateText;
        private final TextView orderEndDateText;
        private final TextView orderCustomerNameText;
        private final TextView orderCustomerPhoneText;
        private final ImageButton finishButton;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

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

            finishButton = itemView.findViewById(R.id.finishButton);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
