package com.example.printerapp.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.printerapp.constants.DbValues;
import com.example.printerapp.entities.BaseEntity;
import com.example.printerapp.entities.Customer;
import com.example.printerapp.entities.Order;
import com.example.printerapp.entities.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DbManager extends SQLiteOpenHelper {
    private ArrayList<Order> orders;
    private ArrayList<Resource> resources;
    private ArrayList<Customer> customers;
    private static DbManager dbManager;

    private DbManager(Context context) {
        super(context, DbValues.DB_NAME.getStringValue(), null,
                Integer.parseInt(DbValues.DB_SCHEMA.getStringValue()));

        orders = new ArrayList<>();
        resources = new ArrayList<>();
        customers = new ArrayList<>();
    }

    public static synchronized DbManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DbManager(context);
        }
        dbManager.fillRepositories();

        return dbManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        List<DbValues> requests = Arrays.asList(
                DbValues.CREATE_RESOURCE_TYPES_TABLE,
                DbValues.CREATE_ORDERS_TABLE,
                DbValues.CREATE_WASTES_TABLE,
                DbValues.INSERT_RESOURCE_TYPES
        );
        for (DbValues request : requests) {
            sqLiteDatabase.execSQL(request.getStringValue());
        }
    }

    private void fillRepositories() {
        orders.clear();
        resources.clear();
        customers.clear();

        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();

            Cursor cursor = sqLiteDatabase
                    .rawQuery(DbValues.GET_ORDERS.getStringValue(), null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int orderIdColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_ID.getStringValue());
                    int orderNameColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_NAME.getStringValue());
                    int orderAmountColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_AMOUNT.getStringValue());
                    int orderStartDateColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_START_DATE.getStringValue());
                    int orderEndDateColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_END_DATE.getStringValue());
                    int orderCustomerNameColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_CUSTOMER_NAME.getStringValue());
                    int orderCustomerPhoneColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_CUSTOMER_PHONE.getStringValue());
                    int orderSizeColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_SIZE.getStringValue());
                    int orderResourceIdColumnIndex = cursor.getColumnIndex(DbValues
                            .RESOURCE_ID.getStringValue());
                    int orderResourceNameColumnIndex = cursor.getColumnIndex(DbValues
                            .RESOURCE_NAME.getStringValue());
                    int orderResourcePriceColumnIndex = cursor.getColumnIndex(DbValues
                            .RESOURCE_PRICE.getStringValue());

                    do {
                       orders.add(new Order(
                               cursor.getInt(orderIdColumnIndex),
                               cursor.getString(orderNameColumnIndex),
                               cursor.getInt(orderAmountColumnIndex),
                               cursor.getLong(orderStartDateColumnIndex),
                               cursor.getLong(orderEndDateColumnIndex),
                               new Customer(
                                       0,
                                       cursor.getString(orderCustomerNameColumnIndex),
                                       cursor.getString(orderCustomerPhoneColumnIndex)
                               ),
                               cursor.getDouble(orderSizeColumnIndex),
                               new Resource(
                                       cursor.getInt(orderResourceIdColumnIndex),
                                       cursor.getString(orderResourceNameColumnIndex),
                                       cursor.getDouble(orderResourcePriceColumnIndex)
                               )
                       ));
                    } while (cursor.moveToNext());
                }
            }

            cursor = sqLiteDatabase.rawQuery(DbValues
                    .GET_RESOURCES.getStringValue(), null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int resourceIdColumnIndex = cursor.getColumnIndex(DbValues
                                .RESOURCE_ID.getStringValue());
                        int resourceNameColumnIndex = cursor.getColumnIndex(DbValues
                                .RESOURCE_NAME.getStringValue());
                        int resourcePriceColumnIndex = cursor.getColumnIndex(DbValues
                                .RESOURCE_PRICE.getStringValue());

                        resources.add(new Resource(
                                cursor.getInt(resourceIdColumnIndex),
                                cursor.getString(resourceNameColumnIndex),
                                cursor.getDouble(resourcePriceColumnIndex)
                        ));

                    } while (cursor.moveToNext());
                }
            }

            for (int i = 0; i < orders.size(); ++i) {
                Customer customer = orders.get(i).getCustomer();
                boolean isExists = false;
                for (int j = i + 1; j < orders.size() && !isExists; ++j) {
                    isExists = customer.getName().equals(orders.get(j).getCustomer().getName());
                }

                if (!isExists) {
                    customers.add(customer);
                }
            }

            if (cursor != null) {
                cursor.close();
            }
        }
        catch (SQLException ignored) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion == oldVersion + 1) {
            oldVersion = newVersion;
            List<DbValues> deleteRequests = Arrays.asList(
                    DbValues.DROP_ORDERS_TABLE,
                    DbValues.DROP_RESOURCE_TYPES_TABLE,
                    DbValues.DROP_WASTES_TABLE
            );
            for (DbValues deleteRequest : deleteRequests) {
                sqLiteDatabase.execSQL(deleteRequest.getStringValue());
            }

            onCreate(sqLiteDatabase);
        }
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void addOrder(Order order) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(DbValues.INSERT_ORDER.getStringValue(), new Object[]{
                order.getName(),
                order.getAmount(),
                order.getStartDate(),
                order.getEndDate(),
                order.getCustomer().getName(),
                order.getCustomer().getPhone(),
                order.getSize(),
                order.getResource().getKey()
        });
    }

    public void deleteOrder(int orderId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(DbValues.DELETE_ORDER_BY_ID.getStringValue(), new Object[]{
                orderId
        });
    }

    public ArrayList<Order> filterOrders(BaseEntity<String> customerFilter) {
        return customerFilter == null ? getOrders() : (ArrayList<Order>) orders.stream()
                .filter(order -> order.getCustomer().getName().equals(customerFilter.getKey()))
                .collect(Collectors.toList());
    }
}
